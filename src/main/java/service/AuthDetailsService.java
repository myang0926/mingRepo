package service;

import beans.AuthDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AuthDetailsService {

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public AuthDetails getAuthDetails(UUID userId, boolean getAuthorizationInfo);
}
