package hierarchical.authorities.demo;

import hierarchical.authorities.demo.security.Authorities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HierarchicalSecuredTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "user1", authorities = {Authorities.CREATE_USER})
    void testCreateUser() throws Exception {
        mvc.perform(get("/create-user"))
                .andExpect(status().isOk())
                .andExpect(content().string("User created"));

    }

    @Test
    @WithMockUser(username = "user2", authorities = {Authorities.READ_GROUPS})
    void testReadGroups() throws Exception {
        mvc.perform(get("/read-groups"))
                .andExpect(status().isOk())
                .andExpect(content().string("Groups read"));
    }

    @Test
    @WithMockUser(username = "user3", authorities = {Authorities.CREATE_USER})
    void testReadGroupsWithDependentAuthority() throws Exception {
        mvc.perform(get("/read-groups"))
                .andExpect(status().isOk())
                .andExpect(content().string("Groups read"));
    }

    @Test
    @WithMockUser(username = "user4", authorities = {Authorities.READ_GROUPS})
    void testCreateUserWithBadAuthority() throws Exception {
        mvc.perform(get("/create-user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user5")
    void testCreateUserWithNoAuthorities() throws Exception {
        mvc.perform(get("/create-user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user6")
    void testReadGroupsWithNoAuthorities() throws Exception {
        mvc.perform(get("/read-groups"))
                .andExpect(status().isForbidden());
    }


}
