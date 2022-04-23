package comp3111.covid;

import covidData.ConfirmedCases;
import covidData.ConfirmedCasesTable;
import covidData.CountrySelection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.ConfirmedCasesTable.NOT_FOUND;

public class ConfirmedCasesController implements Initializable {
    String dataset = "COVID_Dataset_v1.0.csv";
    // datePicker
    @FXML
    private DatePicker datePicker;
    private LocalDate date = null;

    // countrySelectionTable
    @FXML
    private TableView<CountrySelection> countrySelectionTable;
    @FXML
    private TableColumn<CountrySelection,CheckBox> countrySelectionColumn;
    @FXML
    private TableColumn<CountrySelection,CheckBox> checkBoxSelectionColumn;
    // -----------

    // covidCasesTable
    @FXML
    private Text Title;
    @FXML
    private TableView<ConfirmedCasesTable> covidCasesTable;
    @FXML
    private TableColumn<ConfirmedCasesTable,String> countryColumn;
    @FXML
    private TableColumn<ConfirmedCasesTable,String> totalCasesColumn;
    @FXML
    private TableColumn<ConfirmedCasesTable,String> totalCasesPerMillionColumn;
    // -----------

    HashSet<String> selectedCountries = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // datePicker get date
        datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    date = newValue;

                    Title.setText("Number of Covid Cases as of " + newValue);
                }
        );

        // initialize countrySelectionTable
        countrySelectionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkBoxSelectionColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

        Map<String, CountrySelection> countrySelectionRows = getCountrySelectionRows("COVID_Dataset_v1.0.csv");
        List<CountrySelection> countrySelectionList = new ArrayList<>(countrySelectionRows.size());
        countrySelectionList.addAll(countrySelectionRows.values());
        Collections.sort(countrySelectionList);

        for (CountrySelection row : countrySelectionList) {
            CheckBox checkBox = row.getSelect();

            checkBox.selectedProperty().addListener(
                    new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (oldValue == newValue)
                                System.out.println("oldValue == newValue");

                            if (newValue){
                                assert (!selectedCountries.contains(row.getName()));

                                selectedCountries.add(row.getName());
                            }
                            else{
                                assert (selectedCountries.contains(row.getName()));

                                selectedCountries.remove(row.getName());
                            }

                            System.out.println("from " + oldValue + " to " + newValue);
                            for (String str : selectedCountries)
                                System.out.print(str + "\t");
                            System.out.println();
                        }
                    }
            );

            countrySelectionTable.getItems().add(row);
        }

        // initialize covidCasesTable
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        totalCasesPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("totalCasesPerMillion"));
    }

    @FXML
    private Button generateTableButton;
    @FXML
    void generateTableButtonClicked(ActionEvent event) {
        if (date == null) {
            Alert dateNotChosenAlert = new Alert(Alert.AlertType.WARNING);
            dateNotChosenAlert.setTitle("DATE NOT CHOSEN");
            dateNotChosenAlert.setContentText("Please choose the date first");

            dateNotChosenAlert.showAndWait().ifPresent(
                    new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType buttonType) {
                        }
                    }
            );
            return;
        }
        // update covidCasesTable
        covidCasesTable.getItems().removeAll(covidCasesTable.getItems());

        ConfirmedCases confirmedCases = new ConfirmedCases(date,selectedCountries,"COVID_Dataset_v1.0.csv");
        HashMap<String, ConfirmedCasesTable> confirmedCasesHashMap = confirmedCases.getconfirmedCasesTable();

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountries.size());
        for (String countryName : selectedCountries)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        NumberFormat numberFormat = NumberFormat.getInstance();
        for (String countryName : sortedSelectedCountriesList) {
            ConfirmedCasesTable confirmedCasesTable = confirmedCasesHashMap.get(countryName);
            if (!confirmedCasesTable.getTotalCases().equals(NOT_FOUND))
                confirmedCasesTable.setTotalCases(numberFormat.format(Integer.parseInt(confirmedCasesTable.getTotalCases())));
            if (!confirmedCasesTable.getTotalCasesPerMillion().equals(NOT_FOUND))
                confirmedCasesTable.setTotalCasesPerMillion(numberFormat.format(Double.parseDouble(confirmedCasesTable.getTotalCasesPerMillion())));
            covidCasesTable.getItems().add(confirmedCasesTable);
        }

        System.out.println(numberFormat.format(123456.123456));
    }

    @FXML
    private ImageView tableHomeImage;

    @FXML
    void switchToHomeScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/home.fxml"));
        Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private Map<String, CountrySelection> getCountrySelectionRows(String dataset){
        Map<String, CountrySelection> countrySelectionRows = new HashMap<>();

        for (CSVRecord csvRecord : getFileParser(dataset)) {
            String country = csvRecord.get("location");

            if (!countrySelectionRows.containsKey(country)) {
                CountrySelection countrySelection = new CountrySelection(country);

                countrySelectionRows.put(country,countrySelection);
            }
        }

        return countrySelectionRows;
    }

    @FXML
    private CheckBox selectAll;

    @FXML
    void selectAllCheckBoxClicked(ActionEvent event) {
        boolean tick = selectAll.selectedProperty().get();

        for (CountrySelection row : countrySelectionTable.getItems())
            row.getSelect().setSelected(tick);
    }
}
