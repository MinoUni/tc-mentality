package io.teamchallenge.mentality.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GithubTokenClient.class)
class GithubTokenClientTest {

  @Autowired private MockRestServiceServer mockRestServiceServer;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private GithubTokenClient githubTokenClient;

  @Test
  void shouldGetGithubUserProfile() throws JsonProcessingException {
    var data = Map.of("email", "email value", "name", "name value");
    mockRestServiceServer
        .expect(requestTo("https://api.github.com/user"))
        .andRespond(withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

    var response = githubTokenClient.exchange("authorization");
    assertEquals(data, response);
  }
}
