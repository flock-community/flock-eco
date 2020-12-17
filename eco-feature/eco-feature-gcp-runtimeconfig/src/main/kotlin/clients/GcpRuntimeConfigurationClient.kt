package community.flock.eco.feature.gcp.runtimeconfig.clients

import com.google.api.gax.core.CredentialsProvider
import com.google.auth.oauth2.UserCredentials
import community.flock.eco.feature.gcp.runtimeconfig.model.GcpRuntimeConfig
import community.flock.eco.feature.gcp.runtimeconfig.model.GcpRuntimeVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gcp.core.GcpProjectIdProvider
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Component
class GcpRuntimeConfigurationClient(
       private val idProvider: GcpProjectIdProvider,

       private val  credentialsProvider: CredentialsProvider
) {



    data class RuntimeConfigResponse(val configs: List<GcpRuntimeConfig>, val nextPageToken: String?)
    data class RuntimeVariableResponse(val variables: List<GcpRuntimeVariable>, val nextPageToken: String?)

    val baseUrl = "https://runtimeconfig.googleapis.com"
    val restTemplate = RestTemplate()

    init {
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory(baseUrl)
    }

    private fun getHeaders(): HttpHeaders {
        val credentials = credentialsProvider.credentials as UserCredentials
        val headers = credentials.requestMetadata
                .let { CollectionUtils.toMultiValueMap(it) }
                .let { HttpHeaders(it) }
        return headers
    }

    fun getConfigsList(): RuntimeConfigResponse {
        val url = "/v1beta1/projects/${idProvider.projectId}/configs"
        val entity = HttpEntity<Any>(this.getHeaders())
        val res = restTemplate.exchange(url, HttpMethod.GET, entity, RuntimeConfigResponse::class.java)
        return res.body
    }

    fun getVariablesList(config: String): RuntimeVariableResponse {
        val url = "/v1beta1/projects/${idProvider.projectId}/configs/$config/variables?returnValues=true"
        val entity = HttpEntity<Any>(this.getHeaders())
        val res = restTemplate.exchange(url, HttpMethod.GET, entity, RuntimeVariableResponse::class.java)
        return res.body
    }

}