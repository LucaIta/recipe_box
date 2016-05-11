import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;


public class Ingredient {
  private String ingredient_name;
  private int id;

  public Ingredient(String ingredient_name) {
    this.ingredient_name = ingredient_name;
  }

  public String getIngredientName() {
    return ingredient_name;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO ingredients (ingredient_name) VALUES (:ingredient_name)";
      this.id = (int) con.createQuery(sql, true).addParameter("ingredient_name", this.getIngredientName()).executeUpdate().getKey();
    }
  }

  public static List<Ingredient> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients";
      return con.createQuery(sql).executeAndFetch(Ingredient.class);
    }
  }

  public static Ingredient findIngredient(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients WHERE id=:ingredient_id";
      return con.createQuery(sql).addParameter("ingredient_id", id).executeAndFetchFirst(Ingredient.class);
    }
  }

  public boolean equals(Object obj) {
    if(!(obj instanceof Ingredient)) {
      return false;
    } else {
      Ingredient newIngredient = (Ingredient) obj;
      return newIngredient.getIngredientName().equals(this.getIngredientName()) && newIngredient.getId() == this.getId();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM ingredients WHERE id =:ingredient_id";
      con.createQuery(sql).addParameter("ingredient_id", this.getId()).executeUpdate();
      String sql2 = "DELETE FROM recipes_ingredients WHERE ingredient_id = :ingredient_id";
      con.createQuery(sql2).addParameter("ingredient_id",  this.getId()).executeUpdate();
    }
  }

}
