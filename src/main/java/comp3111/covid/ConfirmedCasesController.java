package comp3111.covid;

import covidData.ConfirmedCases;
import covidData.ConfirmedCasesRecord;
import covidData.CountrySelection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
import static covidData.ConfirmedCasesRecord.NOT_FOUND;

public class ConfirmedCasesController implements Initializable {
    String dataset = "COVID_Dataset_v1.0.csv";

    @FXML
    private Tab tableTab;
    @FXML
    private Tab chartTab;

    // datePicker
    @FXML
    private DatePicker datePickerForTable;
    private LocalDate dateForTable = null;

    @FXML
    private DatePicker startDatePicker;
    private LocalDate startDate = null;

    @FXML
    private DatePicker endDatePicker;
    private LocalDate endDate = null;

    // countrySelectionTable
    @FXML
    private TableView<CountrySelection> countrySelectionTableForTable;
    @FXML
    private TableColumn<CountrySelection,CheckBox> countrySelectionColumnForTable;
    @FXML
    private TableColumn<CountrySelection,CheckBox> checkBoxSelectionColumnForTable;

    @FXML
    private TableView<CountrySelection> countrySelectionTableForChart;
    @FXML
    private TableColumn<CountrySelection,CheckBox> countrySelectionColumnForChart;
    @FXML
    private TableColumn<CountrySelection,CheckBox> checkBoxSelectionColumnForChart;
    // -----------

    // covidCasesTable
    @FXML
    private Text tableTitle;
    @FXML
    private TableView<ConfirmedCasesRecord> covidCasesTable;
    @FXML
    private TableColumn<ConfirmedCasesRecord,String> countryColumn;
    @FXML
    private TableColumn<ConfirmedCasesRecord,String> totalCasesColumn;
    @FXML
    private TableColumn<ConfirmedCasesRecord,String> totalCasesPerMillionColumn;
    // -----------

    // covidCasesLineChart
    @FXML
    private LineChart<String,Number> confirmedCasesLineChart;
    // -----------

    HashSet<String> selectedCountriesForTable = new HashSet<>();
    HashSet<String> selectedCountriesForChart = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // datePicker get date
        datePickerForTable.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    dateForTable = newValue;

                    tableTitle.setText("Number of Covid Cases as of " + newValue);
                }
        );

        startDatePicker.valueProperty().addListener(
                new ChangeListener<LocalDate>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                        startDate = newValue;
                    }
                }
        );

        endDatePicker.valueProperty().addListener(
                new ChangeListener<LocalDate>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                        endDate = newValue;
                    }
                }
        );

        // initialize countrySelectionTableForTable
        countrySelectionColumnForTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkBoxSelectionColumnForTable.setCellValueFactory(new PropertyValueFactory<>("select"));

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
                                assert (!selectedCountriesForTable.contains(row.getName()));

                                selectedCountriesForTable.add(row.getName());
                            }
                            else{
                                assert (selectedCountriesForTable.contains(row.getName()));

                                selectedCountriesForTable.remove(row.getName());
                            }

                            System.out.println("from " + oldValue + " to " + newValue);
                            for (String str : selectedCountriesForTable)
                                System.out.print(str + "\t");
                            System.out.println();
                        }
                    }
            );

            countrySelectionTableForTable.getItems().add(row);
        }

        //countrySelectionTableForChart
        countrySelectionColumnForChart.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkBoxSelectionColumnForChart.setCellValueFactory(new PropertyValueFactory<>("select"));

        Map<String, CountrySelection> countrySelectionRows1 = getCountrySelectionRows("COVID_Dataset_v1.0.csv");
        List<CountrySelection> countrySelectionList1 = new ArrayList<>(countrySelectionRows1.size());
        countrySelectionList1.addAll(countrySelectionRows1.values());
        Collections.sort(countrySelectionList1);

        for (CountrySelection row : countrySelectionList1) {
            CheckBox checkBox = row.getSelect();

            checkBox.selectedProperty().addListener(
                    new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (oldValue == newValue)
                                System.out.println("oldValue == newValue");

                            if (newValue){
                                assert (!selectedCountriesForChart.contains(row.getName()));

                                selectedCountriesForChart.add(row.getName());
                            }
                            else{
                                assert (selectedCountriesForChart.contains(row.getName()));

                                selectedCountriesForChart.remove(row.getName());
                            }

                            System.out.println("from " + oldValue + " to " + newValue);
                            for (String str : selectedCountriesForChart)
                                System.out.print(str + "\t");
                            System.out.println();
                        }
                    }
            );

            countrySelectionTableForChart.getItems().add(row);
        }
        // initialize covidCasesTable
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        totalCasesPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("totalCasesPerMillion"));

        // initialize confirmedCaseesLineChart
        XYChart.Series<String,Number> series = new XYChart.Series<>();

        series.setName("Hong Kong");
        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,15).toString(),100));
        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,16).toString(),105));
        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,17).toString(),109));
        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,18).toString(),1005));
        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,19).toString(),1005));
        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,20).toString(),1006));

        confirmedCasesLineChart.getXAxis().setLabel("Date");
        confirmedCasesLineChart.getYAxis().setLabel("Number of Cases");
        confirmedCasesLineChart.setTitle("Title");

        confirmedCasesLineChart.setCreateSymbols(false);
        confirmedCasesLineChart.getData().add(series);

        // tableTab onclick
        tableTab.setOnSelectionChanged(
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {

                    }
                }
        );
        chartTab.setOnSelectionChanged(
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {

                    }
                }
        );
    }

    @FXML
    private Button generateTableButton;
    @FXML
    void generateTableButtonClicked(ActionEvent event) {
        if (dateForTable == null) {
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

        ConfirmedCases confirmedCases = new ConfirmedCases(dateForTable, selectedCountriesForTable,"COVID_Dataset_v1.0.csv");
        HashMap<String, ConfirmedCasesRecord> confirmedCasesHashMap = confirmedCases.getconfirmedCasesTable();

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountriesForTable.size());
        for (String countryName : selectedCountriesForTable)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        NumberFormat numberFormat = NumberFormat.getInstance();
        for (String countryName : sortedSelectedCountriesList) {
            ConfirmedCasesRecord confirmedCasesRecord = confirmedCasesHashMap.get(countryName);
            if (!confirmedCasesRecord.getTotalCases().equals(NOT_FOUND))
                confirmedCasesRecord.setTotalCases(numberFormat.format(Integer.parseInt(confirmedCasesRecord.getTotalCases())));
            if (!confirmedCasesRecord.getTotalCasesPerMillion().equals(NOT_FOUND))
                confirmedCasesRecord.setTotalCasesPerMillion(numberFormat.format(Double.parseDouble(confirmedCasesRecord.getTotalCasesPerMillion())));
            covidCasesTable.getItems().add(confirmedCasesRecord);
        }

        System.out.println(numberFormat.format(123456.123456));
    }

    @FXML
    private Button generateChartButton;

    @FXML
    void generateChartButtonClicked(ActionEvent event) {
        Alert invalidDateAlert = new Alert(Alert.AlertType.WARNING);

        if (startDate == null && endDate == null){
            invalidDateAlert.setTitle("BOTH DATE NOT CHOSEN");
            invalidDateAlert.setContentText("Please choose the start date and end date first");

            invalidDateAlert.showAndWait().ifPresent(
                    new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType buttonType) {
                        }
                    }
            );
            return;
        }
        if (startDate == null){
            invalidDateAlert.setTitle("START DATE NOT CHOSEN");
            invalidDateAlert.setContentText("Please choose the start date first");

            invalidDateAlert.showAndWait().ifPresent(
                    new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType buttonType) {
                        }
                    }
            );
            return;
        }
        if (endDate == null){
            invalidDateAlert.setTitle("END DATE NOT CHOSEN");
            invalidDateAlert.setContentText("Please choose the end date first");

            invalidDateAlert.showAndWait().ifPresent(
                    new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType buttonType) {
                        }
                    }
            );
            return;
        }
        if (startDate.isAfter(endDate)){
            invalidDateAlert.setTitle("INVALID DATE INPUT");
            invalidDateAlert.setContentText("start date cannot be after end date!!");

            invalidDateAlert.showAndWait().ifPresent(
                    new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType buttonType) {
                        }
                    }
            );
            return;
        }

        // update covidCasesChart

        confirmedCasesLineChart.getData().removeAll(confirmedCasesLineChart.getData());

        ConfirmedCases confirmedCases = new ConfirmedCases(startDate,endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");

        HashMap<String,XYChart.Series<String,Number>> confirmedCasesHashMap = confirmedCases.getConfirmedCasesChart();

        for (String countryName : selectedCountriesForChart){
            confirmedCasesLineChart.getData().add(confirmedCasesHashMap.get(countryName));
        }
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
    private CheckBox selectAllForTable;

    @FXML
    void selectAllForTableClicked(ActionEvent event) {
        boolean tick = selectAllForTable.selectedProperty().get();

        for (CountrySelection row : countrySelectionTableForTable.getItems())
            row.getSelect().setSelected(tick);
    }

    @FXML
    private CheckBox selectAllForChart;

    @FXML
    void selectAllForChartClicked(ActionEvent event) {
        boolean tick = selectAllForChart.selectedProperty().get();

        for (CountrySelection row : countrySelectionTableForChart.getItems())
            row.getSelect().setSelected(tick);
    }
}
