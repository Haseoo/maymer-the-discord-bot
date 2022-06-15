package va.com.szkal.maymer.commands;

import net.dv8tion.jda.api.entities.Message;
import va.com.szkal.maymer.Env;
import va.com.szkal.maymer.minecraft.MinecraftRestDao;
import va.com.szkal.maymer.minecraft.MinecraftServerInfoDto;

import java.util.function.Consumer;

final class MinecraftCommand extends DeletableMessageCommand {

    private final MinecraftRestDao minecraftRestDao;
    private final Env env;

    public MinecraftCommand(Consumer<Message> msgDelete, MinecraftRestDao minecraftRestDao, Env env) {
        super(msgDelete);
        this.minecraftRestDao = minecraftRestDao;
        this.env = env;
    }


    @Override
    protected String execute(long serverId) {
        if (serverId != env.getServerId()) {
            return "Premium feature";
        }
        return minecraftRestDao.getServerStatus()
                .map(MinecraftServerInfoDto::toString)
                .orElse("Minecraft server status service ded :(");
    }
}
