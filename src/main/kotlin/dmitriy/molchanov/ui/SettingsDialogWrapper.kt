package main.kotlin.dmitriy.molchanov.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SizedIcon
import com.intellij.ui.table.JBTable
import org.jdesktop.swingx.HorizontalLayout
import org.jdesktop.swingx.VerticalLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel


class SettingsDialogWrapper : DialogWrapper(true) {

    init {
        init()
        title = "Настройки плагина префикса коммитов"
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(HorizontalLayout())

        val addImage = SizedIcon(AllIcons.General.Add, ICON_SIZE, ICON_SIZE)
        val removeImage =  SizedIcon(AllIcons.General.Remove, ICON_SIZE, ICON_SIZE)

        val rightPanel = JPanel(VerticalLayout())
        val addLabel = JLabel(addImage)
        val removeLabel = JLabel(removeImage)
        rightPanel.add(addLabel)
        rightPanel.add(removeLabel)
        rightPanel.addMouseListener(object: MouseClickListener{
            override fun mousePressed(p0: MouseEvent?) {
                println("click")
            }
        })


        val data = Array(2) { arrayOfNulls<Any>(2) }
        val columnNames = arrayOf("Machine Name", "Host Name (port)")
        data[0][0] = "test"
        data[0][1] = "test2"
        data[1][0] = "123"
        data[1][1] = "button"

        val model = DefaultTableModel(data, columnNames)
        val table = JBTable(model)
        table.preferredScrollableViewportSize = Dimension(500, 300)
        table.fillsViewportHeight = true


        dialogPanel.add(table, BorderLayout.CENTER)
        dialogPanel.add(rightPanel)


        return dialogPanel
    }

    private companion object{
        const val ICON_SIZE = 25
    }
}