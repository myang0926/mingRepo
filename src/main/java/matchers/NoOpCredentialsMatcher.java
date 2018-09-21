package matchers;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

public class NoOpCredentialsMatcher implements CredentialsMatcher {

    /**
     * Used with RestRealm as the creds have been matched in the auth service.
     * If The credentials do not match the RestRealm throws an exception.
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return true;
    }

}
