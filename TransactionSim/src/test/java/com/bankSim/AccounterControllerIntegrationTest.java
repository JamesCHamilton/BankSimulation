package com.bankSim;

import com.bankSim.dto.requests.LoginInRequest;
import com.bankSim.dto.requests.UserCreationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AccounterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAuthAndAccountFlow() throws Exception {
        // 1. Register a new user
        UserCreationRequest registerRequest = new UserCreationRequest(
                null,
                "Alice",
                "Smith",
                "alice.smith@test.com",
                "alicesmith",
                "alicepassword",
                new ArrayList<>(),
                new ArrayList<>()
        );

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice.smith@test.com"))
                .andExpect(jsonPath("$.message").value("User created successfully"));

        // 2. Login with the user
        LoginInRequest loginRequest = new LoginInRequest("alice.smith@test.com", "alicepassword");
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        // Extract token
        String jsonResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(jsonResponse).get("token").asText();

        // 3. Request secured accounts endpoint without token -> expects 403 Forbidden
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isForbidden());

        // 4. Request secured accounts endpoint with token -> expects 200 OK
        mockMvc.perform(get("/api/accounts")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
