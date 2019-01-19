package community.flock.eco.feature.payment.model;

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
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
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        return this.id == (o as PaymentMandate).id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}
