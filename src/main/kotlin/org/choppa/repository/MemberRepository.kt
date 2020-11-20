package org.choppa.repository

import org.choppa.model.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<Member, UUID>
