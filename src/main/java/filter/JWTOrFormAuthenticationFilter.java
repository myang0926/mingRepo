package filter;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import service.AppConfig;
import token.JWTAuthenticationToken;

public final class JWTOrFormAuthenticationFilter extends AuthenticatingFilter {

    public static final String USER_ID = "username";
    public static final String PASSWORD = "password";

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AppConfig appConfig;

    public JWTOrFormAuthenticationFilter() {
        setLoginUrl(DEFAULT_LOGIN_URL);
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        //Always return true if the request's method is OPTIONS
        if(request instanceof HttpServletRequest){
            if(((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")){
                return true;
            }
        }

        return  super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

       boolean loggedIn = executeLogin(request, response);

        if (!loggedIn) {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return loggedIn;
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws IOException {

        if (isLoggedAttempt(request, response)) {
            String jwtToken = getAuthzHeader(request);
            if (jwtToken != null && jwtToken.contains("Bearer")) {
                return createToken(jwtToken.replace("Bearer ",""));
            }
        }

        return null;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return false;
    }

    protected boolean isLoggedAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = getAuthzHeader(request);
        return authzHeader != null;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    public JWTAuthenticationToken createToken(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(appConfig.getSecret()))
                    .parseClaimsJws(jwt).getBody();
                String username = claims.getSubject();
                return new JWTAuthenticationToken(username, jwt);


        } catch (Exception ex) {
            throw new AuthenticationException(ex);
        }

    }

}