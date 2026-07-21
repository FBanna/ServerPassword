package fbanna.serverpassword;

import fbanna.serverpassword.config.Config;
import fbanna.serverpassword.dialog.LoginDialog;
import fbanna.serverpassword.event.DialogEvent;
import fbanna.serverpassword.event.LeaveDialogEvent;
import fbanna.serverpassword.event.LoginDialogEvent;
import fbanna.serverpassword.state.LoginState;
import fbanna.serverpassword.state.LoginStates;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.world.InteractionResult;
import net.vampirestudios.packwright.api.PackwrightCallback;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;

public class ServerPassword implements ModInitializer {
	public static final String MOD_ID = "serverpassword";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public final static HashMap<UUID, LoginState> WATCHEDPLAYERS = new HashMap<>();


	@Override
	public void onInitialize() {
		Config.init(); // init config

		// register dialog

		RuntimeResourcePack pack = RuntimeResourcePack.create("%s:login_dialog".formatted(MOD_ID));
		pack.addDataPackMcmeta("Dialogs for ServerPassword");
		LoginDialog.register(pack);

		// DEBUGGING
		//pack.dump();

		PackwrightCallback.BEFORE_VANILLA.register(resources -> resources.add(pack));


		// register dialog events
		new LoginDialogEvent("login_event");
		new LeaveDialogEvent("leave_event");


		// Watch leaving players
		ServerConfigurationConnectionEvents.DISCONNECT.register(((listener, server) -> {
			WATCHEDPLAYERS.remove(listener.getOwner().id());
		}));

		// Loading message
		ModContainer container = FabricLoader.getInstance().getModContainer(MOD_ID)
				.orElseThrow(() -> new RuntimeException("Could not get the ServerPassword mod container."));

		LOGGER.info("Initialised ServerPassword version {}!", container.getMetadata().getVersion().getFriendlyString());


	}
}
