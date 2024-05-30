package hierarchical.authorities.demo.security;

import java.util.Set;

public enum Permission {
    READ_GROUPS("READ_GROUPS", Set.of()),
    CREATE_USER("CREATE_USER", Set.of(Permission.READ_GROUPS));

    private final String permission;
    private final Set<Permission> dependencies;

    Permission(String permission, Set<Permission> dependencies) {
        this.permission = permission;
        this.dependencies = dependencies;
    }

    public String getPermission() {
        return permission;
    }

    public Set<Permission> getDependencies() {
        return dependencies;
    }

    public boolean isDependent(String dependentPermission) {
        Permission value;
        try {
            value = Permission.valueOf(dependentPermission);
        } catch (IllegalArgumentException e) {
            return false;
        }
        Set<Permission> auths = value.getDependencies();
        if (auths.isEmpty()) {
            return  false;
        }
        return auths.stream().map(Permission::toString).toList().contains(permission);
    }


    @Override
    public String toString() {
        return permission;
    }

}
