package main.kotlin.dmitriy.molchanov.ui

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import main.kotlin.dmitriy.molchanov.model.Prefix

@State(name = "PrefixServiceData", storages = [Storage("prefixServiceData.xml")])
object Repository : PersistentStateComponent<Repository.State> {

    val instance: Repository
        get() = ServiceManager.getService(Repository::class.java)

    private var state = State()

    fun addPrefix(prefix: Prefix) {
        state.prefixes[prefix.gitRepo] = prefix.regexPrefix
    }

    fun getPrefixes()  = state.prefixes.map { Prefix(it.key, it.value) }

    fun removePrefix(prefix: Prefix){
        state.prefixes.remove(prefix.gitRepo)
    }

    fun removePrefixes(prefixes: List<Prefix>) {
        prefixes.forEach (::removePrefix)
    }

    override fun getState() = state

    override fun loadState(stateLoadedFromPersistence: State) {
        state = stateLoadedFromPersistence
    }

    data class State(var prefixes: HashMap<String, String> = HashMap())
}