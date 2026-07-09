package fbanna.serverpassword.mixin;

import com.mojang.authlib.GameProfile;
import fbanna.serverpassword.ServerPassword;
import fbanna.serverpassword.state.LoginState;
import fbanna.serverpassword.state.LoginStates;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dialog.Dialog;
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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static fbanna.serverpassword.ServerPassword.*;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class MixinConfigurationFinished extends ServerCommonPacketListenerImpl {


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

        UUID id = this.gameProfile.id();


        if (!WATCHEDPLAYERS.containsKey(id)) {
            return;
        }

        LoginState state = WATCHEDPLAYERS.get(id);

        assert state.isWatchedLogin(); // check for debugging

        //state.setState(LoginStates.WAITING_RESPONSE);

        //WATCHEDPLAYERS.put(id, LoginStates.HALTED_LOGIN);

        Identifier dialogId = Identifier.fromNamespaceAndPath(MOD_ID, "login");
        Optional<Registry<Dialog>> dialogRegistry = this.server.registryAccess().lookup(Registries.DIALOG);

        if (dialogRegistry.isEmpty()) {
            LOGGER.info("no");
        } else {

            CompletableFuture<Boolean> response = new CompletableFuture<>();
            response.completeOnTimeout(false, 5, TimeUnit.SECONDS);

            state.setWaitingResponse(response);

            Optional<Holder.Reference<Dialog>> dialogEntry = dialogRegistry.get().get(dialogId);

            if (dialogEntry.isPresent()) {

                this.connection.send(new ClientboundShowDialogPacket(dialogEntry.get()));

            } else {
                LOGGER.error("Dialog {} not found in registry!", dialogId);
            }

            if(!response.join()) { // if they fail to provide the correct details
                this.connection.disconnect(Component.literal("Failed to provide correct server password"));
                //this.connection.send(new ClientboundShowDialogPacket());
            }

        }



        WATCHEDPLAYERS.remove(id);





    }

}
