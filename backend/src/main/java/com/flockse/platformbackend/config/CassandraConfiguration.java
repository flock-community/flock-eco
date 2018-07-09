package com.flockse.platformbackend.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;

@Configuration
public class CassandraConfiguration {

    @Bean
    @Lazy
    public CassandraTemplate cassandraTemplate(@Lazy Session session, CassandraConverter converter) {
        return new CassandraTemplate(session, converter);
    }

    @Bean
    @Lazy
    public Session session(CassandraConverter converter, Cluster cluster, CassandraProperties cassandraProperties) {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster);
        session.setConverter(converter);
        session.setKeyspaceName(cassandraProperties.getKeyspaceName());
        session.setSchemaAction(SchemaAction.NONE);
        return session.getObject();
    }

}
