package community.flock.eco.feature.payment

import community.flock.eco.core.services.EventService
import community.flock.eco.feature.payment.controllers.PaymentBuckarooController
import community.flock.eco.feature.payment.controllers.PaymentTransactionController
import community.flock.eco.feature.payment.services.PaymentBuckarooService
import community.flock.eco.feature.payment.services.PaymentSepaService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(PaymentBuckarooService::class,
        PaymentSepaService::class,
        PaymentTransactionController::class,
        PaymentBuckarooController::class,
        EventService::class)
class PaymentConfiguration
