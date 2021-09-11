package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;
import va.com.szkal.maymer.meme.MemeRepository;

import java.util.List;
import java.util.function.Consumer;

final class UpdateCommand extends DeletableMessageCommand {

    private final String memeName;
    private final String memeUrl;
    private final MemeRepository memeRepository;

    public UpdateCommand(Consumer<Message> msgDelete, List<String> args, MemeRepository memeRepository) {
        super(msgDelete);
        this.memeName = args.get(1);
        this.memeUrl = args.get(2);
        this.memeRepository = memeRepository;
    }

    @Override
    protected String execute(long serverId) {
        return memeRepository.updateMeme(serverId, memeName, memeUrl) ? "updated" : "fck u, I have no meme with this name";
    }
}
