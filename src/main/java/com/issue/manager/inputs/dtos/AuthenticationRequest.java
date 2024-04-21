package com.issue.manager.inputs.dtos;

import lombok.*;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    private String email;

    private String password;
}
