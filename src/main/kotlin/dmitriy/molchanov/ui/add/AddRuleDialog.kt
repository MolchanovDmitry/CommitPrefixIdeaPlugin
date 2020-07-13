package main.kotlin.dmitriy.molchanov.ui.add

import com.intellij.openapi.ui.DialogWrapper
import main.kotlin.dmitriy.molchanov.model.Rule
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*


class AddRuleDialog(editablePrefix: Rule? = null) : DialogWrapper(true) {

    val rule: Rule
        get() = Rule(gitRepEdit.text, prefixEdit.text, checkStringEdit.text)

    private val gitRepEdit = JTextField(TEXT_COLUMNS)
    private val prefixEdit = JTextField(TEXT_COLUMNS)
    private val checkStringEdit = JTextField(TEXT_COLUMNS)
    private val statusLabel = JLabel("Заполните поля")

    private val keyListener = object : KeyListener {
        override fun keyTyped(p0: KeyEvent?) {}
        override fun keyPressed(p0: KeyEvent?) {}
        override fun keyReleased(p0: KeyEvent?) {
            statusLabel.text = getMessage()
        }
    }

    init {
        init()
        title = "Добавление правила"
        editablePrefix?.gitRepo?.let(gitRepEdit::setText)
        editablePrefix?.regexPrefix?.let(prefixEdit::setText)
        editablePrefix?.checkString?.let(checkStringEdit::setText)
        statusLabel.text = getMessage()
    }

    override fun createCenterPanel(): JComponent {
        // Создание панели для размещение компонентов
        val root = BoxLayoutUtils.createVerticalPanel()
        val getRepLabel = JLabel(REPOSITORY)
        val prefixLabel = JLabel(REGEX_PREFIX)
        val checkStringLabel = JLabel(CHECK_STRING)
        val gitRepGroup = getViewGroup(getRepLabel, gitRepEdit)
        val prefixGroup = getViewGroup(prefixLabel, prefixEdit)
        val statusGroup = getCheckText(statusLabel)
        val checkStringGroup = getViewGroup(checkStringLabel, checkStringEdit)

        // Определение размеров надписей к текстовым полям
        GuiUtils.makeSameSize(arrayOf(getRepLabel, prefixLabel, checkStringLabel))

        root.add(gitRepGroup)
        root.add(prefixGroup)
        root.add(checkStringGroup)
        root.add(statusGroup)
        return root
    }

    /** Заголовок и ввод git репозитория */
    private fun getViewGroup(label: JLabel, textField: JTextField): JPanel {
        textField.addKeyListener(keyListener)
        val group = BoxLayoutUtils.createHorizontalPanel()
        group.add(label)
        group.add(Box.createHorizontalStrut(10))
        group.add(textField)
        return group
    }

    private fun getCheckText(statusLabel: JLabel): JPanel {
        val group = BoxLayoutUtils.createHorizontalPanel()
        group.add(statusLabel)
        return group
    }

    private fun getMessage(): String {
        var message = ""
        if (gitRepEdit.text.isEmpty()) message += "Git repo пуст, "
        if (prefixEdit.text.isEmpty()) message += "Regex prefix пуст, "
        if (checkStringEdit.text.isEmpty()) message += "Check string пуст, "
        if (message.isNotEmpty()) {
            val showedMessage = message.substring(0, message.length - 2)
            return "<html><font color=red>$showedMessage</font></html>"
        }
        val regex = Regex(prefixEdit.text)
        val checkStr = checkStringEdit.text
        regex.find(checkStr)?.range?.apply {
            val lastMatch = if (last == checkStr.length) last else last + 1
            val startText = checkStr.substring(0, first)
            val matchText = checkStr.substring(first, lastMatch)
            val endText = checkStr.substring(lastMatch)
            return "<html>" +
                    "<font color=black>$startText</font>" +
                    "<font color=green>$matchText</font>" +
                    "<font color=black>$endText</font>" +
                    "</html>"
        }
        return "<html><font color=red>Совпадения не найдены</font></html>"
    }

    private companion object {
        const val REPOSITORY = "Git repo:"
        const val REGEX_PREFIX = "Regex prefix:"
        const val CHECK_STRING = "Check branch:"
        const val TEXT_COLUMNS = 15
    }
}