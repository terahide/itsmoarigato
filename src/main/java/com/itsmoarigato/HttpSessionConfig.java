package com.itsmoarigato;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.redis.embedded.EnableEmbeddedRedis;

//@EnableEmbeddedRedis
//@EnableRedisHttpSession
public class HttpSessionConfig {
	@Bean
	JedisConnectionFactory connectionFactory(){
		return new JedisConnectionFactory();
	}
} 	 	
