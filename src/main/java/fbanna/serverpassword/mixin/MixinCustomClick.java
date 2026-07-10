package fbanna.serverpassword.mixin;

import com.mojang.authlib.GameProfile;
import fbanna.serverpassword.ServerPassword;
import fbanna.serverpassword.event.DialogEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class MixinCustomClick {

    @Shadow
    @Final
    protected Connection connection;

    @Shadow
    protected abstract GameProfile playerProfile();



    @Inject(method = "handleCustomClickAction", at = @At(value = "TAIL"))
    private void inject(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        DialogEvent.EVENT.invoker().interact(packet, this.playerProfile().id(), this.connection);
    }
}
