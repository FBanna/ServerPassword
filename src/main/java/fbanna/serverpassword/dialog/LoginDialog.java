package fbanna.serverpassword.dialog;


import fbanna.serverpassword.config.Config;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.registry.dialog.Action;
import net.vampirestudios.packwright.data.registry.dialog.Dialog;
import net.vampirestudios.packwright.data.registry.dialog.Input;

import static fbanna.serverpassword.ServerPassword.MOD_ID;


// note for if packwright is not maintained - https://gist.github.com/sylvxa/4e3b18cd6957c49315e74f1dc0101c92

public class LoginDialog {

    private static Identifier modResource(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Dialog buildLoginDialog() {

        return Dialog.multiAction("Login",
                        Dialog.Button.button("Login")
                                .action(Action.dynamicCustom(modResource("login_event")))
                )
                .input(Input.text("password", "Enter Password"))
                .plainMessage(Config.MESSAGE)
                .pause(false)
                .canCloseWithEscape(false)
                .exitAction(
                        Dialog.Button.button("Leave")
                                .action(Action.custom(modResource("leave_event")))
                );
    }

    public static void register(RuntimeResourcePack pack) {
        pack.addDialog(modResource("login"), buildLoginDialog());
    }
}
