import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static HashMap<String, User> users = new HashMap<>();
    static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args){
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Map map = new HashMap();

                    Session session = request.session();
                    String userName = session.attribute("userName");
                    User user = users.get(userName);

                    if(user == null){
                        return new ModelAndView(map, "index.html");
                    }else{
                        map.put("name", user.name);
                        map.put("messages", messages);
                        return new ModelAndView(map, "homepage.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    User user = users.get(name);

                    if(user == null){
                        users.put(name, new User(name));
                    }

                    Session session = request.session();
                    session.attribute("userName", name);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String message = request.queryParams("userMessage");
                    messages.add(new Message(message));

                    Session session = request.session();
                    session.attribute("userMessage", message);

                    response.redirect("/");
                    return "";
                })
        );
    }
}
