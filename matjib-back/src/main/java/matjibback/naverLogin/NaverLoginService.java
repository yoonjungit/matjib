package matjibback.naverLogin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NaverLoginService {
    @Autowired
    NaverLoginClient naverLoginClient;

    @Autowired
    NaverProfileClient naverProfileClient;

    @Value("${client_id}")
    private String client_id;

    @Value("${client_secret}")
    private String client_secret;

    public NaverUserInfo getUserNaverProfile (String tempToken, String state){
        String[] access = getAccessTokenAndType(tempToken, state);

        NaverUserInfo naverUserInfo = getNaverProfile(access);
        return naverUserInfo;
    }

    public String[] getAccessTokenAndType(String tempToken, String state){
        String clientId = client_id;
        String clientSecret = client_secret;
        String grantType = "authorization_code";

        ResponseEntity<String> accessTokenAndType = naverLoginClient.getAccessToken(
                clientId, clientSecret, grantType, state, tempToken
        );

        Gson gson1 = new Gson();
        NaverLoginResponse naverLoginResponse = gson1.fromJson(String.valueOf(accessTokenAndType.getBody()),NaverLoginResponse.class);

        String[] access = new String[]{naverLoginResponse.getAccess_token(), naverLoginResponse.getToken_type()};
        return access;
    }
    public NaverUserInfo getNaverProfile(String[] access){
        String accessToken = access[0];
        String accessTokenType = access[1];
        Map<String, String> header = new HashMap<>();

        header.put("Authorization", accessTokenType + " " + accessToken);
        ResponseEntity<String> response = naverProfileClient.getNaverProfile(header);

        Gson gson1 = new GsonBuilder()
                .setPrettyPrinting().create();

        NaverProfileResponse naverProfileResponse = gson1.fromJson(response.getBody().toString(), NaverProfileResponse.class);
        NaverProfileResponse.Response naverProfileInfo = naverProfileResponse.getResponse();

        return NaverUserInfo.builder()
                .nToken(naverProfileInfo.getId())
                .nickname(naverProfileInfo.getNickname())
                .email(naverProfileInfo.getEmail())
                .build();
    }

}
