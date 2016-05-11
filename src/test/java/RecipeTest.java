
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;


public class RecipeTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Recipe_InstanceofRecipe() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    assertTrue(testRecipe instanceof Recipe);
  }

  @Test
  public void Recipe_getRecipeName() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    assertEquals("Pizza", testRecipe.getRecipeName());
  }

  @Test
  public void Recipe_getInstructions() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    assertEquals("Make the Pizza", testRecipe.getInstructions());
  }

  @Test
  public void Recipe_SaveRecipe() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    assertEquals(1, Recipe.all().size());
  }

  @Test
  public void find_retrieveRecipeCorrectly() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    assertTrue(testRecipe.equals(Recipe.find(testRecipe.getRecipeId())));
  }

  @Test
  public void update_updatesTheRecipeNameCorrectly() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    testRecipe.update("recipe_name","Pasta");
    assertEquals("Pasta", Recipe.find(testRecipe.getRecipeId()).getRecipeName());
  }
}
