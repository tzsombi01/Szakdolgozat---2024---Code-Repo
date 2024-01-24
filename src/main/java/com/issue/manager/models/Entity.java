package com.issue.manager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
public class Entity {

    @Id
    private String id;

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long modifiedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    private boolean deleted = false;
}
