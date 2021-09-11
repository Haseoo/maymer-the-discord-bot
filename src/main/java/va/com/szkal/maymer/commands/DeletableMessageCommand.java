package va.com.szkal.maymer.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.function.Consumer;

@RequiredArgsConstructor
abstract class DeletableMessageCommand implements MaymerCommand {
    private final Consumer<Message> msgDelete;

    @Override
    public final void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(execute(event.getGuild().getIdLong())).queue(msgDelete);
    }

    protected abstract String execute(long serverId);
}
