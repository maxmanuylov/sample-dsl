package com.example

import kotlin.script.dependencies.ScriptDependenciesResolver
import kotlin.script.templates.ScriptTemplateDefinition

@Suppress("unused")
@ScriptTemplateDefinition(resolver = NoDeps::class)
open class AgentConfig {
    val osName = System.getProperty("os.name") ?: ""

    fun agentFor(serverUrl: String, init: Agent.() -> Unit = {}) {
        val agent = Agent().apply(init)

        println("## Agent Configuration")
        println()
        println("serverUrl=$serverUrl")
        println("name=${agent.name}")
        println()

        printParams("Configuration Parameters", agent.configParams)
        printParams("System Parameters", agent.systemParams, "system.")
        printParams("Environment Parameters", agent.envParams, "env.")
    }

    private fun printParams(title: String, params: Map<String, String>, prefix: String = "") {
        if (params.isNotEmpty()) {
            println("## $title")
            println()

            for (param in params) {
                println("$prefix${param.key}=${param.value}")
            }

            println()
        }
    }

    fun Agent.config(init: Params.() -> Unit = {}) = params(configParams, init)
    fun Agent.system(init: Params.() -> Unit = {}) = params(systemParams, init)
    fun Agent.env(init: Params.() -> Unit = {}) = params(envParams, init)

    private fun params(params: MutableMap<String, String>, init: Params.() -> Unit) {
        Params(params).init()
    }

    /* ******************************************************************************** */

    @DslMarker
    private annotation class DslElement

    @DslElement
    class Agent {
        var name: String = "Default Agent"
        
        val configParams = mutableMapOf<String, String>()
        val systemParams = mutableMapOf<String, String>()
        val envParams = mutableMapOf<String, String>()
    }

    @DslElement
    class Params(private val params: MutableMap<String, String>) {
        operator fun String.unaryPlus() = ParamKey(this)

        infix fun ParamKey.to(value: String) {
            params[key] = value
        }
        
        class ParamKey(internal val key: String)
    }
}

/* ******************************************************************************** */

class NoDeps : ScriptDependenciesResolver
