package com.qrlo.qrloservicecore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = QrloServiceCoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QrloServiceCoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
