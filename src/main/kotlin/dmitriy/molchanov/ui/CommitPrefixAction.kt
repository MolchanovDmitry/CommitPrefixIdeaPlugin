package main.kotlin.dmitriy.molchanov.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class CommitPrefixAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) = Presenter().showMain()

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.icon = AllIcons.General.Settings
    }
}