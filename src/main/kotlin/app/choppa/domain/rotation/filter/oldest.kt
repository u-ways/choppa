package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

internal fun oldest(candidates: List<List<Member>>, amount: Int): List<List<Member>> =
    if (candidates.flatten().size < 2) candidates
    else candidates.filter { it.isNotEmpty() }.run {
        List(candidates.size) { squad ->
            candidates[squad].take(amount / this.size)
        }.let { filtered ->
            if (amount % this.size != 0) {
                val candidate = (amount / this.size) + 1
                val squad = candidates.indexOfFirst { it.size == candidate }
                if (squad != -1) {
                    filtered
                        .take(squad)
                        .plusElement(filtered[squad].plusElement(candidates[squad][candidate - 1]))
                        .plus(filtered.takeLast(filtered.size - squad - 1))
                } else filtered
            } else filtered
        }
    }
