package com.issue.manager.services.base;

import com.issue.manager.auth.JwtService;
import com.issue.manager.inputs.dtos.AuthenticationRequest;
import com.issue.manager.inputs.dtos.AuthenticationResponse;
import com.issue.manager.inputs.dtos.RegisterRequest;
import com.issue.manager.models.base.User;
import com.issue.manager.repositories.base.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        String firstName = request.getFirstName();
        if(firstName == null || firstName.length() == 0) {
            throw new IllegalArgumentException("Name must be at least one caharcter long");
        }

        String lastName = request.getLastName();
        if(lastName == null || lastName.length() == 0) {
            throw new IllegalArgumentException("Name must be at least one caharcter long");
        }

        String email = request.getEmail();
        if(email != null && email.length() > 0) {
            email = email.toLowerCase();

            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new RuntimeException("Email did not validate");
            }

//            CredentialChecker.ifUserPresentWithEmailThrowAuthException(email, userRepository);
        }

        User user = User
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .locked(false)
//                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String email = request.getEmail();
        if(email != null && email.length() > 0) {
            email = email.toLowerCase();

            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new RuntimeException("Email did not validate");
            }
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User was not found by email: " + request.getEmail()));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
