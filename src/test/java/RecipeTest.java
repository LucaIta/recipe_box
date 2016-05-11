
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;


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
  public void Recipe_retrieveRecipeCorrectly() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    assertTrue(testRecipe.equals(Recipe.find(testRecipe.getRecipeId())));
  }

  @Test
  public void Recipe_updatesTheRecipeNameCorrectly() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    testRecipe.update("recipe_name","Pasta");
    assertEquals("Pasta", Recipe.find(testRecipe.getRecipeId()).getRecipeName());
  }

  @Test
  public void Recipe_DeleteRecipeFromDatabase() {
    Category testCategory = new Category("Italian");
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    testCategory.save();
    testRecipe.tagRecipe(testCategory);
    testRecipe.delete();
    assertEquals(0, Recipe.all().size());
    assertEquals(0, testRecipe.getCategories().size());
  }

  @Test
  public void Recipe_rateRecipeCorrectly(){
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    testRecipe.rate(2);
    assertEquals(2, Recipe.find(testRecipe.getRecipeId()).getRating());
  }

  @Test
  public void Recipe_tagRecipewithCategory(){
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    Category testCategory = new Category("Italian");
    testCategory.save();
    testRecipe.save();
    testRecipe.tagRecipe(testCategory);
    assertEquals(1, testRecipe.getCategories().size());
  }

}
