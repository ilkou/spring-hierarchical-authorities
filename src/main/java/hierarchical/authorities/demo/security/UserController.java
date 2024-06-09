package hierarchical.authorities.demo.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

    @GetMapping("/create-user")
    @Secured(Authorities.CREATE_USER)
//    @HierarchicalSecured({Authorities.CREATE_USER})
    public String createUser() {
        return "User created";
    }

    @GetMapping("/read-groups")
    @Secured(Authorities.READ_GROUPS)
//    @HierarchicalSecured({Authorities.READ_GROUPS})
    public String readGroups() {
        return "Groups read";
    }
}

