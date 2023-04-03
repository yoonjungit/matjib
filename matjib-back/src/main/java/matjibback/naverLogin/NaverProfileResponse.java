package matjibback.naverLogin;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfileResponse {
    private String resultcode;

    private String message;

    @Builder.Default
    private Response response = Response.builder().build();

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        public String id;
        public String nickname;
        public String email;

    }
}
