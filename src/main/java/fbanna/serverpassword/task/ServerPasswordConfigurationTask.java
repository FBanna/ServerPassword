package fbanna.serverpassword.task;

import fbanna.serverpassword.ServerPassword;
import fbanna.serverpassword.state.LoginState;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundClearDialogPacket;
import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteListEntry;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static fbanna.serverpassword.ServerPassword.*;

public class ServerPasswordConfigurationTask implements ConfigurationTask {

    private static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_password_event");

    private static final ExecutorService CONFIGURATION_POOL = Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual().name("Configuration Thread #", 0).uncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER)).factory()
    );

    private ServerConfigurationPacketListenerImpl listener;
    private MinecraftServer server;


    public ServerPasswordConfigurationTask(ServerConfigurationPacketListenerImpl listener, MinecraftServer server) {
        this.listener = listener;
        this.server = server;
    }

    @Override
    public void start(Consumer<Packet<?>> connection) {


        NameAndId nameAndId = new NameAndId(this.listener.getOwner());

        if (!WATCHEDPLAYERS.containsKey(nameAndId.id())) {
            this.listener.completeTask(TYPE);
            return;
        }

        CONFIGURATION_POOL.execute(() -> {

            LoginState state = WATCHEDPLAYERS.get(nameAndId.id());
            assert state.isWatchedLogin(); // check for debugging

            Identifier dialogId = Identifier.fromNamespaceAndPath(MOD_ID, "login");
            Optional<Registry<Dialog>> dialogRegistry = this.server.registryAccess().lookup(Registries.DIALOG);

            if (dialogRegistry.isEmpty()) {

                LOGGER.error("Could not find dialog registry!");
                this.listener.completeTask(TYPE);
                return;
            }


            Optional<Holder.Reference<Dialog>> dialogEntry = dialogRegistry.get().get(dialogId);

            if (dialogEntry.isEmpty()) {
                LOGGER.error("Dialog {} not found in registry!", dialogId);
                this.listener.completeTask(TYPE);
                return;
            }

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            future.completeOnTimeout(false, 1, TimeUnit.MINUTES);



            state.setWaitingResponse(future);

            this.listener.send(new ClientboundShowDialogPacket(dialogEntry.get()));


            if (future.join()) {
                LOGGER.info("correct password!");
                this.server.getPlayerList().getWhiteList().add(new UserWhiteListEntry(nameAndId)); // add player to whitelist
            } else { // or kick
                LOGGER.info("you failed!");

                this.listener.send(ClientboundClearDialogPacket.INSTANCE);
                this.listener.disconnect(Component.literal("Failed to provide correct server password"));
            }

            WATCHEDPLAYERS.remove(nameAndId.id());



            this.listener.completeTask(TYPE);
        });
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
