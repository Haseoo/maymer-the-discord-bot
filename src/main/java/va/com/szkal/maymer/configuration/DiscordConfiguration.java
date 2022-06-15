package va.com.szkal.maymer.configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import va.com.szkal.maymer.Env;
import va.com.szkal.maymer.commands.CommandService;
import va.com.szkal.maymer.images.ImageStoreService;
import va.com.szkal.maymer.listeners.MaymerImagesListener;
import va.com.szkal.maymer.listeners.MaymerListener;
import va.com.szkal.maymer.listeners.MaymerListenerEarwig;
import va.com.szkal.maymer.listeners.MaymerListenerWell;

@Configuration
@RequiredArgsConstructor
public class DiscordConfiguration {
    private final CommandService commandService;
    private final ImageStoreService imageStoreService;
    private final Env env;
    private JDA jda;


    @EventListener(ContextRefreshedEvent.class)
    @SneakyThrows
    public void contextRefreshedEvent() {
        jda = JDABuilder.createDefault(env.getBotToken()).build();
        jda.addEventListener(new MaymerListener(commandService),
                new MaymerListenerEarwig(env),
                new MaymerListenerWell(),
                new MaymerImagesListener(imageStoreService));
        jda.addEventListener();
        jda.getPresence().setActivity(env.isProd() ?
                Activity.playing("Your mom") : Activity.watching("in development"));
    }

    @EventListener(ContextClosedEvent.class)
    @SneakyThrows
    public void contextStoppedEvent() {
        if (jda != null) {
            jda.shutdown();
            jda = null;
        }
    }
}
