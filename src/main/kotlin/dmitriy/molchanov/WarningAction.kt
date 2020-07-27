package main.kotlin.dmitriy.molchanov

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import main.kotlin.dmitriy.molchanov.warning.WarningPresenter

class WarningAction: AnAction()  {
    override fun actionPerformed(p0: AnActionEvent) {
        WarningPresenter()
    }
}