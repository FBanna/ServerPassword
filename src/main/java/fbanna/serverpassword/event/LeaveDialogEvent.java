package fbanna.serverpassword.event;

import fbanna.serverpassword.state.LoginState;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;

import java.util.UUID;

import static fbanna.serverpassword.ServerPassword.WATCHEDPLAYERS;

public class LeaveDialogEvent extends DialogEventImpl{

    public LeaveDialogEvent(String id) {
        super(id);
    }


    @Override
    public void run(ServerboundCustomClickActionPacket packet, UUID uuid, Connection connection) {

        if (!WATCHEDPLAYERS.containsKey(uuid)){
            return; // fail
        }

        LoginState state = WATCHEDPLAYERS.get(uuid);

        if (!state.isWaitingResponse()) {
            return; // fail
        }

        state.onResponse(false);
    }

}
