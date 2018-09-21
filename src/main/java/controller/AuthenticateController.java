package controller;

import beans.AuthBean;
import beans.AuthDetails;
import exceptions.LoginFailedException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.AuthDetailsService;
import token.UsernamePasswordAppToken;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping(value = "/api/v1/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
public class AuthenticateController {

    private Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    private AuthDetailsService authDetailsService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AuthDetails login(@RequestBody @Valid AuthBean loginBean, BindingResult result, Model model) {

        if (result.hasErrors()) {
            final String msg = "Invalid Login Request";
            if (logger.isErrorEnabled()) {
                logger.error(msg + ": " + loginBean.toString());
            }
            throw new LoginFailedException(msg);
        }

        UsernamePasswordAppToken authToken = new UsernamePasswordAppToken(
                loginBean.getUsername(),
                loginBean.getPassword().toCharArray(),
                loginBean.isRememberme()
        );

        try {
            final Subject s = SecurityUtils.getSubject();
            s.login(authToken);
            UUID userId = (UUID) s.getPrincipal();

            AuthDetails ad = authDetailsService.getAuthDetails(userId, false);

            s.logout();
            return ad;

        } catch (AuthenticationException authEx) {
            if (logger.isErrorEnabled()) {
                logger.error("Login Failed: " + authEx.getMessage(), authEx);
            }
            throw new LoginFailedException(authEx.getMessage(), authEx);
        }
    }

    @ExceptionHandler(LoginFailedException.class)
    public void handleEx(LoginFailedException ex, HttpServletResponse resp) {
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setStatus(HttpStatus.FORBIDDEN.value());
    }

}