package fbanna.serverpassword.event;

import fbanna.serverpassword.ServerPassword;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface LoginEvent {

    Event<LoginEvent> EVENT = EventFactory.createArrayBacked(LoginEvent.class,
            (listeners) -> (player, password) -> {

                ServerPassword.LOGGER.info("all good");

                for (LoginEvent listener: listeners) {
                    InteractionResult result = listener.interact(player, password);
                    if ( result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;

            });

    InteractionResult interact(Player player, String password);

}
