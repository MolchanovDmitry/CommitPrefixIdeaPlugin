package main.kotlin.dmitriy.molchanov.rule.settings

import main.kotlin.dmitriy.molchanov.rule.model.Rule
import main.kotlin.dmitriy.molchanov.rule.data.RuleRepository
import main.kotlin.dmitriy.molchanov.rule.AddRuleDialog

class SettingsPresenter : SettingsDialog.OnSettingsDialogListener {
    private val repository = RuleRepository.instance
    private val settingsDialog: SettingsDialog

    init {
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