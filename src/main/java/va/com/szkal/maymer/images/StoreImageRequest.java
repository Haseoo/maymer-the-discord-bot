package va.com.szkal.maymer.images;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class StoreImageRequest {
    long messageId;
    long serverId;
    String username;
    String channelName;
    long channelId;
    String url;
    LocalDateTime sendTime;
}
