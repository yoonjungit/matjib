package matjibback.service;

import matjibback.entity.Member;

import java.util.Map;

public interface MemberService {
    Map<Boolean, Member> validateTokenAndGetMemberById(String token);
}

