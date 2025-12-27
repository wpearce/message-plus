package com.topfloor.messageplus.app

import OpenAiProps
import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.openai.OpenAIModels.Chat.GPT4o
import ai.koog.prompt.executor.clients.openai.OpenAIModels.Chat.GPT5
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import ai.koog.prompt.llm.LLModel
import com.topfloor.messageplus.api.dto.AiDto
import com.topfloor.messageplus.api.dto.AiInputDto
import com.topfloor.messageplus.api.dto.LLM
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AiService(
    private val props: OpenAiProps
) {
    fun improve(input: AiInputDto): AiDto =
        runBlocking {

            val agent = AIAgent(
                promptExecutor = simpleOpenAIExecutor(props.apiKey),
                llmModel = getModel(input.model),
                temperature = 1.0,
                systemPrompt = "Fix spelling and grammar errors, while keeping the general structure of the input. Always return only the improved input. If you find nothing to improve, return the input without changes",
            )

            val result = agent.run(input.prompt)
            return@runBlocking AiDto(UUID.randomUUID(), result)
        }

    fun translate(input: AiInputDto): AiDto =
        runBlocking {

            val agent = AIAgent(
                promptExecutor = simpleOpenAIExecutor(props.apiKey),
                llmModel = getModel(input.model),
                temperature = 1.0,
                systemPrompt = "If the message is in english, translate it to portuguese. If it is in portuguese, translate it to english. If there are spelling or grammar errors, don't reproduce them in the translated string. Always return only the translated string",
            )

            val result = agent.run(input.prompt)
            return@runBlocking AiDto(UUID.randomUUID(), result)
        }

    fun getModel(llm: LLM?): LLModel {
        return when (llm) {
            LLM.GPT4o -> GPT4o
            LLM.GPT5 -> GPT5
            else -> {
                GPT4o
            }
        }
    }
}
