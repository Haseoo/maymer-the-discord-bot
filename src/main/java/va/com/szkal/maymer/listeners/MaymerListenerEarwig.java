package va.com.szkal.maymer.listeners;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import va.com.szkal.maymer.Env;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MaymerListenerEarwig extends ListenerAdapter {

    private final Set<String> earwigSet;
    private final String emoteName;

    public MaymerListenerEarwig(Env env) {
        earwigSet = new HashSet<>(Arrays.asList(env.getEarwigSecret().split(",")));
        emoteName = env.getEarwigSecretEmoteName();
    }


    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            return;
        }
        Message message = event.getMessage();
        String content = message.getContentRaw().toLowerCase();
        if (earwigSet.stream().anyMatch(content::contains)) {
            message.addReaction(event.getGuild().getEmotesByName(emoteName, true)
                    .get(0)).queue();
        }
    }
}
