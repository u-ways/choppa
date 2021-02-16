package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

internal fun random(candidates: List<List<Member>>, amount: Int): List<List<Member>> =
    if (candidates.flatten().count() < 1) candidates
    else MutableList<MutableList<Member>>(candidates.size) { mutableListOf() }.apply {
        candidates
            .mapIndexed { i, sc -> sc.map { i to it } }
            .flatten()
            .toMutableList()
            .let { indexedCandidates ->
                (0 until amount).map {
                    indexedCandidates[indexedCandidates.indices.random()].also {
                        indexedCandidates.remove(it)
                        this[it.first].add(it.second)
                    }
                }
            }
    }
