package com.issue.manager.inputs.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;
}
