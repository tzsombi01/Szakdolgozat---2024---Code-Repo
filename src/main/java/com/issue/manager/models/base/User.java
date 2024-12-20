package com.issue.manager.models.base;

import com.issue.manager.models.Entity;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
@Document(collection = User.USERS_COLLECTION_NAME)
public class User extends Entity implements UserDetails {

    public static final String USERS_COLLECTION_NAME = "users";

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> gitUserNames;
    private String accessToken; // GitHub access token
    private boolean active;
    private boolean locked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.name()));
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getUsernameUserName() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAccessTokenSet() {
        return accessToken != null && !accessToken.isEmpty();
    }
}
