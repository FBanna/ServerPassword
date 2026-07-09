package fbanna.serverpassword.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.config.PrepareSpawnTask;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static fbanna.serverpassword.ServerPassword.LOGGER;
import static fbanna.serverpassword.ServerPassword.MOD_ID;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public class MixinConfigurationFinished extends ServerCommonPacketListenerImpl {


    @Shadow
    private @Nullable PrepareSpawnTask prepareSpawnTask;

    @Shadow
    @Final
    private GameProfile gameProfile;

    private MixinConfigurationFinished(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }


    @Inject(method = "handleConfigurationFinished", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/config/PrepareSpawnTask;spawnPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/network/CommonListenerCookie;)Lnet/minecraft/server/level/ServerPlayer;", shift = At.Shift.BEFORE))
    private void inject(ServerboundFinishConfigurationPacket packet, CallbackInfo ci) {



        Identifier dialogId = Identifier.fromNamespaceAndPath(MOD_ID, "login");

        //layer.level().getServer().registries().getAccessFrom(RegistryLayer.RELOADABLE).get(R)

        var dialogRegistry = this.server.registryAccess().lookup(Registries.DIALOG);

        if (dialogRegistry.isEmpty()) {
            LOGGER.info("no");
        }

        CompletableFuture<Boolean> response = new CompletableFuture<>();
        response.completeOnTimeout(false, 1, TimeUnit.MINUTES);

        var dialogEntry = dialogRegistry.get().get(dialogId);
        //var dialogEntry = dialogRegistry.getEntry(dialogId);

        if (dialogEntry.isPresent()) {

            this.connection.send(new ClientboundShowDialogPacket(dialogEntry.get()));

        } else {
            LOGGER.error("Dialog {} not found in registry!", dialogId);
        }

    }

    @Override
    protected GameProfile playerProfile() {
        return null;
    }

    @Override
    public void handleClientInformation(ServerboundClientInformationPacket packet) {

    }

    @Override
    public ConnectionProtocol protocol() {
        return null;
    }

    @Override
    public boolean isAcceptingMessages() {
        return false;
    }
}
