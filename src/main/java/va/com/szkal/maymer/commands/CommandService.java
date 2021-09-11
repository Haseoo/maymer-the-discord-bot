package va.com.szkal.maymer.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Service;
import va.com.szkal.maymer.Env;
import va.com.szkal.maymer.images.ImageStoreService;
import va.com.szkal.maymer.meme.MemeRepository;
import va.com.szkal.maymer.minecraft.MinecraftRestDao;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandService {
    private final MemeRepository memeRepository;
    private final ScheduledExecutorService msgDelScheduler;
    private final ImageStoreService imageStoreService;
    private final MinecraftRestDao minecraftRestDao;
    private final Env env;

    public MaymerCommand parseCommand(String command) {
        var args = Arrays.stream(command.split(" ")).skip(1).collect(Collectors.toList());
        switch (args.size()) {
            case 1:
                var arg = args.get(0);
                switch (arg) {
                    case "list":
                        return new ListCommand(this::deleteMsgAfter, memeRepository, env);
                    case "ranking":
                        return new RankingCommand(this::deleteMsgAfter, memeRepository);
                    case "images":
                        return new ImagesCommand(imageStoreService);
                    case "minecraft":
                        return new MinecraftCommand(this::deleteMsgAfter, minecraftRestDao, env);
                    default:
                        return new MemeCommand(arg, memeRepository);
                }
            case 2: {
                var arg1 = args.get(0);
                if (arg1.equals("delete")) {
                    return new DeleteCommand(this::deleteMsgAfter, args, memeRepository);
                }
                break;
            }
            case 3: {
                var arg1 = args.get(0);
                if (arg1.equals("add")) {
                    return new AddCommand(this::deleteMsgAfter, args, memeRepository);
                } else if (arg1.equals("update")) {
                    return new UpdateCommand(this::deleteMsgAfter, args, memeRepository);
                }
                break;
            }
            default:
                return new HelpCommand(this::deleteMsgAfter);
        }
        return new HelpCommand(this::deleteMsgAfter);
    }

    private void deleteMsgAfter(Message msg) {
        msgDelScheduler.schedule(() -> msg.delete().queue(), env.getMessageDeletionTimeout(), TimeUnit.SECONDS);
    }
}
