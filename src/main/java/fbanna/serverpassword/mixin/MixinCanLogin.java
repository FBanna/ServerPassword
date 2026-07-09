package fbanna.serverpassword.mixin;


import fbanna.serverpassword.ServerPassword;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

import static fbanna.serverpassword.ServerPassword.LOGGER;
import static fbanna.serverpassword.ServerPassword.WATCHEDPLAYERS;

@Mixin(PlayerList.class)
public class MixinCanLogin {

    @Inject(method = "canPlayerLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", shift = At.Shift.BEFORE), cancellable = true)
    private void inject(final SocketAddress address, final NameAndId nameAndId, CallbackInfoReturnable<Component> cir) {

        WATCHEDPLAYERS.add(nameAndId.id());
        LOGGER.info("watching player");
        cir.setReturnValue(null);

    }
}
