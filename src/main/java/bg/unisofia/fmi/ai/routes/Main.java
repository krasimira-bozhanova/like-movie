package bg.unisofia.fmi.ai.routes;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.ModelAndView;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;

public class Main {

	public static void main(String[] args) throws IOException {
		staticFileLocation("/web");

		List<Movie> movies = new ArrayList<Movie>();
		movies.add(new Movie(1));
		movies.add(new Movie(2));

		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");
			attributes.put("movies", movies);

			return new ModelAndView(attributes, "index.ftl");
		}, new FreeMarkerEngine());

		get("/register", (request, response) -> {
		    Map<String, Object> attributes = new HashMap<>();
		    attributes.put("message", "Register");
		    return new ModelAndView(attributes, "register.ftl");

		}, new FreeMarkerEngine());

		post("/register", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String passwordRepeat = request.queryParams("repeat_password");
            String message;
            try {
                User.registerUser(username, password, passwordRepeat);
            } catch (Exception e) {
                response.redirect("/register");
            }
            response.redirect("/");
            return request;
        });

		get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Login");
            return new ModelAndView(attributes, "login.ftl");

        }, new FreeMarkerEngine());

		get("/preview", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "preview");
            return new ModelAndView(attributes, "preview.ftl");

        }, new FreeMarkerEngine());

	}
}