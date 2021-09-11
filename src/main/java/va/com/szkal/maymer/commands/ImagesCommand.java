package va.com.szkal.maymer.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import va.com.szkal.maymer.images.ImageStoreService;

@RequiredArgsConstructor
final class ImagesCommand implements MaymerCommand {
    private final ImageStoreService imageStoreService;

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getMessage().getAuthor()
                .openPrivateChannel()
                .queue(privateChannel -> sendAccessUrl(privateChannel,
                        event.getGuild()));
        event.getMessage().delete().queue();
    }

    private void sendAccessUrl(PrivateChannel privateChannel, Guild server) {
        privateChannel.sendMessageFormat("To jest Twój jednorazowy link <%s> \n wygaśnie za %s minut!",
                imageStoreService.generateLink(server), ImageStoreService.LINK_ID_EXPIRE_MINUTES).queue();
    }
}
