package main.kotlin.dmitriy.molchanov.warning

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "WarningsServiceData", storages = [Storage("warningsServiceData.xml")])
class WarningsRepository : PersistentStateComponent<HashMap<String, String>> {

    private lateinit var warningsMap: HashMap<String, String>

    fun addWarning(branchName: String, message: String) {
        warningsMap[branchName] = message
    }

    override fun getState() = warningsMap

    override fun loadState(storedMap: HashMap<String, String>) {
        warningsMap = storedMap
    }
}