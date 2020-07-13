package main.kotlin.dmitriy.molchanov.domain

import main.kotlin.dmitriy.molchanov.model.Rule
import main.kotlin.dmitriy.molchanov.data.Repository
import main.kotlin.dmitriy.molchanov.ui.add.AddRuleDialog
import main.kotlin.dmitriy.molchanov.ui.main.SettingsDialog

class Presenter : SettingsDialog.OnSettingsDialogListener {
    private val repository = Repository.instance
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
        val addRuleDialog = AddRuleDialog(editableRule)
        if (addRuleDialog.showAndGet()) {
            onSuccess(addRuleDialog.rule)
        }
    }

    private fun updateSettingsDialogTable() {
        val rules = repository.getRules()
        settingsDialog.clearRules()
        settingsDialog.addRules(rules)
    }
}