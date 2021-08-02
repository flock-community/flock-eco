package community.flock.eco.feature.payment

import community.flock.eco.feature.payment.controllers.PaymentBuckarooController
import community.flock.eco.feature.payment.controllers.PaymentMandateController
import community.flock.eco.feature.payment.controllers.PaymentTransactionController
import community.flock.eco.feature.payment.services.PaymentBuckarooService
import community.flock.eco.feature.payment.services.PaymentSepaService
import community.flock.eco.feature.payment.services.PaymentSepaXmlService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(
    PaymentBuckarooService::class,
    PaymentSepaService::class,
    PaymentSepaXmlService::class,
    PaymentMandateController::class,
    PaymentTransactionController::class,
    PaymentBuckarooController::class
)
class PaymentConfiguration
