package com.halfgallon.withcon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.test.context.web.WebAppConfiguration;

@ServletComponentScan
@WebAppConfiguration
@SpringBootTest
class WithconApplicationTests {

	@Test
	void contextLoads() {
	}

}
