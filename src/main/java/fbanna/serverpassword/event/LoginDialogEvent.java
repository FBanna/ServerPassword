package fbanna.serverpassword.event;

import fbanna.serverpassword.state.LoginState;
import fbanna.serverpassword.state.LoginStates;
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

        LOGGER.info("we are running this bad boy");
        if (!WATCHEDPLAYERS.containsKey(uuid)){
            return; // fail
        }

        LoginState state = WATCHEDPLAYERS.get(uuid);

        if (!state.isWaitingResponse()) {
            return; // fail
        }


        Optional<Tag> oTag = packet.payload();

        if (oTag.isEmpty()) {
            LOGGER.info("no data!");
            return; // fail
        }

        LOGGER.info(oTag.get().toString());
        state.onResponse(true);
        //state.joinFuture(true); // accept player on response

        return; // success

    }
}
