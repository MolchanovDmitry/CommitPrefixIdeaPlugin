package main.kotlin.dmitriy.molchanov

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel


class SettingsDialogWrapper : DialogWrapper(true) {

    init {
        init()
        title = "Настройки плагина префикса коммитов"
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout(500, 500))

        val data = Array(2) { arrayOfNulls<String>(2) }
        val columnNames = arrayOf("Machine Name", "Host Name (port)")
        data[0][0] = "test"
        data[0][1] = "test2"
        data[1][0] = "123"
        data[1][1] = "asdkjn"

        val model = DefaultTableModel(data, columnNames)
        val table = JBTable(model)
        table.preferredScrollableViewportSize = Dimension(500, 300)
        table.fillsViewportHeight = true

        dialogPanel.add(table, BorderLayout.CENTER)


        return dialogPanel
    }
}