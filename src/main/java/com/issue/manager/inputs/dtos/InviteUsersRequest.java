package com.issue.manager.inputs.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InviteUsersRequest {
    String projectId;
    List<String> emails;
}
