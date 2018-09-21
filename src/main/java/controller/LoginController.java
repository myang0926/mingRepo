package controller;

import dto.LoginDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.TokenProvider;
import token.UsernamePasswordAppToken;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins={"http://localhost:8100","http://localhost:8080"})
public class LoginController {

    @Autowired
    private ServletContext sc;

    @Autowired
    private TokenProvider tokenProvider;

    @RequestMapping(value="/login*", method=RequestMethod.GET)
    public String login(Model model)
    {
        LoginDto dto = new LoginDto();
        model.addAttribute("loginDto", dto);

        DateTime start = DateTime.now();
        DateTime end = start.plusMinutes(240);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "login";
    }


    @RequestMapping(value="/authenticate", method=RequestMethod.GET)
    public String authenticate() {
        // we don't have to do anything here
        // this is just a secure endpoint and the JWTFilter
        // validates the token
        // this service is called at startup of the app to check
        // if the jwt token is still valid
        return null;
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String doLogin(@Valid @RequestBody LoginDto dto, HttpServletResponse response,BindingResult result, Model model) {
        if(result.hasErrors())
        {
            model.addAttribute("loginFailed", "Please supply a username and password.");
            return null;
        }

        try{
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordAppToken token = new UsernamePasswordAppToken(
                dto.getUsername(),
                dto.getPassword().toCharArray(),
                false
        );
            currentUser.login(token);
            return tokenProvider.createToken(dto.getUsername());
        }
        catch(AuthenticationException ex)
        {
            model.addAttribute("loginFailed", "Login failed. Please check your username and password.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

    }
}
