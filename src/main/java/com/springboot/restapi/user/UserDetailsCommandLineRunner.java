package com.springboot.restapi.user;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private UserDetailsRepository repository;

	public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception {
		repository.save(new UserDetails("Name", "Admin"));
		repository.save(new UserDetails("test", "User"));
		repository.save(new UserDetails("dummy", "User"));
		
		List<UserDetails> users = repository.findByRole("User");
		
		users.forEach(user -> logger.info(user.toString()));
	}
}
