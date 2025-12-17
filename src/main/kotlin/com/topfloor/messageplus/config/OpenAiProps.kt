import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.openai")
data class OpenAiProps(
    @field:NotBlank val apiKey: String,
    //val model: String = "gpt-4o",
)
