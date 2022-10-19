package dmitriy.molchanov.domain

import com.intellij.openapi.project.ProjectManager
import dmitriy.molchanov.data.Repository
import dmitriy.molchanov.model.Rule
import dmitriy.molchanov.ui.add.AddRuleDialog
import dmitriy.molchanov.ui.main.SettingsDialog
import git4idea.repo.GitRepositoryManager

class Presenter : SettingsDialog.OnSettingsDialogListener {

    private val repository = Repository.instance
    private val repositoryManagers = ProjectManager.getInstance().openProjects
        .map(GitRepositoryManager::getInstance)
    private val settingsDialog by lazy {
        SettingsDialog(this).apply {
            addRules(repository.getRules())
        }
    }

    fun showMain() {
        settingsDialog.show()
    }

    override fun onAddClick() {
        showAddRuleDialog { newRule ->
            repository.addRule(newRule)
            settingsDialog.addRule(newRule)
        }
    }

    override fun onRemoveClick() {
        val selectedReps = settingsDialog.getSelectedReps()
        repository.getRules()
            .filter { rule -> selectedReps.contains(rule.gitRepo) }
            .forEach(repository::removeRule)
        updateSettingsDialogTable()
    }

    override fun onEditClick() {
        val selectedRep = settingsDialog.getSelectedReps().firstOrNull() ?: return
        val rule = repository.getRules().firstOrNull { it.gitRepo == selectedRep } ?: return
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
                .forEach(gitRepUrls::add)
        }
        return gitRepUrls
    }
}
