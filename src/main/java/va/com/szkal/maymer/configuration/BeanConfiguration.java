package va.com.szkal.maymer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class BeanConfiguration {

    @Value("${redis.redisMemeUri}")
    private String redisMemeUri;

    @Value("${redis.redisImgUri}")
    private String redisImgUri;

    @Bean(name = "meme")
    @SneakyThrows
    public Jedis jedisMeme() {
        return new Jedis(new URI(redisMemeUri));
    }

    @Bean(name = "img")
    @SneakyThrows
    public Jedis jedisImg() {
        return new Jedis(new URI(redisImgUri));
    }

    @Bean(name = "msgDelScheduler")
    public ScheduledExecutorService msgDelScheduler() {
        return Executors.newScheduledThreadPool(5);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }


    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper().findAndRegisterModules());
    }
}
