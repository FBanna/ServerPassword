package fbanna.serverpassword;

import fbanna.serverpassword.config.Config;
import fbanna.serverpassword.event.DialogEvent;
import fbanna.serverpassword.event.LoginDialogEvent;
import fbanna.serverpassword.state.LoginState;
import fbanna.serverpassword.state.LoginStates;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ServerPassword implements ModInitializer {
	public static final String MOD_ID = "serverpassword";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public final static HashMap<UUID, LoginState> WATCHEDPLAYERS = new HashMap<>();



	@Override
	public void onInitialize() {
		Config.init();

		LOGGER.info("Initialised ServerPassword!");

		new LoginDialogEvent("login");

		ServerConfigurationConnectionEvents.DISCONNECT.register(((listener, server) -> {
			WATCHEDPLAYERS.remove(listener.getOwner().id());
		}));

	}
}
