package main.kotlin.dmitriy.molchanov

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import main.kotlin.dmitriy.molchanov.ui.Repository

class GitMessageTagCheckerProvider : CommitMessageProvider {

    override fun getCommitMessage(localChangeList: LocalChangeList, project: Project): String? {
        val lastComment = localChangeList.comment
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        val currentRepository = gitRepositoryManager.repositories.firstOrNull()
        val branchName = currentRepository?.currentBranch?.name ?: return lastComment
        val regex = getRegexForRepository(currentRepository) ?: return lastComment
        val isMasterBranch = checkMasterBranch(branchName, project)
        val match = regex.find(branchName)
        return match?.value
                ?.let { taskPrefix -> getConcatenatedMessage(lastComment, taskPrefix, regex) }
                ?: let {
                    if (!isMasterBranch) {
                        showMessage("Некорректное наименование ветки, не содержит $regex", project)
                    }
                    lastComment
                }
    }

    private fun getRegexForRepository(repository: GitRepository): Regex? {
        val remoteUrl = repository
                .info.remotes.firstOrNull()
                ?.firstUrl
                ?: return null
        return Repository.instance
                .getRules()
                .firstOrNull { it.gitRepo == remoteUrl }
                ?.regexPrefix
                ?.let(::Regex)
    }

    private fun getConcatenatedMessage(lastComment: String?, taskPrefix: String, regex: Regex): String {
        if (lastComment.isNullOrEmpty()) return "$taskPrefix "
        val currentPrefix = regex.find(lastComment)?.value
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
        val MASTER_BRANCHES = arrayOf("dev", "develop", "master")
    }
}