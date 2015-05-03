package cn.friday.base.service.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;


@ComponentScan(basePackages = "cn.friday")
@EnableAutoConfiguration
public class Application {
	public static void main(String []args){
		
//		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
//		ApplicationContext ctx = SpringApplication.run(Application.class, args);
//		String []beanNames = ctx.getBeanDefinitionNames();
//		for(String beanName : beanNames){
//			System.out.println("==>" + beanName);
//		}
		SpringApplication.run(Application.class, args);
	}
	
	@Value("${spring.data.redis.host}")
	private String redisHost;
	@Value("${spring.data.redis.port}")
	private int redisPort;
	
	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory conn = new JedisConnectionFactory();
		conn.setHostName(this.redisHost);
		conn.setPort(this.redisPort);
		return conn;
	}
	
}
