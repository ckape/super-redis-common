package cn.friday.base.service.global;

import java.util.concurrent.CountDownLatch;

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
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import cn.friday.base.service.global.pubsub.IntBakReceiver;
import cn.friday.base.service.global.pubsub.IntReceiver;
import cn.friday.base.service.global.pubsub.MeReceiver;
import cn.friday.base.service.global.pubsub.Receiver;
import cn.friday.base.service.global.redis.support.pubsub.MessageListenerContainerImpl;
import redis.clients.jedis.JedisPoolConfig;


@ComponentScan(basePackages = "cn.friday")
@EnableAutoConfiguration
//@ImportResource("classpath:redis-config.xml")
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
	
	@Autowired
	RedisConnectionFactory jedisConnectionFactory;
	
	@Autowired
	RedisConnectionFactory messageRedisFactory;
	
	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory conn = new JedisConnectionFactory();
		System.out.println("------------>"+redisHost+":"+redisPort);
		conn.setHostName(this.redisHost);
		conn.setPort(this.redisPort);
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(2000);
		config.setMaxIdle(2000);
		config.setMaxWaitMillis(3000);
		//多久检测一次空闲连接
		config.setTimeBetweenEvictionRunsMillis(30000);
		//空闲多少秒后连接被回收
		config.setMinEvictableIdleTimeMillis(30000);
		config.setTestOnBorrow(true);
		conn.setPoolConfig(config);
		return conn;
	}
	
	@Bean
	public RedisConnectionFactory messageRedisFactory() {
		JedisConnectionFactory conn = new JedisConnectionFactory();
		conn.setHostName("192.168.0.36");
		conn.setPort(6379);
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(20);
		config.setMaxWaitMillis(3000);
		//多久检测一次空闲连接
		config.setTimeBetweenEvictionRunsMillis(30000);
		//空闲多少秒后连接被回收
		config.setMinEvictableIdleTimeMillis(30000);
		config.setTestOnBorrow(true);
		conn.setPoolConfig(config);
		return conn;
	}
	
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
	
	@Bean
	CountDownLatch latch(){
		return new CountDownLatch(1);
	}
	
	@Bean
	Receiver receiver(CountDownLatch latch){
		return new Receiver();
	}
	
	@Bean
	StringRedisTemplate stringRedisTemplate(){
		return new StringRedisTemplate(jedisConnectionFactory);
	}
	
	@Bean
	RedisTemplate<String, String> redisTemplate(){
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory);
		return template;
	}
	
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver){
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
//	@Bean
//	RedisMessageListenerContainer container(RedisConnectionFactory jedisConnectionFactory, MessageListenerAdapter listenerAdapter){
//		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//		container.setConnectionFactory(jedisConnectionFactory);
//		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
//		container.addMessageListener(new MeReceiver(),  new PatternTopic("chat"));
//		return container;
//	}
	
	@Autowired
	Receiver receiver;
	
	@Autowired
	IntReceiver intReceiver;
	
	@Autowired
	IntBakReceiver intBakReceiver;
	
	@Bean
	MessageListenerContainerImpl messageListenerContainerImpl(){
		MessageListenerContainerImpl container = new MessageListenerContainerImpl(messageRedisFactory);
//		container.addMessageListener(receiver);
//		container.addMessageListener(intReceiver);
//		container.addMessageListener(intBakReceiver);
		return container;
	}
	
//	@Bean
//	MessageListenerContainerImpl messageListenerContainerImpl(){
//		return new MessageListenerContainerImpl(this.redisHost, this.redisPort);
//	}
	
}
