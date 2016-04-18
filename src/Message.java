/**
 * Created by tristangreeno on 4/18/16.
 */
public class Message {
    String message;

    public Message(String message){
        this.message = message + "\n\n";
    }

    @Override
    public String toString() {
        return message;
    }
}
