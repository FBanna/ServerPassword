package fbanna.serverpassword.mixin;

import com.mojang.authlib.GameProfile;
import fbanna.serverpassword.state.LoginState;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.server.players.NameAndId;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static fbanna.serverpassword.ServerPassword.LOGGER;
import static fbanna.serverpassword.ServerPassword.WATCHEDPLAYERS;


@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class MixinFinishConnectionSetup{


    @Shadow
    private @Nullable GameProfile authenticatedProfile;

    @Shadow
    @Final
    private MinecraftServer server;

    @ModifyVariable(method = "verifyLoginAndFinishConnectionSetup", at = @At(value = "STORE"), name = "error")
    private Component inject(Component error) {

        if (error == null) {
            return null;
        }

        NameAndId nameAndId = new NameAndId(this.authenticatedProfile); // always non null

        if (!this.server.getPlayerList().isWhiteListed(nameAndId)) {
            WATCHEDPLAYERS.put(nameAndId.id(), new LoginState());
            return null;
        }

        return error;
    }
}
