package dmitriy.molchanov

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import dmitriy.molchanov.data.Repository
import dmitriy.molchanov.model.Rule
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

class GitMessageTagCheckerProvider : CommitMessageProvider {

    override fun getCommitMessage(localChangeList: LocalChangeList, project: Project): String? {
        val lastComment = localChangeList.comment
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        val currentRepository = gitRepositoryManager.repositories.firstOrNull()
        val branchName = currentRepository?.currentBranch?.name ?: return lastComment
        val rule = getRuleForRepository(currentRepository)
        val regex = rule?.regexPrefix?.let(::Regex) ?: return lastComment
        val match = regex.find(branchName)
        return match?.value
            ?.let { taskPrefix ->
                getConcatenatedMessage(
                    lastComment = lastComment,
                    taskPrefix = taskPrefix,
                    startWith = rule.startWith,
                    endWith = rule.endWith,
                    isUpperCase = rule.isUpperCase
                )
            }
            ?: lastComment
    }

    private fun getRuleForRepository(repository: GitRepository): Rule? {
        val remoteUrl = repository
            .info.remotes.firstOrNull()
            ?.firstUrl
            ?: return null
        return Repository.instance
            .getRules()
            .firstOrNull { it.gitRepo.trim() == remoteUrl.trim() }
    }

    private fun getConcatenatedMessage(
        lastComment: String?,
        taskPrefix: String,
        startWith: String,
        endWith: String,
        isUpperCase: Boolean?,
    ): String {
        val registeredPrefix = when (isUpperCase) {
            false -> taskPrefix.lowercase()
            true -> taskPrefix.uppercase()
            else -> taskPrefix
        }
        val fullPrefix = "$startWith$registeredPrefix$endWith"
        return when {
            lastComment?.contains(fullPrefix) == true -> lastComment
            else -> fullPrefix
        }
    }
}
