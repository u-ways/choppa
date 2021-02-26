package app.choppa.acceptance.domain.account

import app.choppa.domain.account.Account
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountSerializerTest {
    private lateinit var account: Account
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        account = Account()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(account))

        val id = uniformDto?.read<String>("$.id")
        val provider = uniformDto?.read<String>("$.provider")
        val providerId = uniformDto?.read<String>("$.providerId")
        val organisationName = uniformDto?.read<String>("$.organisationName")
        val name = uniformDto?.read<String>("$.name")
        val profilePicture = uniformDto?.read<String>("$.profilePicture")
        val firstLogin = uniformDto?.read<String>("$.firstLogin")

        assertThat(id, equalTo(account.id.toString()))
        assertThat(provider, equalTo(account.provider))
        assertThat(providerId, equalTo(account.providerId))
        assertThat(organisationName, equalTo(account.organisationName))
        assertThat(name, equalTo(account.name))
        assertThat(profilePicture, equalTo(account.profilePicture))
        assertThat(firstLogin, equalTo(account.firstLogin.toString()))
    }
}
