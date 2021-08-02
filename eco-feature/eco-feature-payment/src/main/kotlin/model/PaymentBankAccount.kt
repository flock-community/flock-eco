package community.flock.eco.feature.payment.model

import javax.persistence.Embeddable

@Embeddable
data class PaymentBankAccount(
    val name: String,
    val iban: String,
    val bic: String,
    val country: String
)
