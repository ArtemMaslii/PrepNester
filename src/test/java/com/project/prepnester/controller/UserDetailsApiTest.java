package com.project.prepnester.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.prepnester.BaseTest;
import com.project.prepnester.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class UserDetailsApiTest extends BaseTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testRegisterUser() throws Exception {
    String token = obtainAccessToken();

    String registerRequestBody = """
        {
          "fullName": "Test User",
          "email": "testuser@example.com",
          "password": "password123",
          "phoneNumber": null,
          "gender": "MALE"
        }
        """;

    mockMvc.perform(post("/api/v1/user/register")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(registerRequestBody))
        .andExpect(status().isOk());
  }

  private String obtainAccessToken() throws Exception {
    String loginRequestBody = """
        {
            "email": "%s",
            "password": "%s"
        }
        """.formatted("alice@example.com", "password_hash1");

    MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequestBody))
        .andExpect(status().isOk())
        .andReturn();

    String responseContent = result.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(responseContent);

    return jsonNode.get("accessToken").asText();
  }
}
