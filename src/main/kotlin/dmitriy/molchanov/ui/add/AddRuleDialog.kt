package main.kotlin.dmitriy.molchanov.ui.add

import com.intellij.openapi.ui.DialogWrapper
import main.kotlin.dmitriy.molchanov.model.Rule
import javax.swing.*


class AddRuleDialog(editablePrefix: Rule? = null) : DialogWrapper(true) {

    val rule: Rule
        get() = Rule(gitRepEdit.text, prefixEdit.text, checkStringEdit.text)

    private val gitRepEdit = JTextField(TEXT_COLUMNS)
    private val prefixEdit = JTextField(TEXT_COLUMNS)
    private val checkStringEdit = JTextField(TEXT_COLUMNS)

    init {
        init()
        title = "Добавление правила"
        editablePrefix?.gitRepo?.let(gitRepEdit::setText)
        editablePrefix?.regexPrefix?.let(prefixEdit::setText)
    }

    override fun createCenterPanel(): JComponent {
        // Создание панели для размещение компонентов
        val root = BoxLayoutUtils.createVerticalPanel()
        val getRepLabel = JLabel(REPOSITORY)
        val prefixLabel = JLabel(REGEX_PREFIX)
        val checkStringLabel = JLabel(CHECK_STRING)
        val gitRepGroup = getViewGroup(getRepLabel, gitRepEdit)
        val prefixGroup = getViewGroup(prefixLabel, prefixEdit)
        val checkStringGroup = getViewGroup(checkStringLabel, checkStringEdit)

        // Определение размеров надписей к текстовым полям
        GuiUtils.makeSameSize(arrayOf(getRepLabel, prefixLabel, checkStringLabel))

        root.add(gitRepGroup)
        root.add(prefixGroup)
        root.add(checkStringGroup)
        return root
    }

    /** Заголовок и ввод git репозитория */
    private fun getViewGroup(label: JLabel, textField: JTextField): JPanel {
        val group = BoxLayoutUtils.createHorizontalPanel()
        group.add(label)
        group.add(Box.createHorizontalStrut(10))
        group.add(textField)
        return group
    }

    private companion object {
        const val REPOSITORY = "Git repo:"
        const val REGEX_PREFIX = "Regex prefix:"
        const val CHECK_STRING = "Check string:"
        const val TEXT_COLUMNS = 15
    }
}