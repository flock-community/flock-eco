package community.flock.eco.feature.member.model

import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*
import javax.persistence.*

@Entity
data class MemberField(

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true)
        val name: String,
        val label: String,
        val type: MemberFieldType,

        val required: Boolean = true,
        val disabled: Boolean = false,

        @ElementCollection
        @OrderBy
        val options: SortedSet<String> = sortedSetOf(),

        val default: String? = null

)
