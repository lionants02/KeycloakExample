package max.keycloak.example.rest.login

import org.junit.Test

class LoginControllerTest {

    init {
        System.setProperty("API_KEY", "employee-service")
        System.setProperty("API_SECRET", "f00aded5-4731-4d58-a48d-071162912ab2")
        System.setProperty("API_CALLBAK", "https://auth.hii.web.meca.in.th/auth")
        System.setProperty("API_BASE_URL", "https://auth.hii.web.meca.in.th")
        System.setProperty("API_REALM", "demo")
    }

    @Test
    fun login() {
        val keycloak = LoginController()
        val login = LoginBody("", "")
        val token = keycloak.login(login)
        println(token)
    }
}
