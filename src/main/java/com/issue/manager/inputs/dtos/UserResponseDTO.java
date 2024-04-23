package com.issue.manager.inputs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> gitUserNames;
}
