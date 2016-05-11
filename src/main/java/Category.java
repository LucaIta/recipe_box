import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;


public class Category {
  private String category_name;
  private int id;

  public Category(String category_name) {
    this.category_name = category_name;
  }

  public String getCategoryName() {
    return category_name;
  }

  public int getCategoryId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories (category_name) VALUES (:category_name)";
      this.id = (int) con.createQuery(sql, true).addParameter("category_name", this.getCategoryName()).executeUpdate().getKey();
    }
  }

  public static List<Category> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories";
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  public static Category find(int category_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories WHERE id = :category_id";
      return con.createQuery(sql).addParameter("category_id", category_id).executeAndFetchFirst(Category.class);
    }
  }

  @Override
  public boolean equals(Object otherCategory) {
    if (!(otherCategory instanceof Category)){
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getCategoryName().equals(newCategory.getCategoryName()) && this.getCategoryId() == newCategory.getCategoryId();
    }
  }

  public void update(String new_category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE categories SET category_name = :new_category WHERE id = :category_id";
      con.createQuery(sql).addParameter("new_category", new_category).addParameter("category_id", this.getCategoryId()).executeUpdate();
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM categories WHERE id=:category_id";
    String sql2 = "DELETE FROM recipes_categories WHERE category_id = :category_id";
    con.createQuery(sql).addParameter("category_id", this.getCategoryId()).executeUpdate();
    con.createQuery(sql2).addParameter("category_id", this.getCategoryId()).executeUpdate();
    }
  }

  public List<Recipe> getRecipes(){
    try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT recipe_id FROM recipes_categories WHERE category_id = :category_id";
    List<Integer> recipes_ids = con.createQuery(sql).addParameter("category_id", this.getCategoryId()).executeAndFetch(Integer.class);

    List<Recipe> recipes = new ArrayList<Recipe>();

      for (Integer recipe_id : recipes_ids){
        String joinQuery = "SELECT * FROM recipes WHERE id = :recipe_id";
        Recipe recipe = con.createQuery(joinQuery).addParameter("recipe_id", recipe_id).executeAndFetchFirst(Recipe.class);
        recipes.add(recipe);
      }
    return recipes;
    }
  }



}
