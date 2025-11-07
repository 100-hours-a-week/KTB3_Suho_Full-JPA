package kr.adapterz.jpa_practice.dto.user;

import lombok.Getter;

@Getter
public class CreateUserResponse {

    private Long user_id;

    public CreateUserResponse(Long user_id) {

        this.user_id = user_id;

    }

}
