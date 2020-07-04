package main.kotlin.dmitriy.molchanov.ui

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import main.kotlin.dmitriy.molchanov.model.Rule

@State(name = "RuleServiceData", storages = [Storage("ruleServiceData.xml")])
object Repository : PersistentStateComponent<Repository.State> {

    val instance: Repository
        get() = ServiceManager.getService(Repository::class.java)

    private var state = State()

    fun addRule(rule: Rule) {
        removeRule(rule)
        state.rules.add(rule)
    }

    fun getRules() = state.rules

    fun removeRule(rule: Rule) {
        state.rules
                .firstOrNull { it.gitRepo == rule.gitRepo }
                ?.let(state.rules::remove)
    }

    fun removeRule(rules: List<Rule>) {
        rules.forEach(::removeRule)
    }

    override fun getState() = state

    override fun loadState(stateLoadedFromPersistence: State) {
        state = stateLoadedFromPersistence
    }

    data class State(var rules: MutableList<Rule> = ArrayList())
}