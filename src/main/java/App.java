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
      model.put("recipeCategories", newRecipe.getCategories());
      model.put("categories", Category.all());
      model.put("template", "templates/recipe.vtl");
      model.put("recipe", newRecipe);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipe/:recipe_id", (request, response) ->  {
      Map<String, Object> model = new HashMap<String, Object>();
      int recipe_id = Integer.parseInt(request.params("recipe_id"));
      Recipe newRecipe = Recipe.find(recipe_id);
      model.put("recipeCategories", newRecipe.getCategories());
      model.put("categories", Category.all());
      model.put("recipe", newRecipe);
      model.put("template", "templates/recipe.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipe/:recipe_id", (request, response) ->  {
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("recipe_id")));
      String property = request.queryParams("updateRecipe");
      String propValue = request.queryParams("newRecipeValue");
      recipe.update(property, propValue);
      String url = String.format("/recipe/%d", recipe.getRecipeId());
      response.redirect(url);
      return null;
    });

    post("/recipe/:recipe_id/delete", (request, response) ->  {
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("recipe_id")));
      recipe.delete();
      response.redirect("/");
      return null;
    });

    post("/recipe/:recipe_id/rate", (request, response) ->  {
      Recipe newRecipe = Recipe.find(Integer.parseInt(request.params("recipe_id")));
      int rating = Integer.parseInt(request.queryParams("updateRecipe"));
      newRecipe.rate(rating);
      String url = String.format("/recipe/%d", Integer.parseInt(request.params("recipe_id")));
      response.redirect(url);
      return null;
    });

    post("/recipe/:recipe_id/assign", (request, response) ->{
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("recipe_id")));
      Category category = Category.find(Integer.parseInt(request.queryParams("recipeCategory")));
      recipe.tagRecipe(category);
      String url = String.format("/recipe/%d", recipe.getRecipeId());
      response.redirect(url);
      return null;
    }); 
  }
}
