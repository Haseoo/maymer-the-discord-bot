package va.com.szkal.maymer.listeners;


import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class MaymerListenerWell extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            return;
        }
        if(event.getMessage().getContentRaw().equals("!maymer well")) {
            return;
        }
        Message message = event.getMessage();
        List<String> content = Arrays.asList(message.getContentRaw().toLowerCase().split(" "));
        if (content.contains("well")) {
            message.reply("ness").queue();
        }
        if(content.contains("weIl") || content.contains("weII") || content.contains("welI")) {
            message.reply("clever!").queue();
        }
    }
}
