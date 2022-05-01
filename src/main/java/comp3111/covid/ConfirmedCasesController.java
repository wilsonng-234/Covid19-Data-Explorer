package comp3111.covid;

import covidData.ConfirmedCases;
import covidData.ConfirmedCasesRecord;
import covidData.CountrySelection;
import covidData.VaccinationRateRecord;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
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
import static covidData.ConfirmedCasesRecord.NOT_FOUND;

public class ConfirmedCasesController implements Initializable {
    String dataset = "COVID_Dataset_v1.0.csv";
    DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("MMMM d,yyyy");

    @FXML
    private AnchorPane rootPane;

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
    private LineChart<Number,Number> confirmedCasesLineChart;
    @FXML
    private NumberAxis chartXAxis;
    @FXML
    private NumberAxis chartYAxis;
    // -----------

    // radioButton
    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private RadioButton tableRadioButton;
    @FXML
    private RadioButton totalCasesRadioButton;
    @FXML
    private RadioButton totalCasesPerMillionRadioButton;
    // ---------


    HashSet<String> selectedCountriesForTable = new HashSet<>();
    HashSet<String> selectedCountriesForChart = new HashSet<>();

    private void setTableTitleWidth(){
        tableTitle.wrappingWidthProperty().bind(
                covidCasesTable.widthProperty()
        );
    }

    private void setTableTitleWithDate(){
        datePickerForTable.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    dateForTable = newValue;

                    tableTitle.setText("Number of Covid Cases as of " + newValue);
                }
        );
    }

    private void setCountrySelectionTable(TableView<CountrySelection> table, TableColumn<CountrySelection,CheckBox> countryColumn,
                                          TableColumn<CountrySelection,CheckBox> checkBoxColumn,HashSet<String> selectedCountries)
    {
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

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
                            System.out.println(row.getName());
                            if (newValue){
                                assert (!selectedCountries.contains(row.getName()));

                                selectedCountries.add(row.getName());
                            }
                            else{
                                assert (selectedCountries.contains(row.getName()));

                                selectedCountries.remove(row.getName());
                            }

                            sortCountrySelectionColumn(table);
                        }
                    }
            );

            table.getItems().add(row);
        }
    }

    private void setCovidCasesTable(){
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        totalCasesPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("totalCasesPerMillion"));
        countryColumn.prefWidthProperty().bind(
                covidCasesTable.widthProperty().divide(3.1)
        );
        totalCasesColumn.prefWidthProperty().bind(
                covidCasesTable.widthProperty().divide(3.1)
        );
        totalCasesPerMillionColumn.prefWidthProperty().bind(
                covidCasesTable.widthProperty().divide(3.1)
        );

        totalCasesColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        totalCasesPerMillionColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
    }

    private void sortCountrySelectionColumn(TableView<CountrySelection> countrySelection) {
        // once chosen, will move up to the top
        countrySelection.getItems().sort((o1,o2) -> {
            if (o1.getSelect().isSelected() && !o2.getSelect().isSelected())
                return -1;
            else if (!o1.getSelect().isSelected() && o2.getSelect().isSelected())
                return 1;
            else {
                return o1.getName().compareTo(o2.getName());
            }
        });
        countrySelection.sort();
    }

    private void setConfirmedCasesLineChart() {
        chartXAxis.setLabel("Date");
        chartYAxis.setLabel("Number of Cases");
        confirmedCasesLineChart.setTitle("Cumulative Confirmed COVID-19 Cases (per 1M)");

        chartXAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(chartXAxis) {
            @Override
            public String toString(final Number object) {
                long epochDay = object.longValue();
                LocalDate date = LocalDate.ofEpochDay(epochDay);
                String[] dateStringArray = date.toString().split("-");
                String dateString = dateStringArray[0] + "-" + dateStringArray[1] + "-" + dateStringArray[2];

                return dateString;
            }
        });
        chartXAxis.setAutoRanging(false);
        chartXAxis.setLowerBound(LocalDate.of(2020,3,1).toEpochDay());
        chartXAxis.setUpperBound(LocalDate.of(2021,7,20).toEpochDay());
        chartXAxis.setTickUnit((LocalDate.of(2021,7,20).toEpochDay()-LocalDate.of(2020,3,1).toEpochDay())/4.0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initalize table
        setTableTitleWidth();
        setTableTitleWithDate();

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

        // initalize radio buttons
        tableRadioButton.setSelected(true);
        totalCasesBarChart.setVisible(false);   //totalCasesBarChart.setAnimated(false);
        perMillionBarChart.setVisible(false);   //perMillionBarChart.setAnimated(false);

        // initialize countriesTables
        setCountrySelectionTable(countrySelectionTableForTable,countrySelectionColumnForTable,checkBoxSelectionColumnForTable,selectedCountriesForTable);
        setCountrySelectionTable(countrySelectionTableForChart,countrySelectionColumnForChart,checkBoxSelectionColumnForChart,selectedCountriesForChart);

        // initialize covidCasesTable
        setCovidCasesTable();

        // initialize confirmedCaseesLineChart
        setConfirmedCasesLineChart();
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
        totalCasesBarChart.getData().clear();
        perMillionBarChart.getData().clear();

        ConfirmedCases confirmedCases = new ConfirmedCases(dateForTable, selectedCountriesForTable,"COVID_Dataset_v1.0.csv");
        HashMap<String, ConfirmedCasesRecord> confirmedCasesHashMap = confirmedCases.getconfirmedCasesTable();
        XYChart.Series<Number, String> totalConfirmedCasesSeries = new XYChart.Series<>();
        totalConfirmedCasesSeries.setName("Total Confirmed Cases");
        XYChart.Series<Number, String> confirmedCasesPerMillionSeries = new XYChart.Series<>();
        confirmedCasesPerMillionSeries.setName("Confirmed Cases Per Million");

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountriesForTable.size());
        for (String countryName : selectedCountriesForTable)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        for (String countryName : sortedSelectedCountriesList) {
            ConfirmedCasesRecord confirmedCasesRecord = confirmedCasesHashMap.get(countryName);

            try{
                String totalCases = confirmedCasesRecord.getTotalCases();
                totalCases = totalCases.replaceAll(",","");

                totalConfirmedCasesSeries.getData().add(new XYChart.Data<>(Integer.parseInt(totalCases),countryName));
            }
            catch(NumberFormatException exception){
                totalConfirmedCasesSeries.getData().add(new XYChart.Data<>(0, countryName));
            }

            try{
                String totalCasesPerMillion = confirmedCasesRecord.getTotalCasesPerMillion();
                totalCasesPerMillion = totalCasesPerMillion.replaceAll(",","");

                confirmedCasesPerMillionSeries.getData().add(new XYChart.Data<>(Double.parseDouble(totalCasesPerMillion),countryName));
            }
            catch (NumberFormatException exception){
                confirmedCasesPerMillionSeries.getData().add(new XYChart.Data<>(0, countryName));
            }

            confirmedCasesRecord.setTotalCases(confirmedCasesRecord.getTotalCases());
            confirmedCasesRecord.setTotalCasesPerMillion(confirmedCasesRecord.getTotalCasesPerMillion());

            covidCasesTable.getItems().add(confirmedCasesRecord);
        }

        totalCasesBarChart.getData().add(totalConfirmedCasesSeries);
        perMillionBarChart.getData().add(confirmedCasesPerMillionSeries);
    }

    @FXML
    private Button generateChartButton;
    @FXML
    private Label nodeLabel;

    private void setNodeHovered(){
        for (XYChart.Series<Number,Number> series : confirmedCasesLineChart.getData()){
            Path seriesPath = (Path) series.getNode();
            double initialStrokeWidth = seriesPath.getStrokeWidth();

            seriesPath.setOnMouseEntered(
                    e -> {
                        updatePath(seriesPath, seriesPath.strokeProperty().get(),initialStrokeWidth*4,true);
                        nodeLabel.setText(series.getName() + "\n\n" );
                    }
            );
            seriesPath.setOnMouseExited(e -> {
                updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*2, false);
                nodeLabel.setText("");
            });

            for (XYChart.Data<Number,Number> datum : series.getData()){
                datum.getNode().setStyle("""
                        -fx-background-color: transparent, transparent;
                        -fx-background-insets: 0, 2;
                        -fx-background-radius: 5px;
                        -fx-padding: 5px;""");

                datum.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, enter-> {
                    updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth * 4, true);
                    nodeLabel.setText(series.getName() + "\n" + "Date : " + LocalDate.ofEpochDay((Long) datum.getXValue()).format(displayDateFormatter) + "\nCases : " + datum.getYValue());
                });
                datum.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, exit->{
                    updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*2, false);
                    nodeLabel.setText("");
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
        if (startDate.isEqual(endDate)){
            invalidDateAlert.setTitle("INVALID DATE INPUT");
            invalidDateAlert.setContentText("start date cannot be equals to end date!!");

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
        confirmedCasesLineChart.getData().clear();

        chartXAxis.setLowerBound(startDate.toEpochDay());
        chartXAxis.setUpperBound(endDate.toEpochDay());

        ConfirmedCases confirmedCasesData = new ConfirmedCases(startDate,endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");
        HashMap<String,XYChart.Series<Number,Number>> confirmedCasesHashMap = confirmedCasesData.getConfirmedCasesChart();

        for (String countryName : selectedCountriesForChart){
            confirmedCasesLineChart.getData().add(confirmedCasesHashMap.get(countryName));
        }

        setNodeHovered();
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

        if (tick)
            for (int i=0; i<countrySelectionTableForTable.getItems().size(); i++){
                CountrySelection row = countrySelectionTableForTable.getItems().get(i);
                row.getSelect().setSelected(true);
            }
        else
            for (int i=0; i<countrySelectionTableForTable.getItems().size(); i++){
                CountrySelection row = countrySelectionTableForTable.getItems().get(0);
                row.getSelect().setSelected(false);
            }
    }

    @FXML
    private CheckBox selectAllForChart;

    @FXML
    void selectAllForChartClicked(ActionEvent event) {
        boolean tick = selectAllForChart.selectedProperty().get();

        if (tick)
            for (int i=0; i<countrySelectionTableForChart.getItems().size(); i++){
                CountrySelection row = countrySelectionTableForChart.getItems().get(i);
                row.getSelect().setSelected(true);
            }
        else
            for (int i=0; i<countrySelectionTableForChart.getItems().size(); i++){
                CountrySelection row = countrySelectionTableForChart.getItems().get(0);
                row.getSelect().setSelected(false);
            }
    }

    @FXML
    private BarChart<Number,String> totalCasesBarChart;

    @FXML
    private BarChart<Number,String> perMillionBarChart;

    @FXML
    void getGraph(ActionEvent event) {
        if (tableRadioButton.isSelected()) {
            covidCasesTable.setVisible(true);
            totalCasesBarChart.setVisible(false);
            perMillionBarChart.setVisible(false);
        }
        else if (totalCasesRadioButton.isSelected()) {
            covidCasesTable.setVisible(false);
            totalCasesBarChart.setVisible(true);
            perMillionBarChart.setVisible(false);
        } else {
            covidCasesTable.setVisible(false);
            totalCasesBarChart.setVisible(false);
            perMillionBarChart.setVisible(true);
        }
    }

}
