package fbanna.serverpassword.event;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static fbanna.serverpassword.ServerPassword.MOD_ID;

public abstract class DialogEventImpl implements DialogEvent{

    protected Identifier id;

    public DialogEventImpl(String id) {
        this.id = Identifier.fromNamespaceAndPath(MOD_ID, id);
        this.register();
    }

    @Override
    public void interact(ServerboundCustomClickActionPacket packet, UUID uuid, Connection connection) {
        if(packet.id().equals(this.id)){
            this.run(packet,uuid,connection);
        }
    }

    public abstract void run(ServerboundCustomClickActionPacket packet, UUID uuid, Connection connection);
}
