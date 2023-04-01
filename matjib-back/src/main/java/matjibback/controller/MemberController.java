package matjibback.controller;

import io.jsonwebtoken.Claims;
import matjibback.entity.Member;
import matjibback.naverLogin.NaverLoginService;
import matjibback.naverLogin.NaverUserInfo;
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
import java.util.Optional;

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

    @PostMapping("matjib/member/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        NaverUserInfo response = naverLoginService.getUserNaverProfile(params.get("tempToken"), params.get("callbackState"));
        String naverID = response.getId();
        String nickname1 = response.getNickname();
        System.out.println("별명 " + nickname1);
        Member member = memberRepository.findMembersByToken(naverID);

        if (member == null) {
            Member newMember = new Member();
            newMember.setToken(response.getId());
            newMember.setNickname(response.getNickname());
            newMember.setEmail(response.getEmail());
            newMember.setName(response.getName());
            newMember.setBirthday(response.getBirthday());
            newMember.setBirthyear(Integer.parseInt(response.getBirthyear()));

            memberRepository.save(newMember);
            member = newMember;
        }

        String nickname = member.getNickname();
        String loginToken = jwtService.getToken("id", member.getId());

        Cookie cookie = new Cookie("token", loginToken);
        cookie.setPath("/");

        res.addCookie(cookie);

        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }

    @PostMapping("/matjib/member/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/matjib/member/edit")
    public ResponseEntity edit(@RequestParam("token") String token,
                               @RequestBody Map<String, String> params) {
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(token);
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = result.get(true);
        String nickname = params.get("nickname");
//        if (nickname.equals("")){
//            member.setNickname(nicknameN);
//        }else{
        member.setNickname(nickname);
//        }
        memberRepository.save(member);
        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/matjib/member/delete")
    public ResponseEntity delete(@RequestParam("token") String token) {
        Map<Boolean, Member> result = memberService.validateTokenAndGetMemberById(token);
        if (result.containsKey(false)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (result.get(true) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = result.get(true);
        memberRepository.deleteMemberById(member.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
