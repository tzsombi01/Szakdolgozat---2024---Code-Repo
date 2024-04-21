package com.issue.manager.controllers.base;

import com.issue.manager.inputs.base.UserInput;
import com.issue.manager.inputs.dtos.AuthenticationRequest;
import com.issue.manager.inputs.dtos.AuthenticationResponse;
import com.issue.manager.services.base.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse register(@RequestBody UserInput request) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }
}