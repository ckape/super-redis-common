package cn.friday.base.service.global;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


@ComponentScan(basePackages = "cn.friday")
@EnableAutoConfiguration
@ImportResource("classpath:redis-config.xml")
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
	
//	@Value("${spring.data.redis.host}")
//	private String redisHost;
//	@Value("${spring.data.redis.port}")
//	private int redisPort;
//	
//	@Resource(name="JedisConnectionFactory1")
//	RedisConnectionFactory JedisConnectionFactory1;
//	
//	@Resource(name="JedisConnectionFactory2")
//	RedisConnectionFactory JedisConnectionFactory2;
//	
//	@Bean(name="JedisConnectionFactory1")
//	public RedisConnectionFactory jedisConnectionFactory() {
//		JedisConnectionFactory conn = new JedisConnectionFactory();
//		conn.setHostName(this.redisHost);
//		conn.setPort(this.redisPort);
//		return conn;
//	}
//	
//	@Bean(name="JedisConnectionFactory2")
//	public RedisConnectionFactory jedisConnectionFactorySoruce2() {
//		JedisConnectionFactory conn = new JedisConnectionFactory();
//		conn.setHostName(this.redisHost);
//		conn.setPort(this.redisPort);
//		return conn;
//	}
//	
//	@Bean
//	public RedisTemplate<String, String> redisTemplate(){
//		RedisTemplate<String, String> template = new RedisTemplate<String, String>();
//		template.setConnectionFactory(JedisConnectionFactory1);
//		return template;
//	}
//	
//	@Bean(name = "stringRedisTemplate1")
//	public StringRedisTemplate stringRedisTemplate1(){
//		StringRedisTemplate template = new StringRedisTemplate();
//		template.setConnectionFactory(JedisConnectionFactory1);
//		return template;
//	}
//	
//	@Bean(name = "stringRedisTemplate2")
//	public StringRedisTemplate stringRedisTemplate2(){
//		StringRedisTemplate template = new StringRedisTemplate();
//		template.setConnectionFactory(JedisConnectionFactory2);
//		return template;
//	}
	
}
