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
        val addRuleDialog = AddRuleDialog()
        if (addRuleDialog.showAndGet()) {
            val repo = addRuleDialog.gitRepo
            val regexPrefix = addRuleDialog.regexPrefix
            Prefix(repo, regexPrefix)
                    .also(repository::addPrefix)
                    .also(settingsDialog::addPrefix)
        } else {
            println("cancel pressed")
        }
    }

    override fun onRemoveClick() {
        TODO("Not yet implemented")
    }
}