package service;

import beans.AuthDetails;
import model.Role;
import model.User;
import model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RoleRepository;
import repository.UserRepository;
import java.util.UUID;

@Service
public class AuthDetailsServiceImpl implements AuthDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    private final String SUPER_ADMIN = "super admin";

    @Override
    public AuthDetails getAuthDetails(UUID userId, boolean getAuthorizationInfo) {
        AuthDetails ad = new AuthDetails();
        User user = userRepo.findOne(userId);

        ad.setUser(user.getId());
      //  logger.debug("auth details get authz: " + getAuthorizationInfo);
        if(getAuthorizationInfo)
        {
            boolean isSuperAdmin = false;
            for(UserRole ar : user.getRoles())
            {
                if(ar.getRole().getName().equalsIgnoreCase(SUPER_ADMIN))
                {
                    isSuperAdmin = true;
                    break;
                }
            }

            if(!isSuperAdmin)
            {

                for(UserRole r : user.getRoles())
                {
                    ad.getRoles().add(r.getRole().getName());
                }
            }
            else
            {
                for(Role r : roleRepo.findAll())
                {
                    ad.getRoles().add(r.getName());
                }

            }
        }

        ad.setUserName(user.getUserName());
        ad.setLdapId(user.getLdapId());

        return ad;
    }

}