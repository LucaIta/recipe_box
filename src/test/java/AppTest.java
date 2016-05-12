import org.sql2o.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.*;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Recipe Box");
  }

  @Test
  public void contantIndexPageTest() {
    Recipe testRecipe = new Recipe("Pizza", "Make the Pizza");
    Category testCategory = new Category("Italian");
    testRecipe.save();
    testCategory.save();
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pizza").contains("Italian");
  }

  @Test
  public void containsAddRecipeForm() {
    goTo("http://localhost:4567/");
    click("a", withText ("Add a new recipe"));
    assertThat(pageSource()).contains("Recipe name");
  }

  @Test
  public void individualRecipePagesRendersAfterCreatingRecipe() {
    goTo("http://localhost:4567/recipe/new");
    fill("#recipeName").with("Pizza");
    fill("#recipeInstructions").with("Make the pizza");
    submit("#addRecipe");
    assertThat(pageSource()).contains("Pizza").contains("Make the pizza");
  }

  @Test
  public void individualRecipePagesRendersCorrectly(){
    Recipe newRecipe = new Recipe("Pizza", "Make the Pizza");
    newRecipe.save();
    goTo("http://localhost:4567/");
    click("a", withText ("Pizza"));
    assertThat(pageSource()).contains("Pizza").contains("Make the Pizza");
  }

  @Test
  public void individualRecipeIsUpdated(){
    Recipe newRecipe = new Recipe("Pizza", "Make the Pizza");
    newRecipe.save();
    String url = String.format("http://localhost:4567/recipe/%d", newRecipe.getRecipeId());
    goTo(url);
    click("option", withText("Recipe Name"));
    fill("#newRecipeContent").with("Margherita Pizza");
    submit("#recipeFormButton");
    assertThat(pageSource()).contains("Margherita Pizza").doesNotContain("This recipe rating is");
  }

  @Test
  public void recipeIsDeleted(){
    Recipe newRecipe = new Recipe("Pizza", "Make the Pizza");
    newRecipe.save();
    String url = String.format("http://localhost:4567/recipe/%d", newRecipe.getRecipeId());
    goTo(url);
    submit(".btn-danger");
    assertThat(pageSource()).contains("Recipe Box").doesNotContain("Pizza");
  }

  @Test
  public void recipeIsRated() {
    Recipe newRecipe = new Recipe("Pizza", "Make the Pizza");
    newRecipe.save();
    String url = String.format("http://localhost:4567/recipe/%d", newRecipe.getRecipeId());
    goTo(url);
    click("option", withText("3"));
    submit("#recipeRating");
    assertThat(pageSource()).contains("Pizza").contains("This recipe rating is 3");
  }

}
