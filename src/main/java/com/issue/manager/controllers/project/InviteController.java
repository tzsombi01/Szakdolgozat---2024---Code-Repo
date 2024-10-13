package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.InviteInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Invite;
import com.issue.manager.services.project.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Page<Invite> getInvites(@RequestBody QueryOptions queryOptions) {
        return inviteService.getInvites(queryOptions);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invite getInvite(@PathVariable String id) {
        return inviteService.getInvite(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Invite acceptInvite(@RequestBody String id) {
        return inviteService.acceptInvite(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invite declineInvite(@PathVariable String id) {
        return inviteService.declineInvite(id);
    }
}
