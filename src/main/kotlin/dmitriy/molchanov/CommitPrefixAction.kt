package main.kotlin.dmitriy.molchanov

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import main.kotlin.dmitriy.molchanov.rule.settings.SettingsPresenter

class CommitPrefixAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        SettingsPresenter()
    }
}