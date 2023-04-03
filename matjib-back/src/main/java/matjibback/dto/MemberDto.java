package matjibback.dto;

import lombok.Getter;

@Getter
public class MemberDto {
    private int memid;

    private String token;

    private String nickname;

    private String email;
}
