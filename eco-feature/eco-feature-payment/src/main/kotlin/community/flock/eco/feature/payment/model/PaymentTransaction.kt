package community.flock.eco.feature.payment.model

import java.util.*
import javax.persistence.*

@Entity
data class PaymentTransaction(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    val amount: Double,

    val reference: String,

    @Enumerated(EnumType.STRING)
    val status: PaymentTransactionStatus,

    val confirmed: Date? = null,
    val created: Date = Date(),

    @ManyToOne
    val mandate: PaymentMandate

)
