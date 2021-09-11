package va.com.szkal.maymer.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import va.com.szkal.maymer.meme.MemeRepository;

@RequiredArgsConstructor
final class MemeCommand implements MaymerCommand {
    private final String memeName;
    private final MemeRepository memeRepository;

    @Override
    public void execute(MessageReceivedEvent event) {
        var url = memeRepository.getMemeUrl(event.getGuild().getIdLong(), memeName)
                .orElse("https://i.kym-cdn.com/photos/images/newsfeed/001/550/907/d41.jpg");
        var message = event.getMessage();
        message.getChannel().sendMessageFormat("Mem %s dla: <@%s>", memeName, message.getAuthor().getIdLong()).queue();
        MessageAction msgAction;
        var repliedMessage = message.getReferencedMessage();
        if (repliedMessage != null) {
            msgAction = repliedMessage.reply(url);
        } else {
            msgAction = event.getChannel().sendMessage(url);
        }
        msgAction.queue();
    }
}
