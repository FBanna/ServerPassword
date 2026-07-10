package fbanna.serverpassword.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import static fbanna.serverpassword.ServerPassword.MOD_ID;

public class Config {

    public static final String PASSWORD;
    //public static final boolean SPECTATOR_CAN_OPEN;

    static {
        final Properties properties = new Properties();
        final Properties newProperties = new Properties();
        final Path path = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".properties");

        if (Files.isRegularFile(path)) {
            try (InputStream in = Files.newInputStream(path, StandardOpenOption.CREATE)) {
                properties.load(in);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        PASSWORD = getString(properties, newProperties, "password", "change_me");

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            newProperties.store(out, "Configuration file for ServerPassword");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {

    }

    private static String getString(Properties properties, Properties newProperties, String key, String defaultValue) {

        String value = properties.getProperty(key);

        if (value == null) {
            newProperties.setProperty(key, defaultValue);
            return defaultValue;
        }


        newProperties.setProperty(key, value);
        return value;

    }

    private static boolean getBoolean(Properties properties, Properties newProperties, String key, boolean defaultValue) {

        String propertyString = properties.getProperty(key);

        if (propertyString == null) {
            newProperties.setProperty(key, Boolean.toString(defaultValue));
            return defaultValue;
        }

        final boolean value = Boolean.parseBoolean(propertyString);
        newProperties.setProperty(key, Boolean.toString(value));
        return value;

    }


}
