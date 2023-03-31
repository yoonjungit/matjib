package matjibback.controller;

import io.jsonwebtoken.Claims;
import matjibback.entity.Member;
import matjibback.naverLogin.NaverLoginService;
import matjibback.naverLogin.NaverUserInfo;
import matjibback.repository.MemberRepository;
import matjibback.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MemberController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NaverLoginService naverLoginService;
    @Autowired
    JwtService jwtService;

    @PostMapping("matjib/member/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res){
        NaverUserInfo response = naverLoginService.getUserNaverProfile(params.get("tempToken"), params.get("callbackState"));
        String naverID = response.getId();
        Member member = memberRepository.findMembersByToken(naverID);

        if (member == null){
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
    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false)String token){
        Claims claims = jwtService.getClaims(token);

        if (claims != null){
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);

    }
}
