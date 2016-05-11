import org.sql2o.*;
import java.util.List;


public class Recipe {
  private String instructions;
  private String recipe_name;
  private int rating;
  private int id;


  public Recipe(String recipe_name, String instructions){
    this.recipe_name = recipe_name;
    this.instructions = instructions;
  }

  public String getRecipeName() {
    return recipe_name;
  }

  public String getInstructions() {
    return instructions;
  }

  public int getRecipeId() {
    return id;
  }

  public int getRating() {
    return rating;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (recipe_name, instructions) VALUES (:recipe_name, :instructions)";
      this.id = (int) con.createQuery(sql, true).addParameter("recipe_name", this.getRecipeName()).addParameter("instructions", this.getInstructions()).executeUpdate().getKey();
    }
  }

  public static List<Recipe> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes";
      List<Recipe> recipes = con.createQuery(sql).executeAndFetch(Recipe.class);
      return recipes;
    }
  }

  public static Recipe find(int recipe_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE id = :recipe_id";
      return con.createQuery(sql).addParameter("recipe_id", recipe_id).executeAndFetchFirst(Recipe.class);
    }
  }

  @Override
  public boolean equals(Object object) {
    if(!(object instanceof Recipe)){
      return false;
    } else {
      Recipe thisRecipe = (Recipe) object;
      return this.getRecipeName().equals(thisRecipe.getRecipeName()) && this.getInstructions().equals(thisRecipe.getInstructions()) && this.getRecipeId() == thisRecipe.getRecipeId() && this.getRating() == thisRecipe.getRating();
    }
  }

  public void update(String column_name, String column_value){
    try (Connection con = DB.sql2o.open()){
      String sql = "UPDATE recipes SET " + column_name + " = :column_value WHERE id = :recipe_id";
      con.createQuery(sql).addParameter("column_value", column_value).addParameter("recipe_id", this.getRecipeId()).executeUpdate();
    }

  }



}
