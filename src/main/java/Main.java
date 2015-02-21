import static spark.Spark.get;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bg.unisofia.fmi.ai.template.FreeMarkerEngine;
import spark.ModelAndView;
import static spark.Spark.*;

public class Main {

	public static void main(String[] args) throws IOException {
		staticFileLocation("/web");

		get("/hello", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");

			return new ModelAndView(attributes, "index.ftl");
		}, new FreeMarkerEngine());

	}
}