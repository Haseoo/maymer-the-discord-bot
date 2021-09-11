package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;
import va.com.szkal.maymer.meme.MemeRepository;

import java.util.List;
import java.util.function.Consumer;

final class DeleteCommand extends DeletableMessageCommand {

    private final String memeName;
    private final MemeRepository memeRepository;

    public DeleteCommand(Consumer<Message> msgDelete, List<String> args, MemeRepository memeRepository) {
        super(msgDelete);
        this.memeName = args.get(1);
        this.memeRepository = memeRepository;
    }

    @Override
    protected String execute(long serverId) {
        return memeRepository.deleteMeme(serverId, memeName) ? "200" : "fck u, I have no meme with this name";
    }
}
