package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.function.Consumer;

final class HelpCommand extends DeletableMessageCommand {

    public HelpCommand(Consumer<Message> msgDelete) {
        super(msgDelete);
    }

    @Override
    protected String execute(long unused) {
        return "Commands:\n" +
                "- [meme name]\n" +
                "- add/update [meme name] [url]\n" +
                "- delete [meme name]\n" +
                "- ranking\n" +
                "- images\n" +
                "- minecraft";
    }
}
