package com.issue.manager.inputs.dtos;

import lombok.*;

@Builder
@Getter
@Setter
public class AuthenticationResponse {
    private String token;
}
