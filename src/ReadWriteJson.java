import jodd.json.JsonSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Reads a JSON file for messages for users before user opens their account.
 * Writes data into a data file whenever user executes a command successfully.
 */
public class ReadWriteJson extends User {

    static public void writeToJson() throws IOException {
        JsonSerializer js = new JsonSerializer();
        File messagesFile = new File("src/messages.json");
        FileWriter fw = new FileWriter(messagesFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        String messageJson = js.include("*").serialize(Main.messages);
        bw.write(messageJson);

        bw.close();
    }
}
