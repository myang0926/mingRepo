package realm;

import com.google.common.base.Preconditions;
import model.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import repository.UserRepository;
import token.UsernamePasswordAppToken;

public class MyAuthRealm extends AuthenticatingRealm {

    @Autowired
    private UserRepository userRepo;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            UsernamePasswordAppToken upaToken = (UsernamePasswordAppToken) token;
            String username = upaToken.getUsername().toLowerCase();

            User user = userRepo.findByUserName(username);
            Preconditions.checkNotNull(user, "User not found");

            return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), "");
        } catch (Exception e) {
            throw new AuthenticationException("Login failed: " + e.getMessage(), e);
        }
    }

    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordAppToken;
    }

    public UserRepository getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
