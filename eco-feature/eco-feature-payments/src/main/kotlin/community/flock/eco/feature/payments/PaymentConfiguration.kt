package community.flock.eco.feature.payments

import community.flock.eco.feature.payments.controllers.PaymentBuckarooController
import community.flock.eco.feature.payments.controllers.PaymentTransactionController
import community.flock.eco.feature.payments.services.PaymentBuckarooService
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