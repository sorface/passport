package by.devpav.kotlin.oidcidp.web.graphql.config

import by.devpav.kotlin.oidcidp.web.graphql.interpolators.GraphQLMessageInterpolator
import graphql.scalars.ExtendedScalars
import graphql.schema.idl.RuntimeWiring
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQlConfig {

    @Bean
    fun runtimeWiringConfigurer(messageInterpolator: GraphQLMessageInterpolator): RuntimeWiringConfigurer {
        val validationRules = ValidationRules.Builder()
            .messageInterpolator(messageInterpolator)
            .build()

        val validationSchemaWiring = ValidationSchemaWiring(validationRules)
        return RuntimeWiringConfigurer { builder: RuntimeWiring.Builder -> builder.directiveWiring(validationSchemaWiring)
            .scalar(ExtendedScalars.GraphQLLong)
            .scalar(ExtendedScalars.UUID)
        }
    }

}