package community.flock.eco.feature.payment.services

import community.flock.eco.feature.payment.model.PaymentBankAccount
import community.flock.eco.feature.payment.model.PaymentFrequency
import community.flock.eco.feature.payment.model.PaymentMandate
import community.flock.eco.feature.payment.model.PaymentType
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month
import java.util.*

@Component
class PaymentSepaService(
        private val paymentMandateRepository: PaymentMandateRepository
) {

    data class PaymentSepa(
            val code: String = UUID.randomUUID().toString(),

            val amount: Double,
            val frequency: PaymentFrequency,
            val collectionMonth: Month? = null,

            val bankAccount: PaymentBankAccount
    )

    data class SepaResult(
            val mandate: PaymentMandate
    )

    fun create(paymentSepa: PaymentSepa): SepaResult {

        val mandate = paymentSepa.toPaymentMandate()
                .let {
                    paymentMandateRepository.save(it)
                }

        return SepaResult(
                mandate = mandate
        )

    }

    private fun PaymentSepa.toPaymentMandate(): PaymentMandate {
        val now = LocalDate.now()
        return PaymentMandate(
                code = this.code,
                startDate = now,

                amount = this.amount,
                frequency = this.frequency,
                type = PaymentType.SEPA,

                collectionMonth = this.collectionMonth ?: now.month,

                bankAccount = this.bankAccount
        )
    }
}