package main.kotlin.dmitriy.molchanov

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import git4idea.repo.GitRepositoryManager

class GitMessageTagCheckerProvider : CommitMessageProvider {

    override fun getCommitMessage(localChangeList: LocalChangeList, project: Project): String? {
        val lastComment = localChangeList.comment
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        val branchName = gitRepositoryManager.repositories.firstOrNull()?.currentBranch?.name ?: return lastComment
        val isMasterBranch = checkMasterBranch(branchName, project)
        val match = REGEX.find(branchName)
        return match?.value
                ?.let { taskPrefix -> getConcatenatedMessage(lastComment, taskPrefix) }
                ?: let {
                    if (!isMasterBranch) {
                        showMessage("Некорректное наименование ветки, не содержит $MSA_PREFIX-123", project)
                    }
                    lastComment
                }
    }

    private fun getConcatenatedMessage(lastComment: String?, taskPrefix: String): String {
        if (lastComment.isNullOrEmpty()) return "$taskPrefix "
        val currentPrefix = REGEX.find(lastComment)?.value
        return currentPrefix
                ?.let { lastComment.replace(currentPrefix, taskPrefix) }
                ?: "$taskPrefix $lastComment"
    }

    private fun checkMasterBranch(branchName: String, project: Project): Boolean {
        if (MASTER_BRANCHES.contains(branchName)) {
            showMessage("Ай яй яй коммитить в $branchName!", project)
            return true
        }
        return false
    }

    private fun showMessage(message: String, p: Project) {
        val statusBar = WindowManager.getInstance().getStatusBar(p)
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, MessageType.ERROR, null)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.atLeft)
    }

    private companion object {
        const val EMPTY = ""
        const val MSA_PREFIX = "MSA"
        val REGEX = Regex("$MSA_PREFIX-\\d{1,4}")
        val MASTER_BRANCHES = arrayOf("dev", "develop", "master")
    }
}