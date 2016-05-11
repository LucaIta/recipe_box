import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;


public class CategoryTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();


  @Test
  public void Category_InstanceofCategory() {
    Category testCategory = new Category("Italian");
    assertTrue(testCategory instanceof Category);
  }

  @Test
  public void Category_getCategoryName() {
    Category testCategory = new Category("Italian");
    assertEquals("Italian", testCategory.getCategoryName());
  }

  @Test
  public void Category_SaveCategoryToDatabase() {
    Category testCategory = new Category("Italian");
    testCategory.save();
    assertEquals(1, Category.all().size());
  }

  @Test
  public void Category_FindCategoryInDatabase() {
    Category testCategory = new Category("Italian");
    testCategory.save();
    assertTrue(testCategory.equals(Category.find(testCategory.getCategoryId())));
  }

  @Test
  public void Category_UpdateCategoryInDatabase() {
    Category testCategory = new Category("Italian");
    testCategory.save();
    testCategory.update("Mexican");
    assertFalse(testCategory.equals(Category.find(testCategory.getCategoryId())));
    assertEquals("Mexican", Category.find(testCategory.getCategoryId()).getCategoryName());
  }

  @Test
  public void Category_deletesCategoryCorrectly() {
    Category testCategory = new Category("Italian");
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    testRecipe.save();
    testCategory.save();
    testRecipe.tagRecipe(testCategory);
    testCategory.delete();
    assertEquals(0, Category.all().size());
    assertEquals(0, testCategory.getRecipes().size());
  }

  @Test
  public void Category_retrieveRecipesFromCategory(){
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    Category testCategory = new Category("Italian");
    testRecipe.save();
    testCategory.save();
    testRecipe.tagRecipe(testCategory);
    assertEquals(1, testCategory.getRecipes().size());
  }

}
