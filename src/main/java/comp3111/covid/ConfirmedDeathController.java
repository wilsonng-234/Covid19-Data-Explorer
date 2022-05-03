package comp3111.covid;

import covidData.confirmedDeath;
import covidData.confirmedDeathRecord;
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
import static covidData.confirmedDeathRecord.NOT_FOUND;

/**
 *  ConfirmedDeathController initializes ConfirmedDeath scene
 */
public class ConfirmedDeathController implements Initializable {
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
    private LineChart<Number,Number> confirmedDeathLineChart;
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
    private RadioButton totalDeathRadioButton;
    @FXML
    private RadioButton totalDeathPerMillionRadioButton;
    // ---------


    HashSet<String> selectedCountriesForTable = new HashSet<>();
    HashSet<String> selectedCountriesForChart = new HashSet<>();

    /**
     * bind table title width with table width
     */
    private void setTableTitleWidth(){
        tableTitle.wrappingWidthProperty().bind(
                covidDeathTable.widthProperty()
        );
    }

    /**
     * set table title date when date is selected
     */
    private void setTableTitleWithDate(){
        datePickerForTable.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    dateForTable = newValue;

                    tableTitle.setText("Number of Covid Death as of " + newValue);
                }
        );
    }

    /**
     * Initialize countrySelection table
     *
     * @param table The table to be initialized
     * @param countryColumn The countryName column in the table
     * @param checkBoxColumn The checkBox column in the table
     * @param selectedCountries The selectedCountries HashSet
     */
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

    /**
     *  Initialize cells in covidDeathTable. <br>
     *  Bind column width with table width. <br>
     *  Set column display alignment. <br>
     */
    private void setCovidDeathTable(){
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalDeathColumn.setCellValueFactory(new PropertyValueFactory<>("totalDeath"));
        totalDeathPerMillionColumn.setCellValueFactory(new PropertyValueFactory<>("totalDeathPerMillion"));
        countryColumn.prefWidthProperty().bind(
                covidDeathTable.widthProperty().divide(3.1)
        );
        totalDeathColumn.prefWidthProperty().bind(
                covidDeathTable.widthProperty().divide(3.1)
        );
        totalDeathPerMillionColumn.prefWidthProperty().bind(
                covidDeathTable.widthProperty().divide(3.1)
        );

        totalDeathColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        totalDeathPerMillionColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
    }

    /**
     * Sort CountrySelectionColumn to put selected countries at top.
     *
     * @param countrySelection The countrySelectionTable that user is using.
     */
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

    /**
     *  Initialize confirmedDeath LineChart x-axis,y-axis property.
     */
    private void setConfirmedDeathLineChart() {
        chartXAxis.setLabel("Date");
        chartYAxis.setLabel("Number of Death");
        confirmedDeathLineChart.setTitle("Cumulative Confirmed COVID-19 Death (per 1M)");

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

    /**
     * This method is called when the ConfirmedDeath scene is going to be displayed. <br>
     * It initializes the ConfirmedDeath scene.
     */
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
        totalDeathBarChart.setVisible(false);   //totalDeathBarChart.setAnimated(false);
        perMillionBarChart.setVisible(false);   //perMillionBarChart.setAnimated(false);

        // initialize countriesTables
        setCountrySelectionTable(countrySelectionTableForTable,countrySelectionColumnForTable,checkBoxSelectionColumnForTable,selectedCountriesForTable);
        setCountrySelectionTable(countrySelectionTableForChart,countrySelectionColumnForChart,checkBoxSelectionColumnForChart,selectedCountriesForChart);

        // initialize covidDeathTable
        setCovidDeathTable();

        // initialize confirmedCaseesLineChart
        setConfirmedDeathLineChart();
    }

    @FXML
    private Button generateTableButton;

    /**
     *  Set generate table button on clicked.
     */
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
        totalDeathBarChart.getData().clear();
        perMillionBarChart.getData().clear();

        confirmedDeath confirmedDeath = new confirmedDeath(dateForTable, selectedCountriesForTable,"COVID_Dataset_v1.0.csv");
        HashMap<String, confirmedDeathRecord> confirmedDeathHashMap = confirmedDeath.getconfirmedDeathTable();
        XYChart.Series<Number, String> totalConfirmedDeathSeries = new XYChart.Series<>();
        totalConfirmedDeathSeries.setName("Total Confirmed Death");
        XYChart.Series<Number, String> confirmedDeathPerMillionSeries = new XYChart.Series<>();
        confirmedDeathPerMillionSeries.setName("Confirmed Death Per Million");

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountriesForTable.size());
        for (String countryName : selectedCountriesForTable)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        for (String countryName : sortedSelectedCountriesList) {
            confirmedDeathRecord confirmedDeathRecord = confirmedDeathHashMap.get(countryName);

            try{
                String totalDeath = confirmedDeathRecord.getTotalDeath();
                totalDeath = totalDeath.replaceAll(",","");

                totalConfirmedDeathSeries.getData().add(new XYChart.Data<>(Integer.parseInt(totalDeath),countryName));
            }
            catch(NumberFormatException exception){
                totalConfirmedDeathSeries.getData().add(new XYChart.Data<>(0, countryName));
            }

            try{
                String totalDeathPerMillion = confirmedDeathRecord.getTotalDeathPerMillion();
                totalDeathPerMillion = totalDeathPerMillion.replaceAll(",","");

                confirmedDeathPerMillionSeries.getData().add(new XYChart.Data<>(Double.parseDouble(totalDeathPerMillion),countryName));
            }
            catch (NumberFormatException exception){
                confirmedDeathPerMillionSeries.getData().add(new XYChart.Data<>(0, countryName));
            }

            confirmedDeathRecord.setTotalDeath(confirmedDeathRecord.getTotalDeath());
            confirmedDeathRecord.setTotalDeathPerMillion(confirmedDeathRecord.getTotalDeathPerMillion());

            covidDeathTable.getItems().add(confirmedDeathRecord);
        }

        totalDeathBarChart.getData().add(totalConfirmedDeathSeries);
        perMillionBarChart.getData().add(confirmedDeathPerMillionSeries);
    }

    @FXML
    private Button generateChartButton;
    @FXML
    private Label nodeLabel;

    /**
     *  Set node in curve is hovered -> display country and corresponding datum in label.
     */
    private void setNodeHovered(){
        for (XYChart.Series<Number,Number> series : confirmedDeathLineChart.getData()){
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
                    nodeLabel.setText(series.getName() + "\n" + "Date : " + LocalDate.ofEpochDay((Long) datum.getXValue()).format(displayDateFormatter) + "\nDeath : " + datum.getYValue());
                });
                datum.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, exit->{
                    updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*2, false);
                    nodeLabel.setText("");
                });
            }
        }
    }

    /**
     * Set curve in lineChart is Hovered.
     */
    private void updatePath(Path seriesPath, Paint strokeColor, double strokeWidth, boolean toFront){
        seriesPath.setStroke(strokeColor);
        seriesPath.setStrokeWidth(strokeWidth);
        if(!toFront){ return; }
        seriesPath.toFront();
    }

    /**
     * Generate the curves corresponding to selected countries and period. <br>
     * @param event generate chart button is clicked
     */
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

        // update covidDeathChart
        confirmedDeathLineChart.getData().clear();

        chartXAxis.setLowerBound(startDate.toEpochDay());
        chartXAxis.setUpperBound(endDate.toEpochDay());

        confirmedDeath confirmedDeathData = new confirmedDeath(startDate,endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");
        HashMap<String,XYChart.Series<Number,Number>> confirmedDeathHashMap = confirmedDeathData.getConfirmedDeathChart();

        for (String countryName : selectedCountriesForChart){
            confirmedDeathLineChart.getData().add(confirmedDeathHashMap.get(countryName));
        }

        setNodeHovered();
    }

    @FXML
    private ImageView tableHomeImage;

    /**
     * Switch to the home scene.
     * @param event switchToHomeImage is clicked
     * @throws IOException
     */
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

    /**
     * Select all countries in the table.
     * @param event select all button in table tab is clicked.
     */
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

    /**
     * Select all countries in the table.
     * @param event select all button in chart tab is clicked.
     */
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
    private BarChart<Number,String> totalDeathBarChart;

    @FXML
    private BarChart<Number,String> perMillionBarChart;

    /**
     * Change the table/bar chart to be visible.
     * @param event "Table"/"Total Confirmed Death Bar Chart"/"Total Confirmed Death Per Million Bar Chart" radio button is clicked.
     */
    @FXML
    void getGraph(ActionEvent event) {
        if (tableRadioButton.isSelected()) {
            covidDeathTable.setVisible(true);
            totalDeathBarChart.setVisible(false);
            perMillionBarChart.setVisible(false);
        }
        else if (totalDeathRadioButton.isSelected()) {
            covidDeathTable.setVisible(false);
            totalDeathBarChart.setVisible(true);
            perMillionBarChart.setVisible(false);
        } else {
            covidDeathTable.setVisible(false);
            totalDeathBarChart.setVisible(false);
            perMillionBarChart.setVisible(true);
        }
    }

}
