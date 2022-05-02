package Wilson;

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

public class MyApplicationTest extends ApplicationTest {
    private static final String UI_FILE = "/ui/home.fxml";  //file in the folder of src/main/resources/

    @Override
    public void start(Stage stage) throws Exception{
        Parent mainNode = FXMLLoader.load(comp3111.covid.MyApplication.class.getResource(UI_FILE));
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
    public void test(){

    }
}
