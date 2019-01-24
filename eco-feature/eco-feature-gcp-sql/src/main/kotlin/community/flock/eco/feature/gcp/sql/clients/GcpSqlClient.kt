package community.flock.eco.feature.gcp.sql.clients

import com.google.api.gax.core.CredentialsProvider
import com.google.auth.oauth2.UserCredentials
import community.flock.eco.feature.gcp.sql.model.GcpSqlInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gcp.core.GcpProjectIdProvider
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Component
class GcpSqlClient {

    @Autowired
    lateinit var idProvider: GcpProjectIdProvider

    @Autowired
    lateinit var credentialsProvider: CredentialsProvider

    data class SqlInstancesResponse(val items: List<GcpSqlInstance>?, val kind: String?)

    val baseUrl = "https://www.googleapis.com"
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

    fun getInstaceList(): SqlInstancesResponse {
        val url = "/sql/v1beta4/projects/${idProvider.projectId}/instances"
        val entity = HttpEntity<Any>(this.getHeaders())
        try {
            val res = restTemplate.exchange(url, HttpMethod.GET, entity, SqlInstancesResponse::class.java)
            return res.body
        }catch (ex: Exception){
            throw ex
        }
    }


}