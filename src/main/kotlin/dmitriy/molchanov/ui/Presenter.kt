package main.kotlin.dmitriy.molchanov.ui

import main.kotlin.dmitriy.molchanov.ui.add.AddRuleDialog
import main.kotlin.dmitriy.molchanov.ui.main.SettingsDialog

class Presenter : SettingsDialog.OnSettingsDialogListener {

    fun showMain() = SettingsDialog(this).show()

    override fun onAddClick() {
        if (AddRuleDialog().showAndGet()) {
            println("ok pressed")
        } else {
            println("cancel pressed")
        }
    }

    override fun onRemoveClick() {
        TODO("Not yet implemented")
    }
}