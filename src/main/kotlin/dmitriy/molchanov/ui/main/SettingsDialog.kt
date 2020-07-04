package main.kotlin.dmitriy.molchanov.ui.main

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SizedIcon
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import main.kotlin.dmitriy.molchanov.model.Rule
import org.jdesktop.swingx.HorizontalLayout
import org.jdesktop.swingx.VerticalLayout
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel


class SettingsDialog(
        private val listener: OnSettingsDialogListener

) : DialogWrapper(true) {

    private val tableModel: DefaultTableModel
    private lateinit var table: JBTable

    init {
        val data = Array(0) { arrayOfNulls<String>(3) }
        val columnNames = arrayOf(REPOSITORY_TITLE, REGEX_RULE, CHECK_STRING)
        tableModel = DefaultTableModel(data, columnNames)

        init()
        title = "Настройки плагина префикса коммитов"
    }

    fun addRule(rule: Rule) {
        arrayOf(rule.gitRepo, rule.regexPrefix, rule.checkString).let(tableModel::addRow)
    }

    fun addRules(rules: List<Rule>) {
        rules.forEach(::addRule)
    }

    fun clearRules() {
        for (i in tableModel.rowCount - 1 downTo 0) {
            tableModel.removeRow(i)
        }
    }

    fun getSelectedRules() = table.selectedRows
            .map {
                val repo = tableModel.getValueAt(it, 0) as String
                val regexPrefix = tableModel.getValueAt(it, 1) as String
                val checkString = tableModel.getValueAt(it, 2) as String
                Rule(repo, regexPrefix, checkString)
            }.toList()

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(HorizontalLayout())

        table = getTable()
        val rightPanel = getRightPanel()
        val scrollPane = JBScrollPane(table)

        dialogPanel.add(scrollPane)
        dialogPanel.add(rightPanel)
        return dialogPanel
    }

    private fun getTable(): JBTable {
        val table = JBTable(tableModel)
        table.preferredScrollableViewportSize = Dimension(600, 200)
        table.fillsViewportHeight = true
        return table
    }

    private fun getRightPanel(): JPanel {
        val rightPanel = JPanel(VerticalLayout())
        val addImage = addImage(AllIcons.General.Add, listener::onAddClick)
        val remove = addImage(AllIcons.General.Remove, listener::onRemoveClick)
        val edit = addImage(AllIcons.Actions.Edit, listener::onEditClick)
        rightPanel.add(addImage)
        rightPanel.add(remove)
        rightPanel.add(edit)
        return rightPanel
    }

    private fun addImage(icon: Icon, clickAction: () -> Unit): JLabel {
        val sizedIcon = SizedIcon(icon, ICON_SIZE, ICON_SIZE)
        val label = JLabel(sizedIcon)
        label.addMouseListener(object : MouseClickListener {
            override fun mousePressed(p0: MouseEvent?) = clickAction()
        })
        return label
    }

    interface OnSettingsDialogListener {
        fun onAddClick()
        fun onRemoveClick()
        fun onEditClick()
    }

    private companion object {
        const val ICON_SIZE = 25
        const val REGEX_RULE = "Regex prefix"
        const val REPOSITORY_TITLE = "Git repository"
        const val CHECK_STRING = "Check string"
    }
}