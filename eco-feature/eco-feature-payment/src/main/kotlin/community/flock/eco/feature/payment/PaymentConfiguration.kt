package community.flock.eco.feature.payment

import com.flock.community.api.service.PaymentBuckarooService
import community.flock.eco.feature.payment.controllers.PaymentBuckarooController
import community.flock.eco.feature.payment.controllers.PaymentTransactionController
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(PaymentBuckarooService::class,
        PaymentTransactionController::class,
        PaymentBuckarooController::class)
class PaymentConfiguration