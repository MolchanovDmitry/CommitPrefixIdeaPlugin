package main.kotlin.dmitriy.molchanov.domain

import com.intellij.openapi.project.ProjectManager
import git4idea.repo.GitRepositoryManager
import main.kotlin.dmitriy.molchanov.data.Repository
import main.kotlin.dmitriy.molchanov.model.Rule
import main.kotlin.dmitriy.molchanov.ui.add.AddRuleDialog
import main.kotlin.dmitriy.molchanov.ui.main.SettingsDialog


class Presenter : SettingsDialog.OnSettingsDialogListener {

    private val repository = Repository.instance
    private val repositoryManagers = ProjectManager.getInstance().openProjects
            .map(GitRepositoryManager::getInstance)
    private lateinit var settingsDialog: SettingsDialog

    fun showMain() {
        val rules = repository.getRules()
        settingsDialog = SettingsDialog(this)
        settingsDialog.addRules(rules)
        settingsDialog.show()
    }

    override fun onAddClick() {
        showAddRuleDialog { newRule ->
            repository.addRule(newRule)
            settingsDialog.addRule(newRule)
        }
    }

    override fun onRemoveClick() {
        val rules = settingsDialog.getSelectedRules()
        repository.removeRule(rules)
        updateSettingsDialogTable()
    }

    override fun onEditClick() {
        val rule = settingsDialog.getSelectedRules().firstOrNull() ?: return
        showAddRuleDialog(rule) { newRule ->
            repository.removeRule(rule)
            repository.addRule(newRule)
            updateSettingsDialogTable()
        }
    }

    private inline fun showAddRuleDialog(editableRule: Rule? = null, onSuccess: (Rule) -> Unit) {
        val repUrls = getGitRepUrls()
        val addRuleDialog = AddRuleDialog(editableRule, repUrls, ::getCurBranchByUrl)
        if (addRuleDialog.showAndGet()) {
            addRuleDialog.rule?.let(onSuccess)
        }
    }

    private fun getCurBranchByUrl(url: String): String? {
        var currentBranchName: String? = null
        repositoryManagers.forEach { gitRepositoryManager ->
            gitRepositoryManager.repositories
                    .firstOrNull { gitRep -> gitRep.info.remotes.firstOrNull()?.firstUrl == url }
                    ?.currentBranch
                    ?.let { currentBranchName = it.name }
        }
        return currentBranchName
    }

    private fun updateSettingsDialogTable() {
        val rules = repository.getRules()
        settingsDialog.clearRules()
        settingsDialog.addRules(rules)
    }

    private fun getGitRepUrls(): List<String> {
        val gitRepUrls = mutableListOf<String>()
        repositoryManagers.forEach { gitRepManager ->
            gitRepManager.repositories
                    .mapNotNull { gitRep -> gitRep.info.remotes.firstOrNull()?.firstUrl }
                    .forEach { url -> gitRepUrls.add(url) }
        }
        return gitRepUrls
    }
}