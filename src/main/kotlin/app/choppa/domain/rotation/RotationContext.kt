package app.choppa.domain.rotation

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe

class RotationContext {
    companion object {
        fun rotate(
            tribe: Tribe,
            options: RotationOptions,
            revisions: List<Pair<Squad, List<List<Member>?>>> = defaultRevisions(tribe)
        ): Tribe =
            if (tribe.squads.count() < 2) tribe
            else revisions.map {
                it.second.last()?.find(options.chapter) ?: emptyList()
            }.let {
                options.filter.invoke(it, options.amount).run {
                    this.zip(options.strategy.invoke(this.deepCopy()))
                }
            }.let {
                tribe.copy(
                    squads = it.mapIndexed { index, (oldMembers, newMembers) ->
                        tribe.squads[index].run {
                            copy(
                                members = members
                                    .minus(oldMembers)
                                    .plus(newMembers)
                                    .toMutableList(),
                            )
                        }
                    }.toMutableList(),
                )
            }

        private fun defaultRevisions(tribe: Tribe) = List<Pair<Squad, List<List<Member>?>>>(tribe.squads.size) {
            tribe.squads[it] to listOf(tribe.squads[it].members)
        }

        private fun List<List<Member>>.deepCopy(): List<List<Member>> {
            return this.map { it.toList() }.toList()
        }

        private fun List<Member>.find(chapter: Chapter): List<Member> {
            return this.filter { it.chapter == chapter }.toList()
        }
    }
}
