package hierarchical.authorities.demo.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthoritiesDependencies {

    private static final Map<String, Set<String>> authorities = new HashMap<>();

    static {
        authorities.put(Authorities.CREATE_USER, Set.of(Authorities.READ_GROUPS));
        // Add more dependencies if needed
    }

    private static Set<String> getAuthorities(String authority) {
        return authorities.getOrDefault(authority, Collections.emptySet());
    }

    public static boolean isDependent(String authority, String dependentAuthority) {
        Set<String> auths = getAuthorities(authority);
        if (auths.isEmpty()) {
            return false;
        }
        return auths.contains(dependentAuthority);
    }

}

