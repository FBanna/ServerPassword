package fbanna.serverpassword.mixin;

import fbanna.serverpassword.task.ServerPasswordConfigurationTask;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class MixinStartConfiguration extends ServerCommonPacketListenerImpl {


    @Shadow
    @Final
    private Queue<ConfigurationTask> configurationTasks;

    private MixinStartConfiguration(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }


    @Inject(method = "startConfiguration", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerConfigurationPacketListenerImpl;addOptionalTasks()V", shift = At.Shift.AFTER))
    private void inject(CallbackInfo ci) {
        this.configurationTasks.add(new ServerPasswordConfigurationTask((ServerConfigurationPacketListenerImpl) (Object) this, this.server));
    }
}
