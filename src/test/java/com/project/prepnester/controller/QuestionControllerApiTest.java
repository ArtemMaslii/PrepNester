package com.project.prepnester.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.prepnester.BaseTest;
import com.project.prepnester.repository.QuestionRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class QuestionControllerApiTest extends BaseTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private QuestionRepository questionRepository;

  @Test
  void testGetAllQuestions() throws Exception {
    String token = obtainAccessToken();

    mockMvc.perform(get("/api/v1/questions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .param("pageNum", "0")
            .param("pageSize", "25")
            .param("isPublic", "true")
            .param("sortBy", "ASCENDING"))
        .andExpect(status().isOk());
  }

  @Test
  void testGetQuestionById() throws Exception {
    String token = obtainAccessToken();
    UUID questionId = UUID.fromString("784bfa2b-6fc4-4288-a8ae-0579c4cc0b18");

    mockMvc.perform(get("/api/v1/questions/{questionId}", questionId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void testCreateQuestion() throws Exception {
    String token = obtainAccessToken();

    String createQuestionRequestBody = """
        {
            "title": "How does sort in snowflake work?",
            "category": {
                "title": "Algorithms"
            },
            "isPublic": true,
            "subQuestions": [
                {
                    "title": "Have you used Snowflake?"
                }
            ],
            "createdBy": "a1f42067-8f71-4be1-bc9d-95adf4f5c423"
        }
        """;

    mockMvc.perform(post("/api/v1/questions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createQuestionRequestBody))
        .andExpect(status().isOk());
  }

  @Test
  void testUpdateQuestion() throws Exception {
    String token = obtainAccessToken();
    UUID questionId = UUID.fromString("784bfa2b-6fc4-4288-a8ae-0579c4cc0b18");

    String updateQuestionRequestBody = """
        {
             "title": "How does sort in Snowflake work V3?",
             "createdBy": "a1f42067-8f71-4be1-bc9d-95adf4f5c423"
         }
        """;

    mockMvc.perform(put("/api/v1/questions/{questionId}", questionId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateQuestionRequestBody))
        .andExpect(status().isOk());
  }

  @Test
  void testDeleteQuestion() throws Exception {
    String token = obtainAccessToken();
    UUID questionId = UUID.fromString("784bfa2b-6fc4-4288-a8ae-0579c4cc0b17");

    mockMvc.perform(delete("/api/v1/questions/{questionId}", questionId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isNoContent());
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
