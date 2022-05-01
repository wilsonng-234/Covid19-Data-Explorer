package comp3111.covid;

import covidData.confirmedDeath;
import covidData.confirmedDeathRecord;
import covidData.CountrySelection;
import javafx.beans.InvalidationListener;
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
import static covidData.confirmedDeathRecord.NOT_FOUND;

public class ConfirmedDeathController implements Initializable {
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

    // covidDeathTable
    @FXML
    private Text tableTitle;
    @FXML
    private TableView<confirmedDeathRecord> covidDeathTable;
    @FXML
    private TableColumn<confirmedDeathRecord,String> countryColumn;
    @FXML
    private TableColumn<confirmedDeathRecord,String> totalDeathColumn;
    @FXML
    private TableColumn<confirmedDeathRecord,String> totalDeathPerMillionColumn;
    // -----------

    // covidDeathLineChart
    @FXML
    private LineChart<String,Number> confirmedDeathLineChart;
    // -----------

    HashSet<String> selectedCountriesForTable = new HashSet<>();
    HashSet<String> selectedCountriesForChart = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableTitle.wrappingWidthProperty().bind(
                covidDeathTable.widthProperty()
        );

        // datePicker get date
        datePickerForTable.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    dateForTable = newValue;

                    tableTitle.setText("Number of Covid Deaths as of " + newValue);
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
        // initialize covidDeathTable
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalDeathColumn.setCellValueFactory(new PropertyValueFactory<>("totalDeath"));
        totalDeathPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("totalDeathPerMillion"));
        countryColumn.prefWidthProperty().bind(
                covidDeathTable.widthProperty().divide(3.0)
        );
        totalDeathColumn.prefWidthProperty().bind(
                covidDeathTable.widthProperty().divide(3.0)
        );
        totalDeathPerMillionColumn.prefWidthProperty().bind(
                covidDeathTable.widthProperty().divide(3.0)
        );

        // initialize confirmedCaseesLineChart

        confirmedDeathLineChart.getXAxis().setLabel("Date");
        confirmedDeathLineChart.getYAxis().setLabel("Number of Death");
        confirmedDeathLineChart.setTitle("Title");
        confirmedDeathLineChart.setCreateSymbols(false);

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
        // update covidDeathTable
        covidDeathTable.getItems().removeAll(covidDeathTable.getItems());

        confirmedDeath confirmedDeath = new confirmedDeath(dateForTable, selectedCountriesForTable,"COVID_Dataset_v1.0.csv");
        HashMap<String, confirmedDeathRecord> confirmedDeathHashMap = confirmedDeath.getDeathTable();

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountriesForTable.size());
        for (String countryName : selectedCountriesForTable)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        NumberFormat numberFormat = NumberFormat.getInstance();
        for (String countryName : sortedSelectedCountriesList) {
            confirmedDeathRecord confirmedDeathRecord = confirmedDeathHashMap.get(countryName);
            if (!confirmedDeathRecord.getTotalDeath().equals(NOT_FOUND))
                confirmedDeathRecord.setTotalDeath(numberFormat.format(Integer.parseInt(confirmedDeathRecord.getTotalDeath())));
            if (!confirmedDeathRecord.getTotalDeathPerMillion().equals(NOT_FOUND))
                confirmedDeathRecord.setTotalDeathPerMillion(numberFormat.format(Double.parseDouble(confirmedDeathRecord.getTotalDeathPerMillion())));
            covidDeathTable.getItems().add(confirmedDeathRecord);
        }

        System.out.println(tableTitle.wrappingWidthProperty());
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

        // update covidDeathChart

        confirmedDeathLineChart.getData().removeAll(confirmedDeathLineChart.getData());

        confirmedDeath confirmedDeath = new confirmedDeath(startDate,endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");

        HashMap<String,XYChart.Series<String,Number>> confirmedDeathHashMap = confirmedDeath.getConfirmedDeathChart();

        for (String countryName : selectedCountriesForChart){
            confirmedDeathLineChart.getData().add(confirmedDeathHashMap.get(countryName));
        }
    }

//    @FXML
//    void generateChartButtonClicked(ActionEvent event) {
//        Alert invalidDateAlert = new Alert(Alert.AlertType.WARNING);
//
//        if (startDate == null && endDate == null){
//            invalidDateAlert.setTitle("BOTH DATE NOT CHOSEN");
//            invalidDateAlert.setContentText("Please choose the start date and end date first");
//
//            invalidDateAlert.showAndWait().ifPresent(
//                    new Consumer<ButtonType>() {
//                        @Override
//                        public void accept(ButtonType buttonType) {
//                        }
//                    }
//            );
//            return;
//        }
//        if (startDate == null){
//            invalidDateAlert.setTitle("START DATE NOT CHOSEN");
//            invalidDateAlert.setContentText("Please choose the start date first");
//
//            invalidDateAlert.showAndWait().ifPresent(
//                    new Consumer<ButtonType>() {
//                        @Override
//                        public void accept(ButtonType buttonType) {
//                        }
//                    }
//            );
//            return;
//        }
//        if (endDate == null){
//            invalidDateAlert.setTitle("END DATE NOT CHOSEN");
//            invalidDateAlert.setContentText("Please choose the end date first");
//
//            invalidDateAlert.showAndWait().ifPresent(
//                    new Consumer<ButtonType>() {
//                        @Override
//                        public void accept(ButtonType buttonType) {
//                        }
//                    }
//            );
//            return;
//        }
//        if (startDate.isAfter(endDate)){
//            invalidDateAlert.setTitle("INVALID DATE INPUT");
//            invalidDateAlert.setContentText("start date cannot be after end date!!");
//
//            invalidDateAlert.showAndWait().ifPresent(
//                    new Consumer<ButtonType>() {
//                        @Override
//                        public void accept(ButtonType buttonType) {
//                        }
//                    }
//            );
//            return;
//        }
//
//        // update covidDeathChart
//        Map<String,LocalDate> countriesNotFound = new HashMap<>();
//
//        confirmedDeathLineChart.getData().removeAll(confirmedDeathLineChart.getData());
//
//        confirmedDeath confirmedDeath = new confirmedDeath(startDate,endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");
//
//        HashMap<String,XYChart.Series<LocalDate,String>> confirmedDeathHashMap = confirmedDeath.getconfirmedDeathChart();
//
//        for (String countryName : selectedCountriesForChart){
//            XYChart.Series<LocalDate, String> series = confirmedDeathHashMap.get(countryName);
//
//            String value = series.getData().get(0).getYValue();
//
//            if (value.equals(NOT_FOUND)) {
//                confirmedDeathLineChart.getData().add(series);
//            }
//            else{
//                //if (!countriesNotFound.containsKey(countryName))
//                    //countriesNotFound.put(countryName,LocalDate.)
//            }
//        }
//    }

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

