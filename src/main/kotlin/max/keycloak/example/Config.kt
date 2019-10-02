package max.keycloak.example

val apiKey: String by lazy { property("API_KEY") }
val apiSecret: String by lazy { property("API_SECRET") }
val callback: String by lazy { property("API_CALLBAK") }
val baseUrl: String by lazy { property("API_BASE_URL") }
val realm: String by lazy { property("API_REALM") }

private fun property(keyName: String): String {
    System.getProperty(keyName)?.let { return it }
    return System.getenv(keyName) ?: "null"
}
