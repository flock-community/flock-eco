package community.flock.eco.feature.member.events

import community.flock.eco.core.events.Event
import community.flock.eco.feature.member.model.MemberGroup

sealed class MemberGroupEvent(open val group: MemberGroup) : Event

data class CreateMemberGroupEvent(override val group: MemberGroup) : MemberGroupEvent(group)

data class UpdateMemberGroupEvent(override val group: MemberGroup, val oldGroup: MemberGroup) : MemberGroupEvent(group)

data class DeleteMemberGroupEvent(override val group: MemberGroup) : MemberGroupEvent(group)
