package community.flock.eco.feature.multi_tenant

import community.flock.eco.feature.multi_tenant.MultiTenantConstants.DEFAULT_TENANT
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@Component
class MultiTenantConnectionProvider(
    private val dataSource: DataSource
) : org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider {

    @Throws(SQLException::class)
    override fun getAnyConnection(): Connection {
        return dataSource.getConnection()
    }

    @Throws(SQLException::class)
    override fun releaseAnyConnection(connection: Connection) {
        connection.close()
    }

    @Throws(SQLException::class)
    override fun getConnection(tenantIdentifier: String): Connection {
        val connection = anyConnection
        connection.schema = tenantIdentifier
        return connection
    }

    @Throws(SQLException::class)
    override fun releaseConnection(tenantIdentifier: String, connection: Connection) {
        connection.schema = DEFAULT_TENANT
        releaseAnyConnection(connection)
    }

    override fun supportsAggressiveRelease(): Boolean {
        return false
    }

    override fun isUnwrappableAs(unwrapType: Class<*>?): Boolean {
        return false
    }

    override fun <T> unwrap(unwrapType: Class<T>): T? {
        return null
    }
}
