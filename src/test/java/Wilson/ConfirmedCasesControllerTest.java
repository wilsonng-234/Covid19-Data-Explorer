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
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

public class ConfirmedCasesControllerTest extends ApplicationTest {
    private static final String UI_FILE = "/ui/confirmedCases.fxml";  //file in the folder of src/main/resources/

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
    public void testDailyStatistics() {
        clickOn("#datePickerForTable");
        write("20/7/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#selectAllForTable");
        clickOn("#generateTableButton");
        clickOn("#generateTableButton");
        clickOn("#totalCasesRadioButton");
        clickOn("#totalCasesPerMillionRadioButton");
        clickOn("#tableRadioButton");
    }

    @Test
    public void testPeriodicStatitics(){
        clickOn("#chartTab");
        clickOn("#startDatePicker");
        write("1/3/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#endDatePicker");
        write("7/7/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#selectAllForChart");
        clickOn("#generateChartButton");
    }

    @Test
    public void dateAlert(){
        clickOn("#chartTab");
        clickOn("#selectAllForChart");

        clickOn("#startDatePicker");
        write("1/3/2021");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#endDatePicker");
        write("7/7/2020");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#generateChartButton");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    public void a(){

    }
}