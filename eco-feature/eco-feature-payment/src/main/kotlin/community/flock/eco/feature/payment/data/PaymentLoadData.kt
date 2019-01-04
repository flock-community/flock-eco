package community.flock.eco.feature.payment.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.payment.model.PaymentBankAccount
import community.flock.eco.feature.payment.model.PaymentFrequency
import community.flock.eco.feature.payment.model.PaymentMandate
import community.flock.eco.feature.payment.model.PaymentType
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import org.springframework.stereotype.Component
import java.time.Month

@Component
class PaymentLoadData(
        private val paymentMandateRepository: PaymentMandateRepository
) : LoadData<PaymentMandate> {

    override fun load(): Iterable<PaymentMandate> {
        return listOf<PaymentMandate>()
                .plus(loadMandateIdeal())
                .plus(loadMandateCreditCard())
                .plus(loadMandateSepa())


    }

    private fun loadMandateIdeal(): Iterable<PaymentMandate> {
        return (0..100)
                .map {
                    PaymentMandate(
                            amount = when(it % 4){
                                0 -> 2.0
                                1 -> 4.0
                                2 -> 8.0
                                3 -> 16.0
                                else -> 0.0
                            },
                            type = PaymentType.IDEAL,
                            frequency = PaymentFrequency.ONCE
                    )
                }
                .let {
                    paymentMandateRepository.saveAll(it)
                }
    }

    private fun loadMandateCreditCard(): Iterable<PaymentMandate> {
        return (0..100)
                .map {
                    PaymentMandate(
                            amount = when(it % 4){
                                0 -> 3.0
                                1 -> 9.0
                                2 -> 12.0
                                3 -> 15.0
                                else -> 0.0
                            },
                            type = PaymentType.CREDIT_CARD,
                            frequency = PaymentFrequency.ONCE
                    )
                }
                .let {
                    paymentMandateRepository.saveAll(it)
                }
    }

    private fun loadMandateSepa(): Iterable<PaymentMandate> {
        return (0..100)
                .map {
                    PaymentMandate(
                            amount = when(it % 4){
                                0 -> 3.0
                                1 -> 9.0
                                2 -> 12.0
                                3 -> 15.0
                                else -> 0.0
                            },
                            type = PaymentType.SEPA,
                            frequency = when(it % 4){
                                0 -> PaymentFrequency.MONTHLY
                                1 -> PaymentFrequency.QUARTERLY
                                2 -> PaymentFrequency.HALF_YEARLY
                                3 -> PaymentFrequency.YEARLY
                                else -> PaymentFrequency.YEARLY
                            },
                            bankAccount = PaymentBankAccount(
                                    name = "name-$it",
                                    bic = "BIC-$it",
                                    iban = "IBAN-$it",
                                    country = "NL"
                            ),
                            collectionMonth = Month.of(it % 12 + 1)
                    )
                }
                .let {
                    paymentMandateRepository.saveAll(it)
                }
    }
}