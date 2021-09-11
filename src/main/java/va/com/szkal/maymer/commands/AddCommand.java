package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;
import va.com.szkal.maymer.meme.MemeRepository;

import java.util.List;
import java.util.function.Consumer;

final class AddCommand extends DeletableMessageCommand {

    private final String memeName;
    private final String memeUrl;
    private final MemeRepository memeRepository;

    public AddCommand(Consumer<Message> msgDelete, List<String> args, MemeRepository memeRepository) {
        super(msgDelete);
        this.memeName = args.get(1);
        this.memeUrl = args.get(2);
        this.memeRepository = memeRepository;
    }

    @Override
    protected String execute(long serverId) {
        return memeRepository.addMeme(serverId, memeName, memeUrl) ? "added" : "fck u, meme exist";
    }
}
