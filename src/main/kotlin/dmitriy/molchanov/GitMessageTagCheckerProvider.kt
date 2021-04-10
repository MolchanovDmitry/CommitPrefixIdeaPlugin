package dmitriy.molchanov

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import dmitriy.molchanov.data.Repository
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

class GitMessageTagCheckerProvider : CommitMessageProvider {

    override fun getCommitMessage(localChangeList: LocalChangeList, project: Project): String? {
        val lastComment = localChangeList.comment
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        val currentRepository = gitRepositoryManager.repositories.firstOrNull()
        val branchName = currentRepository?.currentBranch?.name ?: return lastComment
        val regex = getRegexForRepository(currentRepository) ?: return lastComment
        val match = regex.find(branchName)
        return match?.value
            ?.let { taskPrefix -> getConcatenatedMessage(lastComment, taskPrefix, regex) }
            ?: lastComment
    }

    private fun getRegexForRepository(repository: GitRepository): Regex? {
        val remoteUrl = repository
            .info.remotes.firstOrNull()
            ?.firstUrl
            ?: return null
        return Repository.instance
            .getRules()
            .firstOrNull { it.gitRepo.trim() == remoteUrl.trim() }
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
}