package fbanna.serverpassword.event;

import fbanna.serverpassword.config.Config;
import fbanna.serverpassword.state.LoginState;
import fbanna.serverpassword.state.LoginStates;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.UUID;

import static fbanna.serverpassword.ServerPassword.*;

public class LoginDialogEvent extends DialogEventImpl{


    public LoginDialogEvent(String id) {
        super(id);
    }

    @Override
    public void run(ServerboundCustomClickActionPacket packet, UUID uuid, Connection connection) {

        if (!WATCHEDPLAYERS.containsKey(uuid)){
            return; // fail
        }

        LoginState state = WATCHEDPLAYERS.get(uuid);



        if (!state.isWaitingResponse()) {
            state.onResponse(false); // idk
            return; // fail
        }


        Optional<Tag> oTag = packet.payload();

        if (oTag.isEmpty()) {
            state.onResponse(false);
            LOGGER.info("no data!");
            return; // fail
        }

        LOGGER.info(oTag.get().toString());


        // CHANGE THIS NOW!!!
        if (!(oTag.get() instanceof CompoundTag)) {
            LOGGER.info("bad packet!");
            state.onResponse(false);
            return;
        }

        CompoundTag tag = (CompoundTag) oTag.get();

        Optional<String> pass = tag.getString("password");

        if (pass.isEmpty()) {
            LOGGER.info("failed to get string");
            state.onResponse(false);
            return;
        }

        if (!pass.get().equals(Config.PASSWORD)) {
            LOGGER.info("bad password");
            state.onResponse(false);
            return;
        }

        LOGGER.info("correct password");
        state.onResponse(true);
        //state.joinFuture(true); // accept player on response

        return; // success

    }
}
