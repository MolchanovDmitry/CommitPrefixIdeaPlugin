package main.kotlin.dmitriy.molchanov.ui

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import main.kotlin.dmitriy.molchanov.Serializer
import main.kotlin.dmitriy.molchanov.model.Rule

@State(name = "RuleServiceData10", storages = [Storage("ruleServiceData10.xml")])
class Repository : PersistentStateComponent<Repository> {

    private var serializedRules: String? = null

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

    override fun getState(): Repository {
        serializedRules = Serializer.serialize(rules)
        return this
    }

    override fun loadState(stateLoadedFromPersistence: Repository) {
        XmlSerializerUtil.copyBean(stateLoadedFromPersistence, this)
        rules = Serializer.deserialize(serializedRules!!)!!
    }

    companion object {
        var rules: ArrayList<Rule> = ArrayList()
        val instance: Repository
            get() = ServiceManager.getService(Repository::class.java)
    }
}