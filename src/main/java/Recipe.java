import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;


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

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM recipes WHERE id = :recipe_id";
      String sql2 = "DELETE FROM recipes_categories WHERE recipe_id = :recipe_id";
      con.createQuery(sql).addParameter("recipe_id", this.getRecipeId()).executeUpdate();
      con.createQuery(sql2).addParameter("recipe_id", this.getRecipeId()).executeUpdate();
    }
  }

  public void rate(int rating) {
    this.rating = rating;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE recipes SET rating = :rating WHERE id = :id";
      con.createQuery(sql).addParameter("rating", rating).addParameter("id", this.getRecipeId()).executeUpdate();
    }
  }

  public void tagRecipe(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes_categories (recipe_id, category_id) VALUES (:recipe_id, :category_id)";
      con.createQuery(sql).addParameter("recipe_id", this.getRecipeId()).addParameter("category_id", category.getCategoryId()).executeUpdate();
    }
  }

  public void addIngredient(Ingredient ingredient){
    try (Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO recipes_ingredients (recipe_id, ingredient_id) VALUES (:recipe_id, :ingredient_id)";
      con.createQuery(sql).addParameter("recipe_id", this.getRecipeId()).addParameter("ingredient_id", ingredient.getId()).executeUpdate();
    }
  }

  public List<Category> getCategories() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT category_id FROM recipes_categories WHERE recipe_id = :recipe_id";
      List<Integer> category_ids = con.createQuery(sql).addParameter("recipe_id", this.getRecipeId()).executeAndFetch(Integer.class);

      List<Category> categories = new ArrayList<Category>();

      for(Integer category_id : category_ids) {
        String joinQuery = "SELECT * FROM categories WHERE id=:category_id";
        Category category = con.createQuery(joinQuery).addParameter("category_id", category_id).executeAndFetchFirst(Category.class);
        categories.add(category);
      }

      return categories;
    }
  }

  public List<Ingredient> getIngredients(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT ingredient_id FROM recipes_ingredients WHERE recipe_id = :recipe_id";
      List<Integer> ingredient_ids = con.createQuery(sql).addParameter("recipe_id", this.getRecipeId()).executeAndFetch(Integer.class);

      List<Ingredient> ingredients = new ArrayList<Ingredient>();
      for (Integer ingredient_id : ingredient_ids) {
        String joinQuery = "SELECT * FROM ingredients WHERE id = :ingredient_id";
        Ingredient ingredient = con.createQuery(joinQuery).addParameter("ingredient_id", ingredient_id).executeAndFetchFirst(Ingredient.class);
        ingredients.add(ingredient);
      }
      return ingredients;
    }
  }

  public static List<Recipe> getRecipesSorted(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM recipes ORDER BY rating";
      List<Recipe> recipes = con.createQuery(sql).executeAndFetch(Recipe.class);
      return recipes;
    }
  }



}
