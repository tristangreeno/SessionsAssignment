import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;
import java.util.Map;

/**
 * This program is designed to read data about messages from a JSON of user info, and then present that information
 * in a list to the user. The user may edit, delete, or add a new message when logged in.
 * If the user's name does not match the password it registered with, then the user will not be able to do anything
 * but try to log in again.
 * All messages are written to a JSON after being edited, deleted, or added.
 * Logging out will require the user to log in again.
 */

public class Main {
    static HashMap<String, User> users = new HashMap<>();
    static HashMap<Message, User> messages = new HashMap<>();

    public static void main(String[] args){
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Map map = new HashMap();

                    Session session = request.session();
                    String userName = session.attribute("userName");
                    String password = session.attribute("userPassword");
                    User user = users.get(userName);

                    if(user == null){
                        return new ModelAndView(map, "index.html");
                    }
                    else if(! user.checkPassword(userName, password)){
                        map.put("error", "Incorrect password.");
                        return new ModelAndView(map, "index.html");
                    }
                    else {
                        map.put("name", user.getName());
                        map.put("messages", user.messages);
                        return new ModelAndView(map, "homepage.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("loginPassword");
                    User user = users.get(name);

                    if(user == null){
                        users.put(name, new User(name, password));
                    }

                    Session session = request.session();
                    session.attribute("userName", name);
                    session.attribute("userPassword", password);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-message",
                ((request, response) -> {
                    Session session = request.session();
                    Integer index = Integer.parseInt(request.queryParams("messageNumber"));
                    String name = session.attribute("userName");
                    User user = users.get(name);

                    try {
                        user.messages.remove(index - 1);
                    }catch (IndexOutOfBoundsException e){
                        response.redirect("/");
                        return "";
                    }

                    messages.put(user.messages.get(index - 1), user);
                    ReadWriteJson.writeToJson();

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit-message",
                ((request, response) -> {
                    Session session = request.session();
                    Integer index = Integer.parseInt(request.queryParams("messageIndex")) - 1;
                    String newMessage = request.queryParams("editedMessage");
                    String name = session.attribute("userName");
                    User user = users.get(name);

                    try{
                        user.messages.remove((int) index);
                        user.messages.add(index, new Message(newMessage, index + 1));
                    }catch (IndexOutOfBoundsException e){
                        response.redirect("/");
                        return "";
                    }

                    messages.put(user.messages.get(index), user);
                    ReadWriteJson.writeToJson();

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    String message = request.queryParams("userMessage");

                    User user = users.get(name);

                    user.messages.add(new Message(message, user.index++));
                    session.attribute("userMessage", message);

                    messages.put(session.attribute("userMessage"), user);
                    ReadWriteJson.writeToJson();

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }
}
