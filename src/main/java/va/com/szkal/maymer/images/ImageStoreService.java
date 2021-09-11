package va.com.szkal.maymer.images;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import va.com.szkal.maymer.Env;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;


@Service
@Slf4j
public class ImageStoreService {

    private final Jedis client;
    private final SecureRandom secureRandom;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final Env env;

    public static final long LINK_ID_EXPIRE_MINUTES = 5;
    private static final long LINK_ID_EXPIRE_SECONDS = 60 * LINK_ID_EXPIRE_MINUTES;


    public static final String COOKIE_JWT = "IMG_TOKEN";
    public static final int TOKEN_EXPIRATION = 30 * 60;

    public ImageStoreService(@Qualifier("img") Jedis client,
                             SecureRandom secureRandom,
                             RestTemplate restTemplate,
                             RabbitTemplate rabbitTemplate,
                             Env env) {
        this.client = client;
        this.secureRandom = secureRandom;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.env = env;
    }

    public void updateChannelName(long channelId, String newName) {
        try {
            var url = env.getDscViewerUrl() + "/api/channel" + channelId;
            restTemplate.patchForObject(url, new ChannelRequest(newName), Object.class);
        } catch (Exception e) {
            log.error("Update channel name", e);
        }
    }

    public void deleteChannel(long channelId) {
        try {
            var url = env.getDscViewerUrl() + "/api/channel" + channelId;
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Update channel name", e);
        }
    }

    public void store(StoreImageRequest request) {
        try {
            rabbitTemplate.convertAndSend(env.getRabbitImageQueue(), request);
        } catch (Exception e) {
            log.error("Image upload", e);
        }
    }

    @SneakyThrows
    public String generateLink(Guild server) {
        ObjectMapper objectMapper = new ObjectMapper();
        AuthRequest authRequest = new AuthRequest();
        authRequest.setServerId(server.getIdLong());
        authRequest.setServerName(server.getName());

        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        String id = Base64.getUrlEncoder().encodeToString(bytes);

        var params = new SetParams();
        params.ex(LINK_ID_EXPIRE_SECONDS);
        client.set(id, objectMapper.writeValueAsString(authRequest), params);
        return env.getMaymerLinkUrl() + "/img?id=" + id;
    }

    public boolean isTokenPresent(String id) {
        return client.exists(id);
    }

    public String getImageJwt(String id) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AuthRequest authRequest = objectMapper.readValue(client.get(id), AuthRequest.class);
            var url = env.getDscViewerUrl() + "/api/token";
            var response = restTemplate.postForEntity(url, authRequest, ImageTokenResponse.class);
            if (response.getStatusCodeValue() != 200) {
                return "";
            }
            return Objects.requireNonNull(response.getBody()).getToken();
        } catch (Exception e) {
            log.error("JWT error", e);
            return "";
        }
    }

    public void deleteToken(String token) {
        client.del(token);
    }
}
