package dmitriy.molchanov.ui.main

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SizedIcon
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import dmitriy.molchanov.Strings
import dmitriy.molchanov.Strings.GITHUB_URL
import dmitriy.molchanov.Strings.LIKE_THIS_PLUGIN
import dmitriy.molchanov.Strings.STAR_ON_GITHUB
import dmitriy.molchanov.model.Rule
import org.jdesktop.swingx.HorizontalLayout
import org.jdesktop.swingx.VerticalLayout
import java.awt.Cursor
import java.awt.Desktop
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
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
        val columnNames = arrayOf(Strings.REPOSITORY_TITLE, Strings.REGEX_RULE, Strings.CHECK_STRING)
        tableModel = DefaultTableModel(data, columnNames)

        init()
        title = Strings.COMMIT_PLUGIN_SETTINGS
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
            val repo = tableModel.getValueAt(it, REP_INDEX) as String
            val regexPrefix = tableModel.getValueAt(it, REGEX_INDEX) as String
            val checkString = tableModel.getValueAt(it, CHECK_INDEX) as String
            Rule(repo, regexPrefix, checkString)
        }.toList()

    override fun createCenterPanel(): JComponent {
        val root = JPanel(VerticalLayout())
        val dialogPanel = JPanel(HorizontalLayout())

        table = getTable()
        val rightPanel = getRightPanel()
        val scrollPane = JBScrollPane(table)

        dialogPanel.add(scrollPane)
        dialogPanel.add(rightPanel)
        root.add(dialogPanel)
        root.add(getBottomLink())

        return root
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

    private fun getBottomLink() =
        JPanel(HorizontalLayout()).apply {
            val label = JLabel(LIKE_THIS_PLUGIN)
            val link = JLabel("<html><a href='$GITHUB_URL'><font color=#5597EB>$STAR_ON_GITHUB</font></a></html>")
            link.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    Desktop.getDesktop().browse(URI(GITHUB_URL))
                }

                override fun mouseEntered(e: MouseEvent?) {
                    cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                }

                override fun mouseExited(e: MouseEvent?) {
                    cursor = Cursor.getDefaultCursor()
                }
            })
            add(label)
            add(link)
        }

    interface OnSettingsDialogListener {
        fun onAddClick()
        fun onRemoveClick()
        fun onEditClick()
    }

    private companion object {
        const val ICON_SIZE = 25
        const val REP_INDEX = 0
        const val REGEX_INDEX = 1
        const val CHECK_INDEX = 2
    }
}