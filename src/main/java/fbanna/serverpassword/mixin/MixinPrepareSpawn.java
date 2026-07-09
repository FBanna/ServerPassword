package fbanna.serverpassword.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static fbanna.serverpassword.ServerPassword.*;

@Mixin(targets = "net.minecraft.server.network.config.PrepareSpawnTask$Ready")
public class MixinPrepareSpawn {

    @Shadow
    @Final
    private PrepareSpawnTask this$0;

    @Shadow
    @Final
    private ServerLevel spawnLevel;

    @Inject(method = "spawn", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = At.Shift.BEFORE, ordinal = 0), cancellable = true)
    private void inject(final Connection connection, final CommonListenerCookie cookie, CallbackInfoReturnable<ServerPlayer> cir, @Local(name = "player") ServerPlayer player) {

        if(WATCHEDPLAYERS.contains(player.getUUID())) {


//
//            if(!response.join()) {
//                player.
//            }
//
//
//            cir.setReturnValue(player);
        }

    }
}
