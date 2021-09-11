package va.com.szkal.maymer.minecraft;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import va.com.szkal.maymer.Env;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MinecraftRestDao {
    private final RestTemplate restTemplate;
    private final Env env;

    public Optional<MinecraftServerInfoDto> getServerStatus() {
        try {
            var response = restTemplate.getForEntity(env.getStatusApiUrl() + "/api/fixed", MinecraftServerInfoDto.class);
            if (response.getStatusCodeValue() != 200) {
                return Optional.empty();
            }
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
