package va.com.szkal.maymer.listeners;


import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import va.com.szkal.maymer.commands.CommandService;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class MaymerListener extends ListenerAdapter {
    private final CommandService commandService;


    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            return;
        }
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (!content.startsWith("!maymer")) {
            return;
        }
        event.getChannel().deleteMessageById(message.getIdLong()).queue();

        commandService.parseCommand(content).execute(event);
    }
}
