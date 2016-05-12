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
  public void Ingredient_DontSaveIngredientWHenItAlreadyExist() {
    Ingredient firstIngredient = new Ingredient("Flour");
    Ingredient secondIngredient = new Ingredient("Flour");
    firstIngredient.save();
    secondIngredient.save();
    assertEquals(firstIngredient.getId(),secondIngredient.getId());
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

  @Test
  public void Ingredient_retrieveRecipesFromIngredient(){
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    Ingredient testIngredient = new Ingredient("Flour");
    testRecipe.save();
    testIngredient.save();
    testRecipe.addIngredient(testIngredient);
    assertEquals("Pizza", testIngredient.getRecipes().get(0).getRecipeName());
  }

}
