package hierarchical.authorities.demo.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CasbinAuthenticationToken extends AbstractAuthenticationToken {

    private boolean authenticated;
    private Object principal;

    public CasbinAuthenticationToken(Object principal, boolean authenticated, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.authenticated = authenticated;

    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
