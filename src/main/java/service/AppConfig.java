package service;

import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    private String secret = "the_secret";

    private long tokenValidityInSeconds = 2592000;

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getTokenValidityInSeconds() {
        return this.tokenValidityInSeconds;
    }

    public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }
}