package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MaymerCommand {
    void execute(MessageReceivedEvent event);
}
