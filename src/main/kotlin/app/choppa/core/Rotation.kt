package app.choppa.core

import app.choppa.core.Options.Companion.DEFAULT_OPTIONS
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe

class Rotation {
    companion object {
        fun rotate(tribe: Tribe, options: Options = DEFAULT_OPTIONS): Tribe {
            if (tribe.squads.count() < 2) return tribe
            val oldMembers = mutableListOf<List<Member>>()

            return tribe.squads
                .map { squad ->
                    squad.members
                        .find(options.chapter)
                        .run { options.filter.invoke(this, options.amount) }
                        .apply { oldMembers.add(this) }
                }.run {
                    options.strategy.invoke(this)
                }.let {
                    Tribe(
                        tribe.id,
                        tribe.name,
                        tribe.color,
                        it.mapIndexed { index, newMembers ->
                            Squad(
                                tribe.squads[index].id,
                                tribe.squads[index].name,
                                tribe.squads[index].color,
                                tribe.squads[index].tribe,
                                tribe.squads[index].members
                                    .minus(oldMembers[index])
                                    .plus(newMembers).toMutableList()
                            )
                        }.toMutableList(),
                        tribe.history
                    )
                }
        }

        private fun MutableList<Member>.find(chapter: Chapter): MutableList<Member> {
            return this.filter { it.chapter == chapter }.toMutableList()
        }
    }
}
