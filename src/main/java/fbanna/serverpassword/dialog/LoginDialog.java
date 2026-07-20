package fbanna.serverpassword.dialog;


import fbanna.serverpassword.config.Config;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.registry.dialog.Action;
import net.vampirestudios.packwright.data.registry.dialog.Body;
import net.vampirestudios.packwright.data.registry.dialog.Dialog;
import net.vampirestudios.packwright.data.registry.dialog.Input;

import static fbanna.serverpassword.ServerPassword.MOD_ID;

public class LoginDialog {

    private static Identifier modResource(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Dialog buildLoginDialog() {

        return Dialog.multiAction("Login",

                Dialog.Button.button("Login")
                        .action(Action.dynamicCustom(modResource("login")))
                )
//                Dialog.Button.button("Leave")
//                        .action(Action.custom(modResource("leave")))
//                )
                .input(Input.text("password", "Enter Password"))
                .plainMessage(Config.MESSAGE)
                .exitAction(
                        Dialog.Button.button("Leave")
                                .action(Action.custom(modResource("leave")))
                );


    }

    public static void register(RuntimeResourcePack pack) {
        pack.addDialog(modResource("dialogs/login"), buildLoginDialog());
    }
}
