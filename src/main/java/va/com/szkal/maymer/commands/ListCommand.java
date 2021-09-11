package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;
import va.com.szkal.maymer.Env;
import va.com.szkal.maymer.meme.MemeRepository;

import java.util.function.Consumer;
import java.util.stream.Collectors;

final class ListCommand extends DeletableMessageCommand {

    private final MemeRepository memeRepository;
    private final Env env;

    public ListCommand(Consumer<Message> msgDelete,
                       MemeRepository memeRepository,
                       Env env) {
        super(msgDelete);
        this.memeRepository = memeRepository;
        this.env = env;
    }

    @Override
    protected String execute(long serverId) {
        return String.format("Quality meme list:%n%s%nYou can see memes at <%s?id=%s>",
                memeRepository.getAllNames(serverId).stream().sorted().collect(Collectors.joining(", ")),
                env.getMaymerLinkUrl(),
                serverId);
    }
}
