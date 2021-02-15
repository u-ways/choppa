package app.choppa.domain.account

import app.choppa.domain.account.converter.OAuth2UserToAccountService
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AccountArgumentResolver(
    private val oAuth2UserToAccountService: OAuth2UserToAccountService,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType.isAssignableFrom(Account::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Account {
        return oAuth2UserToAccountService.convert(
            (SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId,
            SecurityContextHolder.getContext().authentication.principal as OAuth2User
        )
    }

}
