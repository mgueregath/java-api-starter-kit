/*
 * Tasty.
 */
package io.codeffeine.starterkit.api.controller;

import io.codeffeine.starterkit.api.adapter.response.ResponseAdapter;
import io.codeffeine.starterkit.api.command.ChangePasswordCommand;
import io.codeffeine.starterkit.api.configuration.PathConfiguration;
import io.codeffeine.starterkit.domain.security.entity.User;
import io.codeffeine.starterkit.facade.container.ServiceContainer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mirko Gueregat @mgueregath <mgueregath@emendare.cl>
 */
@RestController
@RequestMapping(PathConfiguration.API_PATH + "/profile")
public class ProfileController {

    @Autowired
    private ServiceContainer sc;

    @Autowired
    private ResponseAdapter response;

    @ModelAttribute("user")
    public User getClientUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public @ResponseBody
    ResponseEntity changePassword(
            @ModelAttribute("user") User user,
            @RequestBody ChangePasswordCommand command
    ) {
        return response.send(
                true,
                HttpStatus.OK,
                sc.getUserService().changePassword(
                        user, command.getCurrentPassword(),
                        command.getNewPassword(),
                        command.getRepeatedPassword()
                )
        );
    }
}
