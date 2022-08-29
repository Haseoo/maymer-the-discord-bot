package va.com.szkal.maymer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "env")
@Data
public class Env {
    private String botToken;

    private String maymerUrl;

    private String maymerLinkUrl;

    private String statusApiUrl;

    private String dscViewerUrl;

    private String dscViewerRedirectUrl;

    private String domain;

    private boolean prod;

    private String earwigSecret;

    private String earwigSecretEmoteName;

    private int messageDeletionTimeout;

    private String rabbitImageQueue;

    private String rabbitMessageDeletedQueue;

    private long serverId;
}
