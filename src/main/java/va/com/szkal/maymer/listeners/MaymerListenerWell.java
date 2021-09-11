package va.com.szkal.maymer.listeners;


import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;

@RequiredArgsConstructor
public class MaymerListenerWell extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            return;
        }
        Message message = event.getMessage();
        String[] content = message.getContentRaw().toLowerCase().split(" ");
        if (Arrays.asList(content).contains("well")) {
            message.reply("ness").queue();
        }
    }
}
