package com.spring.on.aws.lambda.config;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class DatasourceConfig {

	private static final String CLASS_LOG_TAG = "<<<--- DatasourceConfig --->>>";
	private static final Logger _LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Bean
	public SingleConnectionDataSource dataSource() {
		_LOGGER.info(CLASS_LOG_TAG, " Configuring Single Connection DataSource");
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
		singleConnectionDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		singleConnectionDataSource.setUrl(System.getenv("AWS_RDS_ENDPOINT"));
		singleConnectionDataSource.setUsername(System.getenv("AWS_RDS_USERNAME"));
		singleConnectionDataSource.setPassword(System.getenv("AWS_RDS_PASSWORD"));
		singleConnectionDataSource.setSuppressClose(true);
		_LOGGER.info(CLASS_LOG_TAG, " Single Connection DataSource Done");
		return singleConnectionDataSource;
	}

	@Bean
	public JedisConnectionFactory redisConnnectionFactory() {
		_LOGGER.info(CLASS_LOG_TAG, " Configuring Jedis Connection");
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(System.getenv("REDIS_ENDPOINT"));
		config.setPort(Integer.parseInt(System.getenv("REDIS_PORT")));

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMinIdle(300);
		JedisClientConfiguration clientConfig = JedisClientConfiguration.builder().readTimeout(Duration.ofSeconds(180))
				.connectTimeout(Duration.ofSeconds(180)).clientName("jedis-client").usePooling().poolConfig(poolConfig)
				.build();
		_LOGGER.info(CLASS_LOG_TAG, " Jedis Pooling Done");
		return new JedisConnectionFactory(config, clientConfig);
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		_LOGGER.info(CLASS_LOG_TAG, " Redis Template Initialized");
		final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return redisTemplate;
	}
}
