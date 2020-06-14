package main.kotlin.dmitriy.molchanov.ui

import main.kotlin.dmitriy.molchanov.model.Prefix
import main.kotlin.dmitriy.molchanov.ui.add.AddRuleDialog
import main.kotlin.dmitriy.molchanov.ui.main.SettingsDialog

class Presenter : SettingsDialog.OnSettingsDialogListener {
    private val repository = Repository.instance
    private lateinit var settingsDialog: SettingsDialog

    fun showMain() {
        val prefixes = repository.getPrefixes()
        settingsDialog = SettingsDialog(this)
        settingsDialog.addPrefixes(prefixes)
        settingsDialog.show()
    }

    override fun onAddClick() {
        showAddRuleDialog { newPrefix ->
            repository.addPrefix(newPrefix)
            settingsDialog.addPrefix(newPrefix)
        }
    }

    override fun onRemoveClick() {
        val prefixes = settingsDialog.getSelectedPrefixes()
        repository.removePrefixes(prefixes)
        updateSettingsDialogTable()
    }

    override fun onEditClick() {
        val prefix = settingsDialog.getSelectedPrefixes().firstOrNull() ?: return
        showAddRuleDialog(prefix) { newPrefix ->
            repository.removePrefix(prefix)
            repository.addPrefix(newPrefix)
            updateSettingsDialogTable()
        }
    }

    private inline fun showAddRuleDialog(editablePrefix: Prefix? = null, onSuccess: (Prefix) -> Unit) {
        val addRuleDialog = AddRuleDialog(editablePrefix)
        if (addRuleDialog.showAndGet()) {
            val newPrefix = addRuleDialog.getPrefix()
            onSuccess(newPrefix)
        }
    }

    private fun updateSettingsDialogTable() {
        val prefixes = repository.getPrefixes()
        settingsDialog.clearPrefixes()
        settingsDialog.addPrefixes(prefixes)
    }

    private fun AddRuleDialog.getPrefix() = Prefix(gitRepo, regexPrefix)
}