package community.flock.eco.feature.payment.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class PaymentTransaction(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    val amount: Double,

    val reference: String,
    val nonce: String,

    val status: PaymentTransactionStatus,

    val confirmed: Date? = null,
    val created: Date = Date()

)
