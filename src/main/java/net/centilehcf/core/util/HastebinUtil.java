package net.centilehcf.core.util;

import lombok.experimental.UtilityClass;
import net.minecraft.util.com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class HastebinUtil {

    private String HASTEBIN_POST_URL = "https://hastebin.com/documents";

    private String HASTEBIN_URL = "https://hastebin.com/";

    public String paste(String content) {
        try {
            byte[] data = content.getBytes(StandardCharsets.UTF_8);
            URL hasteURL = new URL(HASTEBIN_POST_URL);
            HttpsURLConnection connection = (HttpsURLConnection) hasteURL.openConnection();

            connection.setRequestMethod("POST"); // We want to POST the paste data
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length + "");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(data); // Write the content to the output stream
            // Cleanup
            dataOutputStream.flush();
            dataOutputStream.close();

            // Receive the response from hastebin
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseUrl = new JsonParser().parse(reader.readLine()).getAsJsonObject().get("key").getAsString();
            reader.close();
            return HASTEBIN_URL + responseUrl;
        } catch (Exception ex) { // Broad catch statement to handle anything that could've gone wrong
            ex.printStackTrace();
            return "Couldn't paste! Error: " + ex.getCause() + '!';
        }
    }
}
