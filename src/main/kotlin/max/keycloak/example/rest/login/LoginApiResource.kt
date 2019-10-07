package max.keycloak.example.rest.login

import max.keycloak.example.getLogger
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class LoginApiResource {

    @POST
    fun login(login: LoginBody): MessageResponse {
        logger.debug { }

        return createMessageResponse("d")
    }

    companion object {
        private val logger = getLogger()
    }
}

data class LoginBody(val username: String, val password: String)
data class MessageResponse(val message: String, val type: String)

inline fun <reified T> T.createMessageResponse(message: String): MessageResponse =
    MessageResponse(message, T::class.simpleName ?: "")
