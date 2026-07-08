package fbanna.serverpassword.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.config.PrepareSpawnTask;
import net.minecraft.server.players.NameAndId;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

import static fbanna.serverpassword.ServerPassword.*;

@Mixin(targets = "net.minecraft.server.network.config.PrepareSpawnTask$Ready")
public class MixinPrepareSpawn {

    @Shadow
    @Final
    private PrepareSpawnTask this$0;

    @Inject(method = "spawn", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = At.Shift.BEFORE, ordinal = 0), cancellable = true)
    private void inject(final Connection connection, final CommonListenerCookie cookie, CallbackInfoReturnable<ServerPlayer> cir, @Local(name = "player") ServerPlayer player) {

        if(WATCHEDPLAYERS.contains(player.getUUID())) {

            Identifier dialogId = Identifier.fromNamespaceAndPath(MOD_ID, "login");
            player.level().getServer().registries().getLayer(RegistryLayer.STATIC).get(Registries.DIALOG).get().
            var dialogRegistry = player.level().getServer().registryAccess().getOrThrow(Registries.DIALOG);
            dialogRegistry.
            var dialogEntry = dialogRegistry.getEntry(dialogId);

            if (dialogEntry.isPresent()) {
                player.openDialog(dialogEntry.get());
            } else {
                LOGGER.error("Dialog {} not found in registry!", dialogId);
            }


            cir.setReturnValue(player);
        }

    }
}
