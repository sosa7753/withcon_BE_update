package com.halfgallon.withcon;

import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories(basePackages = {"com.halfgallon.withcon.domain.auth.repository"})
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		AccessTokenRepository.class, RefreshTokenRepository.class}))
@SpringBootApplication
public class WithconApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithconApplication.class, args);
	}
}
