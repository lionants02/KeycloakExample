package max.keycloak.example.login

import com.github.scribejava.apis.KeycloakApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import max.keycloak.example.apiKey
import max.keycloak.example.apiSecret
import max.keycloak.example.baseUrl
import max.keycloak.example.callback
import max.keycloak.example.getLogger
import max.keycloak.example.realm

class LoginController {
    private val protectedResourceUrl = "$baseUrl/auth/realms/$realm/protocol/openid-connect/userinfo"
    private val service = ServiceBuilder(apiKey)
        .apiSecret(apiSecret)
        .defaultScope("openid")
        .callback(callback)
        .build(KeycloakApi.instance(baseUrl, realm))

    fun login(loginBody: LoginBody): String {
        logger.info { "=== Keyloack's OAuth Workflow ===" }

        // Obtain the Authorization URL
        logger.info { "Fetching the Authorization URL..." }

        val authorizationUrl = service.authorizationUrl
        logger.info { "Got the Authorization URL!" }
        logger.info { "Now go and authorize ScribeJava here:" }
        println(authorizationUrl)
        logger.info { "And paste the authorization code here" }
        print(">>")
        val accessToken = service.getAccessTokenPasswordGrantAsync(loginBody.username, loginBody.password).get()

        logger.info { "Got the Access Token!" };
        logger.info { "(The raw response looks like this: " + accessToken.rawResponse + "')" };

        // Now let's go and ask for a protected resource!
        logger.info { "Now we're going to access a protected resource..." };

        val request = OAuthRequest(Verb.GET, protectedResourceUrl)

        service.signRequest(accessToken, request)
        val response = service.execute(request)
        logger.info { "Got it! Lets see what we found..." }
        logger.info { response.code }
        logger.info { response.body }

        logger.info { "Thats it man! Go and build something awesome with ScribeJava! :)" }
        return accessToken.rawResponse
    }

    companion object {
        private val logger = getLogger()
    }
}
