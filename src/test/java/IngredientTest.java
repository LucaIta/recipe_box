import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;


public class IngredientTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Ingredient_InstanceOfIngredient() {
    Ingredient testIngredient = new Ingredient("Flour");
    assertTrue(testIngredient instanceof Ingredient);
  }

  @Test
  public void Ingredient_GetIngredientName() {
    Ingredient testIngredient = new Ingredient("Flour");
    assertEquals("Flour", testIngredient.getIngredientName());
  }

  @Test
  public void Ingredient_SaveIngredient() {
    Ingredient testIngredient = new Ingredient("Flour");
    testIngredient.save();
    assertEquals(1, Ingredient.all().size());
  }

  @Test
  public void Ingredient_FindIngredient() {
    Ingredient testIngredient = new Ingredient("Flour");
    testIngredient.save();
    Ingredient foundIngredient = Ingredient.findIngredient(testIngredient.getId());
    assertTrue(testIngredient.equals(foundIngredient));
  }

  @Test
  public void Ingredient_DeleteIngredient() {
    Ingredient testIngredient = new Ingredient("Flour");
    Recipe testRecipe = new Recipe("Pizza", "Make the pizza");
    testRecipe.save();
    testIngredient.save();
    testRecipe.addIngredient(testIngredient);
    testIngredient.delete();
    assertEquals(0, Ingredient.all().size());
    assertEquals(0, testRecipe.getIngredients().size());
  }

}
