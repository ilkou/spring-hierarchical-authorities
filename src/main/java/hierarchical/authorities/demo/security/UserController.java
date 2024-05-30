package hierarchical.authorities.demo.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/create-user")
    @HierarchicalSecured({Permission.CREATE_USER})
    public String createUser() {
        return "User created";
    }

    @GetMapping("/read-groups")
    @HierarchicalSecured({Permission.READ_GROUPS})
    public String readGroups() {
        return "Groups read";
    }
}

