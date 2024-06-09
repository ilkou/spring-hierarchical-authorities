package hierarchical.authorities.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CasbinFilter extends OncePerRequestFilter {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CasbinFilter.class);

    private final Enforcer enforcer;

    public CasbinFilter(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();

        for (String role : roles) {
            if (enforcer.enforce(role, path, method)) {
                List<String> rolesForUser = enforcer.getRolesForUser(role);
                log.info("Granted!! {} {}", role, rolesForUser);

                List<String> rol = new ArrayList<>();
                rol.add(role);
                rol.addAll(rolesForUser);
                List<SimpleGrantedAuthority> newAuths = rol.stream().map(SimpleGrantedAuthority::new).toList();

                // https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#_tabs_1
                SecurityContext newContext = SecurityContextHolder.createEmptyContext();
                Authentication newAuthentication = new CasbinAuthenticationToken(auth.getPrincipal(), auth.isAuthenticated(), newAuths);
                newContext.setAuthentication(newAuthentication);
                SecurityContextHolder.setContext(newContext);

                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
