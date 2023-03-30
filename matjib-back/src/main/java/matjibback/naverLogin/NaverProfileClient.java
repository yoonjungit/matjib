package matjibback.naverLogin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "naverProfile", url = "https://openapi.naver.com/",
        configuration = {NaverLoginConfig.class})

public interface NaverProfileClient {
    @GetMapping("v1/nid/me")
    ResponseEntity<String> getNaverProfile(@RequestHeader Map<String, String> header);
}
