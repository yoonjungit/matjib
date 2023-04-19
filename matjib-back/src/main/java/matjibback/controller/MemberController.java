package matjibback.controller;

import matjibback.entity.Member;
import matjibback.naverLogin.NaverLoginService;
import matjibback.naverLogin.NaverUserInfo;
import matjibback.repository.BookmarkRepository;
import matjibback.repository.MemberRepository;
import matjibback.service.JwtService;
import matjibback.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Map;

@RestController
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    NaverLoginService naverLoginService;
    @Autowired
    JwtService jwtService;
    @Autowired
    MemberService memberService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @PostMapping("matjib/member/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        NaverUserInfo response = naverLoginService.getUserNaverProfile(params.get("tempToken"), params.get("callbackState"));
        String nToken = response.getNToken();
        Member member = memberRepository.findMembersByToken(nToken);

        if (member == null) {
            Member newMember = new Member();
            newMember.setToken(nToken);
            newMember.setNickname(response.getNickname());
            newMember.setEmail(response.getEmail());
            memberRepository.save(newMember);
            member = newMember;
        }

        String nickname = member.getNickname();
        String loginToken = jwtService.getToken("id", member.getId());

        Cookie cookie = new Cookie("token", loginToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        res.addCookie(cookie);
        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }

    @GetMapping("/matjib/member/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/matjib/member/edit")
    public ResponseEntity edit(@CookieValue(value = "token", required = false)String token,
                               @RequestBody Map<String, String> params) {
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(token);
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = result.get(true);
        String nickname = params.get("nickname");
        member.setNickname(nickname);

        memberRepository.save(member);
        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/matjib/member/delete")
    public ResponseEntity delete(@CookieValue(value = "token", required = false)String token) {
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(token);
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = result.get(true);
        memberRepository.deleteMemberById(member.getId());
        bookmarkRepository.deleteBookmarkByMemId(member.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}