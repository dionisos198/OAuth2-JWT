package com.OAuth2.oauth2Login.repository;


import com.OAuth2.oauth2Login.domain.Member;
import com.OAuth2.oauth2Login.domain.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByUserID(String userID);
    Optional<Member> findMemberByUserID(String userID);
    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}