package main.kotlin.dmitriy.molchanov.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import main.kotlin.dmitriy.molchanov.ui.add.AddRuleDialog

class CommitPrefixAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) = AddRuleDialog().show()

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.icon = AllIcons.General.Settings
    }
}