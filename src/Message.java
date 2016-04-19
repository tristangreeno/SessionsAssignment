/**
 * Stores the index of each message, and its content.
 */
public class Message {
    String message;
    Integer index;

    public Message(String message, Integer index){
        this.message = message + "\n\n";
        this.index = index;
    }
}
