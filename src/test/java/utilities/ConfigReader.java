package utilities;

import java.io.*;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void updateProperty(String key, String value) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.setProperty(key, value);
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
