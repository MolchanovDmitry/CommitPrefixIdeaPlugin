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

    fun addPrefix(prefix: Prefix) {
        state.prefixes[prefix.gitRepo] = prefix.regexPrefix
    }

    fun getPrefixes() = state.prefixes.map { Prefix(it.key, it.value) }

    override fun getState() = state

    override fun loadState(stateLoadedFromPersistence: State) {
        state = stateLoadedFromPersistence
    }

    fun removePrefixes(prefixes: List<Prefix>) {
        prefixes.map { it.gitRepo }
                .forEach(state.prefixes::remove)
    }

    private var state = State()

    data class State(var prefixes: HashMap<String, String> = HashMap())
}