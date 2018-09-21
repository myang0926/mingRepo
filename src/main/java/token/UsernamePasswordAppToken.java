package token;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class UsernamePasswordAppToken  implements AuthenticationToken, RememberMeAuthenticationToken {

    private String username;
    private char[] password;
    private boolean rememberme;

    public UsernamePasswordAppToken(String username, char[] password, boolean rememberme) {
        this.username = username;
        this.password = password;
        this.rememberme = rememberme;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public boolean isRememberMe() {
        return rememberme;
    }
}
