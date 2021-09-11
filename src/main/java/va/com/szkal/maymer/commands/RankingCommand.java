package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;
import va.com.szkal.maymer.meme.MemeRepository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class RankingCommand extends DeletableMessageCommand {

    private final MemeRepository memeRepository;

    public RankingCommand(Consumer<Message> msgDelete, MemeRepository memeRepository) {
        super(msgDelete);
        this.memeRepository = memeRepository;
    }

    @Override
    protected String execute(long serverId) {
        var counter = new AtomicInteger(1);
        return "Top 10 memes:\n" +
                memeRepository.getTopList(serverId).stream()
                        .map(memeRecord -> String.format("%d.%s: %s times", counter.getAndIncrement(), memeRecord.getName(), memeRecord.getCount()))
                        .collect(Collectors.joining("\n"));
    }
}
