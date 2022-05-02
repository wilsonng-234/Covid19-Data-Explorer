package Wilson;

import comp3111.covid.MyApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;


public class LinearRegressionControllerTest extends ApplicationTest{
    private static final String UI_FILE = "/ui/LinearRegression.fxml";  //file in the folder of src/main/resources/

    @Override
    public void start(Stage stage) throws Exception{
        Parent mainNode = FXMLLoader.load(MyApplication.class.getResource(UI_FILE));
        stage.setScene(new Scene(mainNode));
        stage.show();
    }

    @Before
    public void setUp () throws Exception {
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void generateRegressionLine(){
        clickOn("#countryTextField");
        write("Hong Kong");

        clickOn("#startDatePicker");
        write("1/4/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#endDatePicker");
        write("7/7/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#yParameterList");

        clickOn("Total Confirmed Cases");
        clickOn("#generateButton");

        clickOn("Confirmed Cases Per Million");
        clickOn("#generateButton");

        clickOn("Total Deaths");
        clickOn("#generateButton");

        clickOn("Total Deaths Per Million");
        clickOn("#generateButton");

        clickOn("#xParameterList");
        clickOn("Vaccination Rate");
        clickOn("#generateButton");

        clickOn("Fully Vaccination Rate");
        clickOn("#generateButton");
    }

    @Test
    public void dateAlert(){
        clickOn("Fully Vaccination Rate");
        clickOn("#yParameterList");

        clickOn("#countryTextField");
        write("Hong Kong");

        clickOn("#startDatePicker");
        write("1/4/2018");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#endDatePicker");
        write("7/7/2030");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#generateButton");
        clickOn("OK");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#startDatePicker");
        press(KeyCode.CONTROL);
        press(KeyCode.A);
        write("1/4/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#generateButton");
        clickOn("OK");
    }

    @Test
    public void contextMenuTest(){
        clickOn("#countryTextField");
        write("Kong");
        clickOn("Hong Kong");

        press(KeyCode.CONTROL);
        press(KeyCode.A);
        write("United");
        clickOn("United States");
    }
}
