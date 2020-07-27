package main.kotlin.dmitriy.molchanov.rule

import com.intellij.openapi.ui.DialogWrapper
import main.kotlin.dmitriy.molchanov.rule.model.Rule
import main.kotlin.dmitriy.molchanov.utils.BoxLayoutUtils
import main.kotlin.dmitriy.molchanov.utils.GuiUtils
import main.kotlin.dmitriy.molchanov.utils.GuiUtils.getViewGroup
import main.kotlin.dmitriy.molchanov.utils.KeyReleasedListener
import java.awt.event.KeyEvent
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class AddRuleDialog(editablePrefix: Rule? = null) : DialogWrapper(true) {

    val rule: Rule
        get() = Rule(gitRepEdit.text, prefixEdit.text, checkStringEdit.text)

    private val gitRepEdit = JTextField(TEXT_COLUMNS)
    private val prefixEdit = JTextField(TEXT_COLUMNS)
    private val checkStringEdit = JTextField(TEXT_COLUMNS)
    private val statusLabel = JLabel("Заполните поля")

    init {
        init()
        title = "Добавление правила"
        editablePrefix?.gitRepo?.let(gitRepEdit::setText)
        editablePrefix?.regexPrefix?.let(prefixEdit::setText)
        editablePrefix?.checkString?.let(checkStringEdit::setText)
        updateDialogStatus()
    }

    override fun createCenterPanel(): JComponent {
        val keyReleasedListener = object : KeyReleasedListener {
            override fun keyReleased(var1: KeyEvent?)  = updateDialogStatus()
        }

        // Создание панели для размещение компонентов
        val root = BoxLayoutUtils.createVerticalPanel()
        val getRepLabel = JLabel(REPOSITORY)
        val prefixLabel = JLabel(REGEX_PREFIX)
        val checkStringLabel = JLabel(CHECK_STRING)
        val gitRepGroup = getViewGroup(getRepLabel, gitRepEdit, keyReleasedListener)
        val prefixGroup = getViewGroup(prefixLabel, prefixEdit, keyReleasedListener)
        val statusGroup = getCheckText(statusLabel)
        val checkStringGroup = getViewGroup(checkStringLabel, checkStringEdit, keyReleasedListener)

        // Определение размеров надписей к текстовым полям
        GuiUtils.makeSameSize(arrayOf(getRepLabel, prefixLabel, checkStringLabel))

        root.add(gitRepGroup)
        root.add(prefixGroup)
        root.add(checkStringGroup)
        root.add(statusGroup)
        return root
    }

    private fun updateDialogStatus() {
        val dialogStatus = getDialogStatus()
        statusLabel.text = dialogStatus.message
        okAction.isEnabled = dialogStatus.shouldOkButtonActive
    }

    private fun getCheckText(statusLabel: JLabel): JPanel {
        val group = BoxLayoutUtils.createHorizontalPanel()
        group.add(statusLabel)
        return group
    }

    /**
     * Получить статус заполнения формы
     */
    private fun getDialogStatus(): DialogStatus {
        var message = ""
        if (gitRepEdit.text.isEmpty()) message += "Git repo пуст, "
        if (prefixEdit.text.isEmpty()) message += "Regex prefix пуст, "
        if (checkStringEdit.text.isEmpty()) message += "Check string пуст, "
        if (message.isNotEmpty()) {
            val showedMessage = message.substring(0, message.length - 2)
            return DialogStatus(
                    message = "<html><font color=red>$showedMessage</font></html>",
                    shouldOkButtonActive = false)
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
                            "<font color=black>$startText</font>" +
                            "<font color=green>$matchText</font>" +
                            "<font color=black>$endText</font>" +
                            "</html>",
                    shouldOkButtonActive = true)
        }
        return DialogStatus(
                message = "<html><font color=red>Совпадения не найдены</font></html>",
                shouldOkButtonActive = false)
    }

    private class DialogStatus(val message: String, val shouldOkButtonActive: Boolean)

    private companion object {
        const val REPOSITORY = "Git repo:"
        const val REGEX_PREFIX = "Regex prefix:"
        const val CHECK_STRING = "Check branch:"
        const val TEXT_COLUMNS = 15
    }
}