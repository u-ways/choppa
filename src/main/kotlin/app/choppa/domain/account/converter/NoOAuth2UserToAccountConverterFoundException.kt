package app.choppa.domain.account.converter

class NoOAuth2UserToAccountConverterFoundException(provider: String?) : RuntimeException("No OAuth2UserToAccountConverter found for $provider")
