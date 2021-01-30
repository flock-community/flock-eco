package community.flock.eco.feature.member.events

import community.flock.eco.core.events.Event
import community.flock.eco.feature.member.model.Member

sealed class MemberEvent(open val member: Member) : Event
data class CreateMemberEvent(override val member: Member) : MemberEvent(member)
data class UpdateMemberEvent(override val member: Member, val oldMember: Member) : MemberEvent(member)
data class DeleteMemberEvent(override val member: Member) : MemberEvent(member)
data class MergeMemberEvent(override val member: Member, val mergedMembers: Set<Member>) : MemberEvent(member)
