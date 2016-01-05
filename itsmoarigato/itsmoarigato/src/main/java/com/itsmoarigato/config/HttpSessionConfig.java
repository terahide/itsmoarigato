package com.itsmoarigato.config;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.redis.embedded.EnableEmbeddedRedis;



//@EnableEmbeddedRedis
// tag::class[]
//@EnableRedisHttpSession // <1>
public class HttpSessionConfig { }
// end::class[]