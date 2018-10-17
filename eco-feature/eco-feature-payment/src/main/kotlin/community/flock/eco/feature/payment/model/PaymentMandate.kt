package community.flock.eco.feature.payment.model;

import java.time.LocalDate
import java.time.Month
import java.util.*
import javax.persistence.*

@Entity
data class PaymentMandate(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(unique = true)
    val code: String = UUID.randomUUID().toString(),

    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate? = null,

    val amount: Double,

    @Enumerated(EnumType.STRING)
    val frequency: PaymentFrequency,
    val type: PaymentType,
    val collectionMonth: Month? = null,

    @Embedded
    val bankAccount: PaymentBankAccount? = null

)
