package com.kurakura.posblockchain.backend;

import com.kurakura.posblockchain.backend.controller.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableCaching
public class Application {

	@Autowired
	private UserController userController;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		Application app = ctx.getBean(Application.class);
		app.initialize(args);
	}

	private void initialize(String[] args) {
		log.info("Initialize Pos_Blockchain Backend Application.");
		int i = 0;
		boolean isInitialized = false;
		while (!isInitialized && i < 10) {
			try {
				isInitialized = userController.initialize();
			} catch (Exception e) {
				log.error(String.format("Failed to initialize %d times", i+1));
			}
			i++;
		}
		log.info("Finish to initialize userPos_Blockchain Backend Application.s. {SystemUser, Administrator}");
	}

}
