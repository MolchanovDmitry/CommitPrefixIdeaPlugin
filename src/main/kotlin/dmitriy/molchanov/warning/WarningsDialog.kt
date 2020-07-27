package main.kotlin.dmitriy.molchanov.warning

import com.intellij.openapi.ui.DialogWrapper
import main.kotlin.dmitriy.molchanov.utils.BoxLayoutUtils
import main.kotlin.dmitriy.molchanov.utils.GuiUtils
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField

class WarningsDialog(warningsMap: Map<String, String>) : DialogWrapper(false) {

    private val messageEdit = JTextField(TEXT_COLUMNS)
    private val branchesEdit = JTextField(TEXT_COLUMNS)

    init {
        init()
        if (warningsMap.isEmpty()) {
            branchesEdit.text = "master, main, develop, dev"
            messageEdit.text = "Нельзя комитить в ветку<%branch_name%>"
        }
    }

    override fun createCenterPanel(): JComponent? {
        val root = BoxLayoutUtils.createVerticalPanel()
        val branchesLabel = JLabel(BRANCHES)
        val messageLabel = JLabel(MESSAGE)
        val getRepGroup = GuiUtils.getViewGroup(branchesLabel, messageEdit, keyListener)
    }

    private companion object {
        const val BRANCHES = "Branches"
        const val MESSAGE = "Message"
        const val TEXT_COLUMNS = 15
    }
}