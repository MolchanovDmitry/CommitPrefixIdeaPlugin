package main.kotlin.dmitriy.molchanov.rule.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import main.kotlin.dmitriy.molchanov.rule.model.Rule

@State(name = "RuleServiceData", storages = [Storage("ruleServiceData.xml")])
class RuleRepository : PersistentStateComponent<RuleRepository> {

    @Suppress("MemberVisibilityCanBePrivate")
    var serializedRules: String? = null

    fun addRule(rule: Rule) {
        removeRule(rule)
        rules.add(rule)
    }

    fun getRules(): List<Rule> = rules

    fun removeRule(rule: Rule) {
        rules
                .firstOrNull { it.gitRepo == rule.gitRepo }
                ?.let(rules::remove)
    }

    fun removeRule(rules: List<Rule>) {
        rules.forEach(::removeRule)
    }

    override fun getState(): RuleRepository {
        serializedRules = Serializer.serialize(rules)
        return this
    }

    override fun loadState(stateLoadedFromPersistence: RuleRepository) {
        stateLoadedFromPersistence.serializedRules
                ?.let { Serializer.deserialize<Rule>(it) }
                ?.let { rules = it }
    }

    companion object {
        var rules: ArrayList<Rule> = ArrayList()
        val instance: RuleRepository
            get() = ServiceManager.getService(RuleRepository::class.java)
    }
}