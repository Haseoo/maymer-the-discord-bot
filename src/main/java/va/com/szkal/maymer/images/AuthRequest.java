package va.com.szkal.maymer.images;

import lombok.Data;

import java.util.Set;

@Data
public class AuthRequest {
    private long serverId;
    private String serverName;

    private Set<Long> channelIds;
}
