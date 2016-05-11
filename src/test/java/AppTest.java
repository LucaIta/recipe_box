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
  public void individualRecipePagesRendersCorrectlt() {
    goTo("http://localhost:4567/recipe/new");
    fill("#recipeName").with("Pizza");
    fill("#recipeInstructions").with("Make the pizza");
    submit("#addRecipe");
    assertThat(pageSource()).contains("Pizza").contains("Make the pizza");
  }

}
