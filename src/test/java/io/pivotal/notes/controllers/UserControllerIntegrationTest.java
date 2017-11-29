package io.pivotal.notes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.notes.models.UserRequest;
import io.pivotal.notes.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerIntegrationTest {

    private static final String USER = "USER";
    private static final String PASS = "PASS";

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        UserController controller = new UserController(userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void should_return200Ok_andUserId_when_newUserSaved() throws Exception {
        String body = objectMapper.writeValueAsString(new UserRequest(null, USER, PASS));
        mockMvc.perform(post("/api/v1/user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value(USER))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    public void should_return200Ok_andUserId_when_newUserUpdated() throws Exception {
        userRepository.saveOrUpdateUser(null , USER, PASS);
        String body = objectMapper.writeValueAsString(new UserRequest(1, USER, PASS));
        mockMvc.perform(post("/api/v1/user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value(USER))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    public void should_return400BadRequest_when_usernameNullOrEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(new UserRequest(null, null, PASS));
        mockMvc.perform(post("/api/v1/user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Username and/or password cannot be blank"))
                .andExpect(jsonPath("$.user.id").doesNotExist())
                .andExpect(jsonPath("$.user.username").doesNotExist());

        body = objectMapper.writeValueAsString(new UserRequest(null, "", PASS));
        mockMvc.perform(post("/api/v1/user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Username and/or password cannot be blank"))
                .andExpect(jsonPath("$.user.id").doesNotExist())
                .andExpect(jsonPath("$.user.username").doesNotExist());
    }

    @Test
    public void should_return400BadRequest_when_passwordNullOrEmpty() throws Exception {
        String body = objectMapper.writeValueAsString(new UserRequest(null, USER, null));
        mockMvc.perform(post("/api/v1/user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Username and/or password cannot be blank"))
                .andExpect(jsonPath("$.user.id").doesNotExist())
                .andExpect(jsonPath("$.user.username").doesNotExist());

        body = objectMapper.writeValueAsString(new UserRequest(null, USER, ""));
        mockMvc.perform(post("/api/v1/user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Username and/or password cannot be blank"))
                .andExpect(jsonPath("$.user.id").doesNotExist())
                .andExpect(jsonPath("$.user.username").doesNotExist());
    }

    @Test
    public void should_return200Ok_andUser_when_userFetchedById() throws Exception {
        userRepository.saveOrUpdateUser(null , USER, PASS);
        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value(USER))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    public void should_return400BadRequest_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("User not found"))
                .andExpect(jsonPath("$.user.id").doesNotExist())
                .andExpect(jsonPath("$.user.username").doesNotExist());
    }
}
