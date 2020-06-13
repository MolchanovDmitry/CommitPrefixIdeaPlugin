package main.kotlin.dmitriy.molchanov.ui.add

import com.intellij.openapi.ui.DialogWrapper
import javax.swing.*


class AddRuleDialog : DialogWrapper(true) {
    private val gitRepEdit = JTextField(15)
    private val prefixEdit = JTextField(15)

    init {
        init()
        title = "Добавление правила"
    }

    override fun createCenterPanel(): JComponent {
        // Создание панели для размещение компонентов
        val root = BoxLayoutUtils.createVerticalPanel()
        val getRepLabel = JLabel(REPOSITORY)
        val prefixLabel = JLabel(REGEX_PREFIX)
        val gitRepGroup = getViewGroup(getRepLabel, gitRepEdit)
        val prefixGroup = getViewGroup(prefixLabel, prefixEdit)

        // Определение размеров надписей к текстовым полям
        GuiUtils.makeSameSize(arrayOf(getRepLabel, prefixLabel))

        root.add(gitRepGroup)
        root.add(prefixGroup)
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

    private companion object{
        const val REPOSITORY = "Git repo:"
        const val REGEX_PREFIX = "Regex prefix:"
    }
}