package community.flock.eco.feature.payment.model

import com.fasterxml.jackson.annotation.JsonBackReference
import community.flock.eco.core.events.EventEntityListeners
import java.time.LocalDate
import java.time.Month
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(EventEntityListeners::class)
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

    @Enumerated(EnumType.STRING)
    val type: PaymentType,

    @Enumerated(EnumType.STRING)
    val collectionMonth: Month? = null,

    @Embedded
    val bankAccount: PaymentBankAccount? = null,

    @OneToMany(mappedBy = "mandate")
    @JsonBackReference
    val transactions: Set<PaymentTransaction> = setOf()

) {

    constructor(int: Int, type: PaymentType = PaymentType.IDEAL) : this(
        amount = when (int % 4) {
            0 -> 2.0
            1 -> 4.0
            2 -> 8.0
            3 -> 16.0
            else -> 0.0
        },
        type = type,
        frequency = PaymentFrequency.ONCE
    )

    override fun equals(other: Any?): Boolean {
        return if (this === other) true
        else if (other == null || javaClass != other.javaClass) false
        else this.id == (other as PaymentMandate).id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}
