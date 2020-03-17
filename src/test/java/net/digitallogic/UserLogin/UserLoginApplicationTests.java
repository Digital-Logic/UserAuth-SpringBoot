package net.digitallogic.UserLogin;

import net.digitallogic.UserLogin.web.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserLoginApplicationTests {

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertNotNull(userController);
	}

}
