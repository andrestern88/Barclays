package com.example.barclays.controllers;

import com.example.barclays.TestDataUtil;
import com.example.barclays.domain.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Autowired
    public UserControllerIntegrationTest(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsSavedAuthor() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testUser.getId())
        );
    }

    @Test
    public void testThatUserCanAuthenticate() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        );
        final String credentialsJson = "{\n" +
        "    \"username\": \"" + testUser.getUsername() + "\",\n" +
        "    \"password\": \"" + testUser.getPassword() + "\"\n" +
        "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatUserCannotAuthenticateWithInvalidCredentials() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        );
        final String credentialsJson = "{\n" +
                "    \"username\": \"testuser\",\n" +
                "    \"password\": \"testpass\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsJson)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testThatAuthenticatedUserCanFetchTheirDetails() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        );
        final String credentialsJson = "{\n" +
                "    \"username\": \"" + testUser.getUsername() + "\",\n" +
                "    \"password\": \"" + testUser.getPassword() + "\"\n" +
                "}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsJson)
        ).andReturn();

        String jwt = result.getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/"+testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatUnauthenticatedUserCannotFetchTheirDetails() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/"+testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testThatAuthenticatedUserCanUpdateTheirDetails() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstnames").value("Test")
        );
        final String credentialsJson = "{\n" +
                "    \"username\": \"" + testUser.getUsername() + "\",\n" +
                "    \"password\": \"" + testUser.getPassword() + "\"\n" +
                "}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsJson)
        ).andReturn();

        String jwt = result.getResponse().getContentAsString();

        final String updateUserJson = "{\n" +
                "    \"firstnames\": \"Updated\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/users/"+testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserJson)
                .header("Authorization", "Bearer " + jwt)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstnames").value("Updated")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatAuthenticatedUserCannotFetchOtherUserDetails() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        );
        final String credentialsJson = "{\n" +
                "    \"username\": \"" + testUser.getUsername() + "\",\n" +
                "    \"password\": \"" + testUser.getPassword() + "\"\n" +
                "}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsJson)
        ).andReturn();

        String jwt = result.getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/usr-4")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testThatAuthenticatedUserCanDeleteTheirDetails() throws Exception {
        User testUser = TestDataUtil.createTestUser("user-3");
        final String userJson = mapper.writeValueAsString(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        );
        final String credentialsJson = "{\n" +
                "    \"username\": \"" + testUser.getUsername() + "\",\n" +
                "    \"password\": \"" + testUser.getPassword() + "\"\n" +
                "}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsJson)
        ).andReturn();

        String jwt = result.getResponse().getContentAsString();

        final String updateUserJson = "{\n" +
                "    \"firstnames\": \"Updated\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/"+testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
