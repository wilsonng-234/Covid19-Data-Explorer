package comp3111.covid;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static comp3111.covid.DataAnalysis.getFileParser;

public class ConfirmedCasesController implements Initializable {
    @FXML
    private DatePicker DatePicker;
    @FXML
    private ListView<String> CountryList;

    @FXML
    private TableView<temp> Table;
    @FXML
    private Text Title;
    @FXML
    private TableColumn<temp,String> CountryColumn;
    @FXML
    private TableColumn<temp,String> TotalCasesColumn;
    @FXML
    private TableColumn<temp,String> TotalCasesPerMillionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        TotalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        TotalCasesPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("totalCasesPerMillion"));

        ArrayList<String> countries = new ArrayList<String>();
        for (CSVRecord csvRecord : getFileParser("COVID_Dataset_v1.0.csv")) {
            if (!countries.contains(csvRecord.get("location"))) {
                countries.add(csvRecord.get("location"));
            }
        }

        CountryList.getItems().addAll(countries);
        CountryList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        CountryList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->
                        System.out.println("Check box for "+item+" changed from "+wasSelected+" to "+isNowSelected)
                );
                return observable;
            }
        }));
    }

    @FXML
    private ImageView home;

    @FXML
    void switchToHomeScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/home.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
