package comp3111.covid;

import covidData.CountrySelection;
import covidData.VaccinationRate;
import covidData.VaccinationRateTable;
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
import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;

public class VaccinationRateController implements Initializable {
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
    private TableView<VaccinationRateTable> VaccinationRateTable;
    @FXML
    private TableColumn<VaccinationRateTable,String> countryColumn;
    @FXML
    private TableColumn<VaccinationRateTable,String> fullyVaccinatedColumn;
    @FXML
    private TableColumn<VaccinationRateTable,String> rateOfVaccinationColumn;
    @FXML
    private Button generateVaccinationTableButton;
    // -----------

    HashSet<String> selectedCountries = new HashSet<>();
    List<String> countries = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // datePicker get date
        datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    date = newValue;
                    System.out.println(oldValue + "\t" + newValue);
                }
        );


        // initialize countrySelectionTable
        countrySelectionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkBoxSelectionColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

        Map<String, CountrySelection> countrySelectionRows = getCountrySelectionRows(dataset);
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
                                assert (!countries.contains(row.getName()));

                                countries.add(row.getName());
                            }
                            else{
                                assert (countries.contains(row.getName()));

                                countries.remove(row.getName());
                            }

                            System.out.println("from " + oldValue + " to " + newValue);
                            for (String str : countries)
                                System.out.print(str + "\t");
                            System.out.println();
                        }
                    }
            );

            countrySelectionTable.getItems().add(row);
        }

        // initialize covidCasesTable
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        fullyVaccinatedColumn.setCellValueFactory(new PropertyValueFactory<>("fullyVaccinated"));
        rateOfVaccinationColumn.setCellValueFactory(new PropertyValueFactory<>("rateOfVaccination"));
    }

    @FXML
    private Button generateTableButton;
    @FXML
    void generateTableButtonClicked(ActionEvent event) {
        // update covidCasesTable

//        ConfirmedCases confirmedCases = new ConfirmedCases(date,selectedCountries,dataset);
//        Map<String,String> totalCases = confirmedCases.getTotalCases().get(0);
//        Map<String,String> totalCasesPerMillion = confirmedCases.getCasesPerMillion().get(0);
//
//        for (String countryName : selectedCountries){
//            String totalCases_ = String.valueOf(totalCases.get(countryName));
//            String totalCasesPerMillion_ = String.valueOf(totalCasesPerMillion.get(countryName));
//
//            covidCasesTable.getItems().add(new foo(countryName,totalCases_,totalCasesPerMillion_));
//        }
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
    public void generateVaccinationTable(MouseEvent event) {
        LocalDate d = LocalDate.of(2021, 7, 20);
        List<String> c = new ArrayList<>();
        c.add("Hong Kong");
        date = d;
        countries = c;
        System.out.println(countries);

        VaccinationRate vacRecord = new VaccinationRate(date, countries, dataset);
        HashMap<String, VaccinationRateTable> vacTableMap = vacRecord.getVaccinationRateTable();




//        VaccinationRateTable.getItems().clear();
        for (Map.Entry<String, VaccinationRateTable> entry : vacTableMap.entrySet()) {
            VaccinationRateTable.getItems().add(entry.getValue());
        }
    }
}