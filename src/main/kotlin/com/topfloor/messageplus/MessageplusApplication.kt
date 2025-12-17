package com.topfloor.messageplus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(OpenAiProps::class)
@SpringBootApplication
class MessageplusApplication

fun main(args: Array<String>) {
	runApplication<MessageplusApplication>(*args)
}
