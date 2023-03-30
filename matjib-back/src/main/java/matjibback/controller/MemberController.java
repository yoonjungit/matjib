package matjibback.controller;

import matjibback.entity.Member;
import matjibback.naverLogin.NaverLoginService;
import matjibback.naverLogin.NaverUserInfo;
import matjibback.repository.MemberRepository;
import matjibback.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
        String token = response.getId();
        int id ;
        Member member = memberRepository.findMembersByToken(token);

        if (member != null){
            id = member.getId();
        } else {
            Member newMember = new Member();
            newMember.setToken(response.getId());
            newMember.setNickname(response.getNickname());
            newMember.setEmail(response.getEmail());
            newMember.setName(response.getName());
            newMember.setBirthday(response.getBirthday());
            newMember.setBirthyear(Integer.parseInt(response.getBirthyear()));

            memberRepository.save(newMember);
            id = newMember.getId();
        }
        String loginToken = jwtService.getToken("id", id);

        Cookie cookie =new Cookie("token", loginToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        res.addCookie(cookie);
        return new ResponseEntity<>(id, HttpStatus.OK);

    }

    @PostMapping("/matjib/member/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
