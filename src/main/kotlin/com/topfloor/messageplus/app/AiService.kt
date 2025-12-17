package com.topfloor.messageplus.app

import OpenAiProps
import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import com.topfloor.messageplus.api.dto.AiDto
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AiService(
    private val props: OpenAiProps
) {
    fun get(): AiDto =
        runBlocking {

            val agent = AIAgent(
                promptExecutor = simpleOpenAIExecutor(props.apiKey),
                llmModel = OpenAIModels.Chat.GPT4o
            )

            val result = agent.run("Hello! How can you help me?")
            return@runBlocking AiDto(UUID.randomUUID(), result)
        }

}
