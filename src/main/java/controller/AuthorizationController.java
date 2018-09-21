package controller;

import beans.AuthDetails;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.AuthDetailsService;

import java.util.UUID;

@Controller
@RequestMapping(value = "/api/v1/authorization", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
public class AuthorizationController {

    public AuthorizationController() {}

    @Autowired
    private AuthDetailsService authDetailsService;

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    @ResponseBody
    public AuthDetails details(@PathVariable("id") UUID userId)
    {
        return authDetailsService.getAuthDetails(userId, true);
    }
}