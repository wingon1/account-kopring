package com.sori.account.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration(
) {
    @Bean
    fun api(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("account")
            .addOpenApiCustomizer {
                openApiCustomizer(it)
            }
            .pathsToMatch("/")
            .displayName("Account API")
            .build()
    }

    @Bean
    fun modelResolver(mapper: ObjectMapper): ModelResolver {
        return ModelResolver(mapper.setPropertyNamingStrategy(SNAKE_CASE))
    }

    private fun openApiCustomizer(openApi: OpenAPI): OpenAPI {
        return openApi
            .components(
                openApi.components.addSecuritySchemes(
                    "bearer-token",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .info(Info().title("Account API").version("v1"))
    }
}
