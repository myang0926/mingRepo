package realm;

import beans.AuthBean;
import beans.AuthDetails;
import com.google.common.base.Preconditions;
import model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import repository.UserRepository;
import token.UsernamePasswordAppToken;
import utils.CurrentUserUtils;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRestRealm extends AuthorizingRealm {

    private  final String authURL = "http://localhost:8080/api/v1/authenticate";

    private  final String detailsUrl = "http://localhost:8080/api/v1/authorization";
    private final String REALM_NAME = "myRealm";

    private static final Logger logger = LoggerFactory.getLogger(MyRestRealm.class);

    @Autowired
    private UserRepository userRepo;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordAppToken upaToken = (UsernamePasswordAppToken) token;
        String username = upaToken.getUsername().toLowerCase();

        User user = userRepo.findByUserName(username);
        upaToken.setUsername(upaToken.getUsername().toLowerCase());
        Preconditions.checkNotNull(user, "User not found");

        return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), getName());

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
        AuthDetails ad = getAuthorizationDetails(principalCollection.getPrimaryPrincipal().toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        simpleAuthorizationInfo.addRoles((Collection<String>)ad.getRoles());

        return simpleAuthorizationInfo;
    }

    protected AuthDetails getAccount(UsernamePasswordAppToken token) {
        AuthBean authBean = new AuthBean();
        authBean.setUsername(token.getUsername());
        authBean.setPassword(new String(token.getPassword()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<AuthBean> entity = new HttpEntity<AuthBean>(authBean, headers);

        RestTemplate rest = new RestTemplate();
        AuthDetails ad = null;
        try{
            ad = rest.postForObject(authURL, entity, AuthDetails.class);
            logger.debug(ad.getUser().toString());
        }
        catch(Exception e)
        {
            throw new AuthenticationException("RestRealm failed to authenticate or unexpected error occurred:\n" + e.getMessage(), e);
        }
        return ad;
    }

    protected AuthDetails getAuthorizationDetails(String userId)
    {
        AuthDetails ad = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        final RestTemplate rest = new RestTemplate();

        try{
            logger.debug(detailsUrl + "/" + userId);
            ad = rest.postForObject(detailsUrl + "/" + userId, new HttpEntity<HttpHeaders>(headers), AuthDetails.class);
            logger.debug("Got role details for " + userId + ": " + ad.getRoles().size());
        }
        catch(Exception e)
        {
            throw new AuthenticationException("RestRealm failed to get authorization info or unexpected error occurred:\n" + e.getMessage(), e);
        }
        return ad;
    }

    @Override
    public String getName()
    {
        return REALM_NAME;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        boolean supports = false;
        if(UsernamePasswordAppToken.class.isInstance(token))
        {
            supports = true;
        }
        return supports;
    }

    public String getAuthURL() {
        return authURL;
    }

    public String getAppName() {
        return REALM_NAME;
    }

    public void clearCachedAuthorizationInfo(PrincipalCollection coll)
    {
        super.clearCachedAuthorizationInfo(coll);
    }
}
