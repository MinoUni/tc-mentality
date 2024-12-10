package io.teamchallenge.mentality;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MentalityApplicationTests {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void shouldLoadContext() {
    /* Just to check that spring context load properly */
    assertNotNull(applicationContext);
  }
}
