package matjibback.service;

import io.jsonwebtoken.Claims;
import matjibback.entity.Member;
import matjibback.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
    @Autowired
    JwtService jwtService;

    @Autowired
    MemberRepository memberRepository;

    @Override
    public Map<Boolean, Member> validateTokenAndGetMemberById(String token) {
        Map<Boolean, Member> result = new HashMap<>();
        Claims claims = jwtService.getClaims(token);
        if (claims == null) {
            result.put(false, null);
            return result;
        }
        int id = Integer.parseInt(claims.get("id").toString());
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findMembersById(id));
        if (!memberOptional.isPresent()) {
            result.put(true, null);
            return result;
        }
        Member member = memberOptional.get();
        result.put(true, member);
        return result;
    }
}
