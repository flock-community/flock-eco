package community.flock.eco.feature.member.events

import community.flock.eco.core.events.Event
import community.flock.eco.feature.member.model.MemberField

sealed class MemberFieldEvent(open val field: MemberField) : Event
data class CreateMemberFieldEvent(override val field: MemberField) : MemberFieldEvent(field)
data class UpdateMemberFieldEvent(override val field: MemberField, val oldField: MemberField) : MemberFieldEvent(field)
data class DeleteMemberFieldEvent(override val field: MemberField) : MemberFieldEvent(field)
