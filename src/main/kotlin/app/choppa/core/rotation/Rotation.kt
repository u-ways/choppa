package app.choppa.core.rotation

import app.choppa.core.rotation.RotationOptions.Companion.DEFAULT_OPTIONS
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import kotlin.system.measureTimeMillis

class Rotation {
    companion object {
        fun rotate(tribe: Tribe, options: RotationOptions = DEFAULT_OPTIONS): Tribe {
            if (tribe.squads.count() < 2) return tribe

            return tribe.squads
                .map {
                    it.members.find(options.chapter)
                }.let {
                    val oldMembers =
                        if(it.flatten().count() <= options.amount)
                            options.filter.invoke(it.toList(), options.amount)
                        else
                            it
                    options.strategy.invoke(oldMembers.copy()).mapIndexed { index, newMembers ->
                        Pair(oldMembers[index], newMembers)
                    }
                }.let {
                    Tribe(
                        tribe.id,
                        tribe.name,
                        tribe.color,
                        it.mapIndexed { index, (oldMembers, newMembers) ->
                            Squad(
                                tribe.squads[index].id,
                                tribe.squads[index].name,
                                tribe.squads[index].color,
                                tribe.squads[index].tribe,
                                tribe.squads[index].members
                                    .minus(oldMembers)
                                    .plus(newMembers).toMutableList()
                            )
                        }.toMutableList(),
                        tribe.history.toMutableList()
                    )
                }
        }

        private fun List<MutableList<Member>>.copy(): List<MutableList<Member>> {
            return this.map { x -> x.toMutableList() }.toList()
        }

        private fun MutableList<Member>.find(chapter: Chapter): MutableList<Member> {
            return this.filter { it.chapter == chapter }.toMutableList()
        }
    }
}
