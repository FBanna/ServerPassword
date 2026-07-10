package fbanna.serverpassword.event;

import fbanna.serverpassword.ServerPassword;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface DialogEvent {

    Event<DialogEvent> EVENT = EventFactory.createArrayBacked(DialogEvent.class,
            (DialogEvent[] listeners) -> (packet, uuid,connection) -> {

                for (DialogEvent listener: listeners) {
                    listener.interact(packet, uuid, connection);
                }

            });

    void interact(ServerboundCustomClickActionPacket packet, UUID uuid, Connection connection);

    default void register(){
        DialogEvent.EVENT.register(this);
    }

}
