package com.flockse.platformbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class PlatformBackendApplication {

//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    private class RetryableDataSourceBeanPostProcessor implements BeanPostProcessor {
//        @Override
//        public Object postProcessBeforeInitialization(Object bean, String beanName)
//                throws BeansException {
//            if (bean instanceof Session) {
//                bean = new RetryableDataSource((Session) bean);
//            }
//            return bean;
//        }
//
//        @Override
//        public Object postProcessAfterInitialization(Object bean, String beanName)
//                throws BeansException {
//            return bean;
//        }
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(PlatformBackendApplication.class, args);
    }

//    @Bean
//    public BeanPostProcessor dataSouceWrapper() {
//        return new RetryableDataSourceBeanPostProcessor();
//    }

}

//class RetryableDataSource extends AbstractDataSource {
//
//    private DataSource delegate;
//
//    public RetryableDataSource(DataSource delegate) {
//        this.delegate = delegate;
//    }
//
//    @Override
//    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 2.3, maxDelay = 30000))
//    public Connection getConnection() throws SQLException {
//        return delegate.getConnection();
//    }
//
//    @Override
//    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 2.3, maxDelay = 30000))
//    public Connection getConnection(String username, String password)
//            throws SQLException {
//        return delegate.getConnection(username, password);
//    }
//
//}
