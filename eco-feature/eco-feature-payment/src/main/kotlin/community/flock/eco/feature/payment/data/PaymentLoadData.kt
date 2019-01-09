package community.flock.eco.feature.payment.data

import community.flock.eco.core.data.LoadData
import community.flock.eco.feature.payment.model.*
import community.flock.eco.feature.payment.repositories.PaymentMandateRepository
import community.flock.eco.feature.payment.repositories.PaymentTransactionRepository
import org.springframework.stereotype.Component
import java.time.Month

@Component
class PaymentLoadData(
        private val paymentMandateRepository: PaymentMandateRepository,
        private val paymentTransactionRepository: PaymentTransactionRepository
) : LoadData<PaymentMandate> {

    override fun load(n:Int): Iterable<PaymentMandate> {
        return listOf<PaymentMandate>()
                .plus(loadMandateIdeal(n))
                .plus(loadMandateCreditCard(n))
                .plus(loadMandateSepa(n))


    }

    private fun loadMandateIdeal(n:Int = 100): Iterable<PaymentMandate> {
        val mandates = (0..n)
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

        mandates.map {
                    PaymentTransaction(
                            amount = 10.0,
                            mandate = it
                    )
                }
                .let {
                    paymentTransactionRepository.saveAll(it)
                }

        return mandates
    }

    private fun loadMandateCreditCard(n:Int = 100): Iterable<PaymentMandate> {
        return (0..n)
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

    private fun loadMandateSepa(n:Int = 100): Iterable<PaymentMandate> {
        return (0..n)
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