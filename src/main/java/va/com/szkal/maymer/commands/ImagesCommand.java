package va.com.szkal.maymer.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import va.com.szkal.maymer.images.ImageStoreService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
final class ImagesCommand implements MaymerCommand {
    private final ImageStoreService imageStoreService;

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getMessage().getAuthor()
                .openPrivateChannel()
                .queue(privateChannel -> sendAccessUrl(privateChannel,
                        event.getGuild(),
                        getPermittedChannels(event)));
        event.getMessage().delete().queue();
    }

    private Set<Long> getPermittedChannels(MessageReceivedEvent event) {
        var authorMember = Objects.requireNonNull(event.getMember());
        var channels = event.getGuild().getTextChannels();
        return channels.stream()
                .filter(tc -> authorMember.getPermissions(tc).contains(Permission.MESSAGE_READ))
                .map(TextChannel::getIdLong)
                .collect(Collectors.toSet());
    }

    private void sendAccessUrl(PrivateChannel privateChannel, Guild server, Set<Long> channels) {
        privateChannel.sendMessageFormat("This is your single-use link <%s> \n it will expire in %s minutes!",
                imageStoreService.generateLink(server, channels), ImageStoreService.LINK_ID_EXPIRE_MINUTES).queue();
    }
}
