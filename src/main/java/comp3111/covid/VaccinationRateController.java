package comp3111.covid;

import covidData.VaccinationRate;
import covidData.CountrySelection;
import covidData.VaccinationRateRecord;
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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.VaccinationRateRecord.NOT_FOUND;

public class VaccinationRateController implements Initializable {
    String dataset = "COVID_Dataset_v1.0.csv";

    @FXML
    private Tab tableTab;
    @FXML
    private Tab chartTab;
    @FXML
    public Tab relationTab;

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
    private TableView<VaccinationRateRecord> covidCasesTable;
    @FXML
    private TableColumn<VaccinationRateRecord,String> countryColumn;
    @FXML
    private TableColumn<VaccinationRateRecord,String> totalCasesColumn;
    @FXML
    private TableColumn<VaccinationRateRecord,String> totalCasesPerMillionColumn;
    // -----------

    // covidCasesLineChart
    @FXML
    private LineChart<Number,Number> vaccinationRateLineChart;
    @FXML
    public NumberAxis chartXAxis;
    @FXML
    public NumberAxis chartYAxis;
    // -----------

    HashSet<String> selectedCountriesForTable = new HashSet<>();
    HashSet<String> selectedCountriesForChart = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableTitle.wrappingWidthProperty().bind(
                covidCasesTable.widthProperty()
        );
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
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("fullyVaccinated"));
        totalCasesPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("rateOfVaccination"));

        countryColumn.prefWidthProperty().bind(
                covidCasesTable.widthProperty().divide(3.0)
        );
        totalCasesColumn.prefWidthProperty().bind(
                covidCasesTable.widthProperty().divide(3.0)
        );
        totalCasesPerMillionColumn.prefWidthProperty().bind(
                covidCasesTable.widthProperty().divide(3.0)
        );

        // initialize Chart
//        XYChart.Series<String,Number> series = new XYChart.Series<>();
//
//        series.setName("Hong Kong");
//        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,15).toString(),100));
//        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,16).toString(),105));
//        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,17).toString(),109));
//        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,18).toString(),1005));
//        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,19).toString(),1005));
//        series.getData().add(new XYChart.Data<>(LocalDate.of(2020,7,20).toString(),1006));
//
//        vaccinationRateLineChart.getXAxis().setLabel("Date");
//        vaccinationRateLineChart.getYAxis().setLabel("Number of Cases");
//        vaccinationRateLineChart.setTitle("Title");
        LocalDate sd = LocalDate.of(2020, 12, 27);
        LocalDate ed = LocalDate.of(2021, 7, 20);
        startDatePicker.setValue(sd);
        endDatePicker.setValue(ed);


        chartXAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(chartXAxis) {
            @Override
            public String toString(final Number object) {
                long longValue = object.longValue();
                LocalDate date = LocalDate.ofEpochDay(longValue);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy");
                return date.format(formatter);
            }
        });
        chartYAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(chartYAxis, null, "%"));
//        vaccinationRateLineChart.getData().add(series);


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

        VaccinationRate vaccinationRate = new VaccinationRate(dateForTable, selectedCountriesForTable,"COVID_Dataset_v1.0.csv");
        HashMap<String, VaccinationRateRecord> vaccinationRateHashMap = vaccinationRate.getVaccinationRateTable();

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountriesForTable.size());
        for (String countryName : selectedCountriesForTable)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        NumberFormat numberFormat = NumberFormat.getInstance();
        for (String countryName : sortedSelectedCountriesList) {
            VaccinationRateRecord vaccinationRateRecord = vaccinationRateHashMap.get(countryName);
            if (!vaccinationRateRecord.getFullyVaccinated().equals(NOT_FOUND))
                vaccinationRateRecord.setFullyVaccinated(numberFormat.format(Integer.parseInt(vaccinationRateRecord.getFullyVaccinated())));
            if (!vaccinationRateRecord.getFullyVaccinated().equals(NOT_FOUND))
                vaccinationRateRecord.setRateOfVaccination(numberFormat.format(Double.parseDouble(vaccinationRateRecord.getRateOfVaccination())));
            covidCasesTable.getItems().add(vaccinationRateRecord);
        }

        System.out.println(numberFormat.format(123456.123456));
    }

    @FXML
    private Button generateChartButton;
    @FXML
    private Label lbl;

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
//        chartXAxis.setLowerBound(iStartDate.toEpochDay());
//        chartXAxis.setUpperBound(iEndDate.toEpochDay());

        vaccinationRateLineChart.getData().clear();
        chartXAxis.setAutoRanging(false);
        chartXAxis.setLowerBound(startDate.toEpochDay());
        chartXAxis.setUpperBound(endDate.toEpochDay());
        VaccinationRate confirmedCases = new VaccinationRate(startDate, endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");
        HashMap<String,XYChart.Series<Number,Number>> vaccinationRateHashMap = confirmedCases.getVaccinationRateChart();
        for (String countryName : selectedCountriesForChart){
            vaccinationRateLineChart.getData().add(vaccinationRateHashMap.get(countryName));
        }

        Collection<XYChart.Series<Number,Number>> seriesList = vaccinationRateHashMap.values();
        List<XYChart.Series<Number,Number>> vaccinationRateList = new ArrayList<>(seriesList);

        // add event handler to every node in lines
        for(XYChart.Series<Number,Number> series : seriesList){
            //Setup for hovering on series (cleaner)
            Path seriesPath = (Path) series.getNode();
            double initialStrokeWidth = seriesPath.getStrokeWidth();

            seriesPath.setOnMouseEntered(e -> {
                updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*4, true);
                lbl.setText(series.getName() + "\n " + "\n ");
            });
            seriesPath.setOnMouseExited(e -> {
                updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*2, false);
                lbl.setText("");
            });

            for (XYChart.Data<Number,Number> data : series.getData()) {

//                data.getNode().setVisible(false);
                data.getNode().setStyle("""
                        -fx-background-color: transparent, transparent;
                        -fx-background-insets: 0, 2;
                        -fx-background-radius: 5px;
                        -fx-padding: 5px;""");

                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event1 -> {
                    updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*4, true);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy");
                    lbl.setText(series.getName()+ "\n" + "Date : " + LocalDate.ofEpochDay((Long) data.getXValue()).format(formatter) + "\nRate : " + data.getYValue() + "%");
                });
                data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event2 -> {
                    updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*2, false);
                    lbl.setText("");
                });
            }
        }
    }

    private void updatePath(Path seriesPath, Paint strokeColor, double strokeWidth, boolean toFront){
        seriesPath.setStroke(strokeColor);
        seriesPath.setStrokeWidth(strokeWidth);
        if(!toFront){ return; }
        seriesPath.toFront();
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