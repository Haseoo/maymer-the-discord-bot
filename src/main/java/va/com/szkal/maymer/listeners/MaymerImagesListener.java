package va.com.szkal.maymer.listeners;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import va.com.szkal.maymer.images.ImageStoreService;
import va.com.szkal.maymer.images.StoreImageRequest;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MaymerImagesListener extends ListenerAdapter {

    private final ImageStoreService imageStoreService;

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        var imagesAttachments = getImagesFromMessage(event.getMessage());
        if (imagesAttachments.isEmpty()) {
            return;
        }
        for (Message.Attachment image : imagesAttachments) {
            var imageRequest = StoreImageRequest.builder()
                    .messageId(event.getMessageIdLong())
                    .username(event.getAuthor().getName())
                    .serverId(event.getGuild().getIdLong())
                    .channelName(event.getChannel().getName())
                    .channelId(event.getChannel().getIdLong())
                    .sendTime(event.getMessage().getTimeCreated().toLocalDateTime())
                    .url(image.getUrl())
                    .build();
            imageStoreService.store(imageRequest);
        }
    }

    private List<Message.Attachment> getImagesFromMessage(Message message) {
        return message.getAttachments().stream()
                .filter(Message.Attachment::isImage)
                .collect(Collectors.toList());
    }

    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
        super.onTextChannelDelete(event);
        imageStoreService.deleteChannel(event.getChannel().getIdLong());
    }

    @Override
    public void onTextChannelUpdateName(@Nonnull TextChannelUpdateNameEvent event) {
        super.onTextChannelUpdateName(event);
        imageStoreService.updateChannelName(event.getChannel().getIdLong(), event.getNewName());
    }
}
