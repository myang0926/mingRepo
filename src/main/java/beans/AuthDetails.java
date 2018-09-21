package beans;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.UUID;

public class AuthDetails {
    private UUID user;
    private String userName;
    private String ldapId;
    private Set<String> roles = Sets.newHashSet();

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLdapId() {
        return ldapId;
    }

    public void setLdapId(String ldapId) {
        this.ldapId = ldapId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
