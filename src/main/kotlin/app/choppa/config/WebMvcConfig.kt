package app.choppa.config

import app.choppa.domain.account.AccountArgumentResolver
import app.choppa.domain.account.converter.OAuth2UserToAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    @Autowired private val oAuth2UserToAccountService: OAuth2UserToAccountService,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver?>) {
        argumentResolvers.add(AccountArgumentResolver(oAuth2UserToAccountService))
    }

}
