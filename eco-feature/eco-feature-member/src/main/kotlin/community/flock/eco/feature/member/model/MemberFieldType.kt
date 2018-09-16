package community.flock.eco.feature.member.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


enum class MemberFieldType {
        TEXT,
        CHECKBOX,
        SINGLE_SELECT,
        MULTI_SELECT
}
