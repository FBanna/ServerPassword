package fbanna.serverpassword.config;

import net.fabricmc.loader.api.FabricLoader;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Properties;

import static fbanna.serverpassword.ServerPassword.LOGGER;
import static fbanna.serverpassword.ServerPassword.MOD_ID;

public class Config {

    private static final String PASSWORD;

    private static byte[] HASHEDPASSWORD;

    private static byte[] SALT;
    private static final String FUNCTION = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65536;
    private static final int SALT_LEN = 16;
    private static final int HASH_LEN = 256;


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
        // generate password hash

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LEN];
        random.nextBytes(salt);

        SALT = salt;

        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, ITERATIONS, HASH_LEN);


        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(FUNCTION);

            byte[] hash = factory.generateSecret(spec).getEncoded();
            HASHEDPASSWORD = hash;
            //LOGGER.info(Arrays.toString(hash));
        } catch(Exception e) {
            LOGGER.error("error loading function");
        }


    }

    public static boolean comparePassword(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, ITERATIONS, HASH_LEN);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(FUNCTION);

            byte[] hash = factory.generateSecret(spec).getEncoded();

            int accum = 0;

            for (int i = 0; i < HASHEDPASSWORD.length; i++)
                accum |= (HASHEDPASSWORD[i] ^ hash[i]);

            return accum == 0;


        } catch(Exception e) {
            LOGGER.error("error loading function");
            return false;
        }
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
