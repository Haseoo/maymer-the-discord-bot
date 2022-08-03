package va.com.szkal.maymer.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import va.com.szkal.maymer.images.ImageStoreService;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
final class ImagesCommand implements MaymerCommand {
    private final ImageStoreService imageStoreService;

    @Override
    public void execute(MessageReceivedEvent event) {
        final var author = event.getAuthor();
        final var authorChannels =
                event.getGuild().getTextChannels()
                        .stream().filter(textChannel -> textChannel.getMembers().stream()
                                .anyMatch(channelMember -> channelMember.getUser().getIdLong() == author.getIdLong()))
                        .map(TextChannel::getIdLong)
                        .collect(Collectors.toSet());
        event.getMessage().getAuthor()
                .openPrivateChannel()
                .queue(privateChannel -> sendAccessUrl(privateChannel,
                        event.getGuild(),
                        authorChannels));
        event.getMessage().delete().queue();
    }

    private void sendAccessUrl(PrivateChannel privateChannel, Guild server, Set<Long> channels) {
        privateChannel.sendMessageFormat("This is your single-use link <%s> \n it will expire in %s minutes!",
                imageStoreService.generateLink(server, channels), ImageStoreService.LINK_ID_EXPIRE_MINUTES).queue();
    }
}
