package com.bcg.dv.challenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationIT {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${server.port}")
	private String serverPort;

	@Test
	public void contextLoads() {
		assertThat(activeProfile).contains("dev");
	}

}
