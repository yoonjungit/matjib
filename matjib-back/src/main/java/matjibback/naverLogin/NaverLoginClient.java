package matjibback.naverLogin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "naverLogin",
        url="https://nid.naver.com", configuration = {NaverLoginConfig.class})
public interface NaverLoginClient {
    @GetMapping("/oauth2.0/token")
    ResponseEntity<String> getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("state") String state,
            @RequestParam("code") String code
    );
}
