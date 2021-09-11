package va.com.szkal.maymer.minecraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MinecraftServerInfoDto {
    private boolean online;
    @JsonProperty(required = false)
    private String serverName;
    @JsonProperty(required = false)
    private String version;
    @JsonProperty(required = false)
    private List<String> onlinePlayers;

    public String toString() {
        if (!online) {
            return "Server ded :(";
        }
        var stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Server is on%nVersion: %s%n", version));
        if (onlinePlayers.isEmpty()) {
            stringBuilder.append("Nobody is playing :(");
        } else {
            stringBuilder.append("Are playing: ");
            stringBuilder.append(String.join(",", onlinePlayers));
        }
        return stringBuilder.toString();
    }
}
