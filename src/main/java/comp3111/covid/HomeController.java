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

    @FXML
    void switchToDeathCasesScene(MouseEvent event) {

    }

    @FXML
    private ImageView VaccinationIcon;

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

    @FXML
    void switchToLinearRegressionScene(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/LinearRegression.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
