package main.kotlin.dmitriy.molchanov.ui


import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import main.kotlin.dmitriy.molchanov.model.Prefix
import java.util.concurrent.CopyOnWriteArrayList

@State(name = "PrefixServiceData", storages = [Storage("prefixServiceData.xml")])
object Repository : PersistentStateComponent<Repository.State> {

    val instance: Repository
        get() = ServiceManager.getService(Repository::class.java)

    fun addPrefix(prefix: Prefix) {
        state.prefixes.add(prefix)
    }

    fun getPrefixes() = state.prefixes

    override fun toString(): String {
        return with(state.prefixes) {
            "size=$size" + "\n${joinToString(separator = "\n")}"
        }
    }


    override fun getState() = state

    override fun loadState(stateLoadedFromPersistence: State) {
        state = stateLoadedFromPersistence
    }

    private var state = State()

    data class State(var prefixes: MutableList<Prefix> = CopyOnWriteArrayList())
}