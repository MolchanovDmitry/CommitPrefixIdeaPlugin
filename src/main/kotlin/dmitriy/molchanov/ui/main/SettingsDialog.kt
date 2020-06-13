package main.kotlin.dmitriy.molchanov.ui.main

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SizedIcon
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
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

    init {
        init()
        title = "Настройки плагина префикса коммитов"
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(HorizontalLayout())

        val table = getTable()
        val rightPanel = getRightPanel()
        val scrollPane = JBScrollPane(table)

        dialogPanel.add(scrollPane)
        dialogPanel.add(rightPanel)
        return dialogPanel
    }

    private fun getTable(): JBTable {
        val data = Array(2) { arrayOfNulls<String>(2) }
        val columnNames = arrayOf(REPOSITORY_TITLE, REGEX_RULE)
        data[0][0] = "test"
        data[0][1] = "test2"
        data[1][0] = "123"
        data[1][1] = "button"

        val model = DefaultTableModel(data, columnNames)
        val table = JBTable(model)
        table.preferredScrollableViewportSize = Dimension(500, 200)
        table.fillsViewportHeight = true
        return table
    }

    private fun getRightPanel(): JPanel {
        val rightPanel = JPanel(VerticalLayout())
        val addImage = addImage(AllIcons.General.Add, listener::onAddClick)
        val remove = addImage(AllIcons.General.Remove, listener::onRemoveClick)
        rightPanel.add(addImage)
        rightPanel.add(remove)
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
    }

    private companion object {
        const val ICON_SIZE = 25
        const val REGEX_RULE = "Regex prefix"
        const val REPOSITORY_TITLE = "Git repository"
    }
}