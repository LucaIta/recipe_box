import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;


public class App {

  public static void main (String[] args){

    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) ->  {
      Map <String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("categories", Category.all());
      model.put("recipes", Recipe.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipe/new", (request, response) ->  {
    Map <String, Object> model = new HashMap<String, Object>();
    model.put("template", "templates/recipe-form.vtl");
    return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipe", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String recipeName = request.queryParams("recipeName");
      String instructions = request.queryParams("recipeInstructions");

      Recipe newRecipe = new Recipe(recipeName, instructions);
      newRecipe.save();
      
      model.put("template", "templates/recipe.vtl");
      model.put("recipe", newRecipe);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }
}
