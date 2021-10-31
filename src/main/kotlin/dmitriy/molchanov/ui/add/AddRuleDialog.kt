package dmitriy.molchanov.ui.add

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import dmitriy.molchanov.Strings
import dmitriy.molchanov.model.Rule
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*


/**
 * Дилог сохранения/редактирования правила
 *
 * @param editablePrefix редактируемое правило. Если null, значит создаем новое.
 * @param gitRepUrls список активных Git репозиториев (репозиториев открытых проектов).
 * @property getCurBranchByUrl функция получения текущей ветки по выбранному репозиторию.
 */
class AddRuleDialog(
    editablePrefix: Rule? = null,
    gitRepUrls: List<String>,
    private val getCurBranchByUrl: (String) -> String?
) : DialogWrapper(true) {

    val rule: Rule?
        get() = selectedGitRep?.let { Rule(it, prefixEdit.text, checkStringEdit.text) }

    private val selectedGitRep: String?
        get() = gitRepBox.selectedItem?.toString()
    private val prefixEdit = JTextField(TEXT_COLUMNS)
    private val checkStringEdit = JTextField(TEXT_COLUMNS)
    private val startWithEdit = JTextField(TEXT_COLUMNS)
    private val endWithEdit = JTextField(TEXT_COLUMNS)
    private val statusLabel = JLabel(Strings.FILL_FIELDS)
    private val resultTitle = JLabel(Strings.RESULT)
    private val resultTextField = JTextArea(Strings.COMMIT_MESSAGE).apply {
        addFocusListener(DisabledEditFocusListener(this))
    }

    private val gitRepBox = ComboBox(gitRepUrls.toTypedArray()).apply {
        isEditable = true
        addActionListener { updateCheckString() }
        editablePrefix?.gitRepo?.let(::setToolTipText)
    }
    private val registerBox = ComboBox(Strings.registers).apply {
        isEditable = true
        addActionListener { updateDialogStatus() }
    }

    private val keyListener = object : KeyListener {
        override fun keyTyped(p0: KeyEvent?) {}
        override fun keyPressed(p0: KeyEvent?) {}
        override fun keyReleased(p0: KeyEvent?) = updateDialogStatus()
    }

    init {
        init()
        title = Strings.ADD_RULE
        editablePrefix?.regexPrefix?.let(prefixEdit::setText)
        editablePrefix?.checkString
            ?.let(checkStringEdit::setText)
            ?: updateCheckString()
        updateDialogStatus()
    }

    override fun createCenterPanel(): JComponent {
        // Создание панели для размещение компонентов
        val root = BoxLayoutUtils.createVerticalPanel()

        val getRepLabel = JLabel(Strings.GIT_REPO)
        val prefixLabel = JLabel(Strings.REGEX_PREFIX)
        val registerLabel = JLabel(Strings.REGISTER)
        val messagePrefixLabel = JLabel(Strings.START_WITH)
        val messageSuffixLabel = JLabel(Strings.END_WITH)
        val checkStringLabel = JLabel(Strings.CHECK_BRANCH)

        val gitRepGroup = getViewGroup(getRepLabel, gitRepBox)
        val prefixGroup = getViewGroup(prefixLabel, prefixEdit)
        val checkStringGroup = getViewGroup(checkStringLabel, checkStringEdit)

        val registerGroup = getViewGroup(registerLabel, registerBox)
        val messagePrefixGroup = getViewGroup(messagePrefixLabel, startWithEdit)
        val messageSuffixGroup = getViewGroup(messageSuffixLabel, endWithEdit)
        val statusGroup = getCheckText(statusLabel)

        val messageGroup = getViewGroup(messagePrefixGroup, messageSuffixGroup)

        // Определение размеров надписей к текстовым полям
        GuiUtils.makeSameSize(arrayOf(getRepLabel, prefixLabel, checkStringLabel, registerLabel, messagePrefixLabel))

        root.add(gitRepGroup)
        root.add(prefixGroup)
        root.add(checkStringGroup)
        root.add(registerGroup)
        root.add(messageGroup)
        root.add(statusGroup)
        root.add(getCheckText(resultTitle))
        root.add(resultTextField)
        return root
    }

    /** Берем текущую ветку из выбранного репозитория и вставляем в [checkStringEdit] */
    private fun updateCheckString() {
        selectedGitRep
            ?.let(getCurBranchByUrl)
            ?.let(checkStringEdit::setText)
    }

    private fun updateDialogStatus() {
        val dialogStatusToPrefix = getDialogStatusToCorePrefix()
        val dialogStatus = dialogStatusToPrefix.first
        val prefix = dialogStatusToPrefix.second

        statusLabel.text = dialogStatus.message
        okAction.isEnabled = dialogStatus.shouldOkButtonActive

        resultTextField.text = (prefix?.formatByParams() ?: "") + Strings.COMMIT_MESSAGE
    }

    /** Получить строку вида "текст _ поле для ввода" */
    private fun getViewGroup(component: JComponent, componentWithListener: JComponent): JPanel =
        BoxLayoutUtils.createHorizontalPanel().apply {
            componentWithListener.addKeyListener(keyListener)
            add(component)
            add(Box.createHorizontalStrut(HORIZONTAL_STRUT))
            add(componentWithListener)
        }

    private fun getCheckText(statusLabel: JLabel): JPanel =
        BoxLayoutUtils.createHorizontalPanel().apply {
            add(statusLabel)
        }

    /**
     * Получить статус заполнения формы
     */
    private fun getDialogStatusToCorePrefix(): Pair<DialogStatus, String?> {
        var message = Strings.EMPTY
        if (selectedGitRep.isNullOrEmpty()) message += "${Strings.GIT_REPO_WARNING}, "
        if (prefixEdit.text.isNullOrEmpty()) message += "${Strings.REGEX_PREFIX_WARNING}, "
        if (checkStringEdit.text.isNullOrEmpty()) message += "${Strings.CHECK_BRANCH_WARNING}, "
        if (message.isNotEmpty()) {
            val showedMessage = message.substring(0, message.length - 2)
            return DialogStatus(
                message = "<html><font color=red>$showedMessage</font></html>",
                shouldOkButtonActive = false
            ) to null
        }
        val regex = Regex(prefixEdit.text)
        val checkStr = checkStringEdit.text
        regex.find(checkStr)?.range?.apply {
            val lastMatch = if (last == checkStr.length) last else last + 1
            val startText = checkStr.substring(0, first)
            val matchText = checkStr.substring(first, lastMatch)
            val endText = checkStr.substring(lastMatch)
            return DialogStatus(
                message = "<html>" +
                        "<font color=black>Match check: $startText</font>" +
                        "<font color=green>${matchText}</font>" +
                        "<font color=black>$endText</font>" +
                        "</html>",
                shouldOkButtonActive = true
            ) to matchText
        }
        return DialogStatus(
            message = "<html><font color=red>${Strings.NO_MATCHES_FOUND}</font></html>",
            shouldOkButtonActive = false
        ) to null
    }

    private fun String.formatByParams(): String {
        val prefix = startWithEdit.text
        val suffix = endWithEdit.text

        val isUpperCase = registerBox.selectedItem?.toString() == Strings.REGISTER_UPPER
        val isLowerCase = registerBox.selectedItem?.toString() == Strings.REGISTER_LOWER
        val core = when {
            isUpperCase -> uppercase()
            isLowerCase -> lowercase()
            else -> this
        }
        return "$prefix$core$suffix"
    }

    private class DialogStatus(val message: String, val shouldOkButtonActive: Boolean)

    private companion object {
        const val TEXT_COLUMNS = 15
        const val HORIZONTAL_STRUT = 10
    }
}