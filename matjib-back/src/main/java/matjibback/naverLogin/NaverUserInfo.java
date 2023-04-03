package matjibback.naverLogin;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class NaverUserInfo {
    private String nToken;
    private String nickname;
    private String email;
}
