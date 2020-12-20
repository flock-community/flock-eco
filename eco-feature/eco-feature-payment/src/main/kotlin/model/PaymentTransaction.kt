package community.flock.eco.feature.payment.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import community.flock.eco.core.events.EventEntityListeners
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(EventEntityListeners::class)
data class PaymentTransaction(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    val amount: Double,

    val reference: String = UUID.randomUUID().toString(),

    @Enumerated(EnumType.STRING)
    val status: PaymentTransactionStatus = PaymentTransactionStatus.PENDING,

    val confirmed: LocalDate? = null,
    val created: LocalDate = LocalDate.now(),

    @ManyToOne
    @JsonManagedReference
    val mandate: PaymentMandate

)
