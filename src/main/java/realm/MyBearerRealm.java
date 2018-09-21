package realm;

import antlr.StringUtils;
import beans.AuthBean;
import beans.AuthDetails;
import com.google.common.base.Preconditions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import repository.UserRepository;
import service.AppConfig;
import token.JWTAuthenticationToken;
import token.UsernamePasswordAppToken;

import javax.xml.bind.DatatypeConverter;
import java.util.Collection;

public class MyBearerRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AppConfig appConfig;

    private final String REALM_NAME = "myBearerRealm";

    private  final String detailsUrl = "http://localhost:8080/api/v1/authorization";

    private static final Logger logger = LoggerFactory.getLogger(MyRestRealm.class);


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JWTAuthenticationToken upaToken = (JWTAuthenticationToken) token;
        String username = upaToken.getUsername().toLowerCase();
        String tokenSub = getSubject(upaToken.getToken());
        User user = userRepo.findByUserName(username);
        Preconditions.checkNotNull(user, "User not found");

        if(username.equals(tokenSub) && user !=null)
            return new SimpleAuthenticationInfo(user.getId(),upaToken.getToken(),"");
        else
            throw new AuthenticationException("Error occurred.");
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        boolean supports = false;
        if(JWTAuthenticationToken.class.isInstance(token))
        {
            supports = true;
        }
        return supports;
    }

    private String getSubject(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(appConfig.getSecret()))
                .parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
        AuthDetails ad = getAuthorizationDetails(principalCollection.getPrimaryPrincipal().toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        simpleAuthorizationInfo.addRoles((Collection<String>)ad.getRoles());

        return simpleAuthorizationInfo;
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
}
