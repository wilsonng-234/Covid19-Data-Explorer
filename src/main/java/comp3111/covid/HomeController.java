package comp3111.covid;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private ImageView ConfirmedCasesIcon;

    /**
     * switch to confirmed cases scene
     * @param event ConfirmedCasesIcon is clicked
     * @throws IOException
     */
    @FXML
    void switchToConfirmedCasesScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/confirmedCases.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ImageView DeathCasesIcon;

    /**
     * switch to death cases scene
     * @param event DeathCasesIcon is clicked
     * @throws IOException
     */
    @FXML
    void switchToDeathCasesScene(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/confirmedDeath.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ImageView VaccinationIcon;

    /**
     * switch to rate of vaccination scene
     * @param event VaccinationIcon is clicked
     * @throws IOException
     */
    @FXML
    void switchToRateOfVaccinationScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/vaccinationRate.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ImageView LinearRegressionIcon;

    /**
     * switch to linear regression scene
     * @param event linear regression icon is clicked
     * @throws IOException
     */
    @FXML
    void switchToLinearRegressionScene(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/LinearRegression.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
