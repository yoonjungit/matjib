package matjibback.naverLogin;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class NaverUserInfo {
    private String id;
    private String nickname;
    private String email;
    private String name;
    private String birthday;
    private String birthyear;
}
