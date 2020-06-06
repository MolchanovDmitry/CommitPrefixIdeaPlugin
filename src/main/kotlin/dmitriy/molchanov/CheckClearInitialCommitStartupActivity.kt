package main.kotlin.dmitriy.molchanov

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vcs.VcsConfiguration
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import javax.swing.event.HyperlinkEvent

class CheckClearInitialCommitStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        val conf = VcsConfiguration.getInstance(project)
        if (conf.CLEAR_INITIAL_COMMIT_MESSAGE) {
            showDisableDialog(project)
        }
    }

    private fun showDisableDialog(project: Project) {
        val statusBar = WindowManager.getInstance().getStatusBar(project)

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(
                        "Для работы плагина CommitMessageRefactorer, необходимо выключить опцию \"Clear initial commit message\" <a href=\"enable\">Выключить</a>",
                        MessageType.WARNING
                ) {
                    if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        enableClearInitialCommitMessage(project)
                    }
                }
                .setHideOnLinkClick(true)
                .setFadeoutTime(7000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.atRight)
    }

    private fun enableClearInitialCommitMessage(project: Project) {
        val conf = VcsConfiguration.getInstance(project)
        conf.CLEAR_INITIAL_COMMIT_MESSAGE = false

        val statusBar = WindowManager.getInstance().getStatusBar(project)
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(
                        "Опция выключена.",
                        MessageType.INFO,
                        null
                )
                .setFadeoutTime(3000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.atLeft)
    }
}