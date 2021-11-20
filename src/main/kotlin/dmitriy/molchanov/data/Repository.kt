package dmitriy.molchanov.data

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import dmitriy.molchanov.model.Rule


@State(name = "RuleServiceData", storages = [Storage("ruleServiceData.xml")])
class Repository : PersistentStateComponent<Repository> {

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

    override fun getState(): Repository {
        serializedRules = Serializer.serialize(rules)
        return this
    }

    override fun loadState(stateLoadedFromPersistence: Repository) {
        stateLoadedFromPersistence.serializedRules
            ?.let { Serializer.deserialize<Rule>(it) }
            ?.let { rules = it }
    }

    companion object {
        var rules: ArrayList<Rule> = ArrayList()
        val instance: Repository
            get() = ApplicationManager.getApplication().getService(Repository::class.java)
    }
}