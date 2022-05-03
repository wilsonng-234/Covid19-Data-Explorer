package comp3111.covid;

import covidData.VaccinationRate;
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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

/**
 *  VaccinationRateController initializes VaccinationRate scene
 */
public class VaccinationRateController implements Initializable {
    String dataset = "COVID_Dataset_v1.0.csv";
    DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("MMM d,yyyy", Locale.ENGLISH);
    private final List<String> defaultCountries = Arrays.asList("Hong Kong", "India", "Israel", "Japan", "Singapore", "United Kingdom", "United States", "World");
    String tableTitle = "Rate of Vaccination against COVID-19 as of ";
    String tableColumn1 = "Fully Vaccinated";
    String tableColumn2 = "Rate of Vaccination";
    String chartTitle = "Cumulative Rate of Vaccination against COVID-19";
    String chartAxisLabel1 = "Fully Vaccinated Number";
    String chartAxisLabel2 = "Fully Vaccinated per Hundred People(%)";

    // tab
    @FXML
    private Tab tableTab;
    @FXML
    private Tab chartTab;
    @FXML
    private Tab relationTab;

    // datePicker
    @FXML
    private DatePicker tableDatePicker;
    private LocalDate tableDate = null;

    @FXML
    private DatePicker startDatePicker;
    private LocalDate startDate = null;

    @FXML
    private DatePicker endDatePicker;
    private LocalDate endDate = null;

    // country selection table
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

    // table tab
    @FXML
    private Text tableTitleText;
    @FXML
    private TableView<VaccinationRateRecord> vaccinationRateTable;
    @FXML
    private TableColumn<VaccinationRateRecord,String> countryColumn;
    @FXML
    private TableColumn<VaccinationRateRecord,String> fullyColumn;
    @FXML
    private TableColumn<VaccinationRateRecord,String> rateColumn;

    @FXML
    private ScrollPane fullyBarCharScrollPane;
    @FXML
    private BarChart<Number, String> fullyBarChart;
    @FXML
    private CategoryAxis fullyBarChartXAxis;
    @FXML
    private NumberAxis fullyBarChartYAxis;
    @FXML
    private ScrollPane rateBarCharScrollPane;
    @FXML
    private BarChart<Number, String> rateBarChart;
    @FXML
    private CategoryAxis rateBarChartXAxis;
    @FXML
    private NumberAxis rateBarChartYAxis;
    @FXML
    private RadioButton tableRadioButton;
    @FXML
    private RadioButton fullyBarChartRadioButton;
    @FXML
    private RadioButton rateBarChartRadioButton;
    @FXML
    private ToggleGroup tableOrChart;
    @FXML
    private Label remarkForBarChartLabel;
    // -----------

    // line chart tab
    @FXML
    private LineChart<Number,Number> vaccinationRateLineChart;
    @FXML
    private NumberAxis lineChartXAxis;
    @FXML
    private NumberAxis lineChartYAxis;
    @FXML
    private Label nodeLabel;
    // -----------

    HashSet<String> selectedCountriesForTable = new HashSet<>();
    HashSet<String> selectedCountriesForChart = new HashSet<>();

    /**
     * add listener to date picker
     * bind table title width with table width
     * set table title date when date is selected
     * set default value for radio button and stack pane
     */
    private void initDailyTabView() {
        tableTitleText.wrappingWidthProperty().bind(
                vaccinationRateTable.widthProperty()
        );

        tableDatePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tableDate = newValue;
                    tableTitleText.setText(tableTitle + newValue.format(displayDateFormatter));
                }
        );

        tableRadioButton.setSelected(true);
        fullyBarCharScrollPane.setVisible(false);
        rateBarCharScrollPane.setVisible(false);
    }

    /**
     *  Initialize cells in vaccinationRateTable.
     *  Bind column width with table width.
     *  Set column display alignment.
     *  Initialize bar chart x-axis,y-axis property
     */
    private void setTableAndBarChar(){
        // table
//        LocalDate date = LocalDate.of(2021, 7, 20);
//        tableDatePicker.setValue(date);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        fullyColumn.setCellValueFactory(new PropertyValueFactory<>("fullyVaccinated"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rateOfVaccination"));

        countryColumn.prefWidthProperty().bind(
                vaccinationRateTable.widthProperty().divide(3.1)
        );
        fullyColumn.prefWidthProperty().bind(
                vaccinationRateTable.widthProperty().divide(3.1)
        );
        rateColumn.prefWidthProperty().bind(
                vaccinationRateTable.widthProperty().divide(3.1)
        );

        fullyColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        rateColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        // bar chart
        fullyBarChart.prefWidthProperty().bind(fullyBarCharScrollPane.widthProperty().divide(1.1));
        fullyBarChart.setPrefHeight(445);
        fullyBarChart.animatedProperty().setValue(false);
        rateBarChart.prefWidthProperty().bind(rateBarCharScrollPane.widthProperty().divide(1.1));
        rateBarChart.setPrefHeight(445);
        rateBarChart.animatedProperty().setValue(false);
        fullyColumn.setText(tableColumn1);
        rateColumn.setText(tableColumn2);
        remarkForBarChartLabel.setVisible(false);

        fullyBarChartXAxis.setLabel("Country");
        fullyBarChartYAxis.setLabel(chartAxisLabel1);

        rateBarChartXAxis.setLabel("Country");
        rateBarChartYAxis.setLabel(chartAxisLabel2);
        rateBarChartYAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(lineChartYAxis, null, "%"));
    }

    /**
     * add listener to start and end date picker
     */
    private void initPeriodTab() {
        startDatePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> startDate = newValue
        );
        endDatePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> endDate = newValue
        );
    }

    /**
     *  Initialize vaccinationRate LineChart x-axis,y-axis property.
     */
    private void setLineChart() {
//        LocalDate sd = LocalDate.of(2020, 12, 27);
//        LocalDate ed = LocalDate.of(2021, 7, 20);
//        startDatePicker.setValue(sd);
//        endDatePicker.setValue(ed);

        vaccinationRateLineChart.setTitle(chartTitle);
        vaccinationRateLineChart.animatedProperty().setValue(false);

        lineChartXAxis.setLabel("Date");
        lineChartXAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(lineChartXAxis) {
            @Override
            public String toString(final Number object) {
                long longValue = object.longValue();
                LocalDate date = LocalDate.ofEpochDay(longValue);
                return date.format(displayDateFormatter);
            }
        });
        lineChartYAxis.setLabel(chartAxisLabel2);
        lineChartYAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(lineChartYAxis, null, "%"));

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
                                //assert (!selectedCountries.contains(row.getName()));

                                selectedCountries.add(row.getName());
                            }
                            else{
                                //assert (selectedCountries.contains(row.getName()));

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
     * Sort CountrySelectionColumn to put selected countries at top.
     *
     * @param countrySelectionTable The countrySelectionTable that user is using.
     */
    void sortCountrySelectionColumn(TableView<CountrySelection> countrySelectionTable) {
        // once chosen, will move up to the top
        countrySelectionTable.getItems().sort((o1,o2) -> {
            if (o1.getSelect().isSelected() && !o2.getSelect().isSelected())
                return -1;
            else if (!o1.getSelect().isSelected() && o2.getSelect().isSelected())
                return 1;
            else {
                return o1.getName().compareTo(o2.getName());
            }
        });
        countrySelectionTable.sort();

    }


//    void initCountrySelectionCheckBox(List<CountrySelection> countrySelections) {
//        for (CountrySelection row : countrySelections) {
//            if (defaultCountries.contains(row.getName())){
//                row.getSelect().setSelected(true);
//            }
//        }
//    }

    /**
     * UI initialization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // datePicker get date

        initDailyTabView();
        setTableAndBarChar();

        initPeriodTab();
        setLineChart();

        setCountrySelectionTable(countrySelectionTableForTable,countrySelectionColumnForTable,checkBoxSelectionColumnForTable,selectedCountriesForTable);
        setCountrySelectionTable(countrySelectionTableForChart,countrySelectionColumnForChart,checkBoxSelectionColumnForChart,selectedCountriesForChart);

//        initCountrySelectionCheckBox(countrySelectionList);
//        initCountrySelectionCheckBox(countrySelectionList1);

    }

    @FXML
    private Button generateTableButton;

    /**
     *  Generate the table and bar chart corresponding to selected countries and period.
     * @param event generate chart button is clicked
     */
    @FXML
    void generateTableButtonClicked(ActionEvent event) {
        //check validity
        if (tableDate == null) {
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

        List<String> sortedSelectedCountriesList = new ArrayList<>(selectedCountriesForTable.size());
        for (String countryName : selectedCountriesForTable)
            sortedSelectedCountriesList.add(countryName);
        Collections.sort(sortedSelectedCountriesList);

        // generate table and bar char
        vaccinationRateTable.getItems().clear();

        fullyBarChart.getData().clear();
        rateBarChart.getData().clear();

        VaccinationRate vaccinationRate = new VaccinationRate(tableDate, selectedCountriesForTable,"COVID_Dataset_v1.0.csv");
        HashMap<String, VaccinationRateRecord> vaccinationRateHashMap = vaccinationRate.getVaccinationRateTable();

        XYChart.Series<Number, String> fullySeries = new XYChart.Series<>();
        fullySeries.setName("fully vaccinated");
        XYChart.Series<Number, String> rateSeries = new XYChart.Series<>();
        rateSeries.setName("rate of vaccination");

        NumberFormat numberFormat = NumberFormat.getInstance();
        for (String countryName : sortedSelectedCountriesList) {
            VaccinationRateRecord record = vaccinationRateHashMap.get(countryName);

            try{
                String fullyVaccinated = record.getFullyVaccinated();
                fullyVaccinated = fullyVaccinated.replaceAll(",","");

                fullySeries.getData().add(new XYChart.Data<>(Integer.parseInt(fullyVaccinated),countryName));
            }
            catch(NumberFormatException exception){
                fullySeries.getData().add(new XYChart.Data<>(0, countryName));
            }

            try{
                String rateOfVaccination = record.getRateOfVaccination();
                rateOfVaccination = rateOfVaccination.replaceAll("%","");

                rateSeries.getData().add(new XYChart.Data<>(Double.parseDouble(rateOfVaccination),countryName));
            }
            catch (NumberFormatException exception){
                rateSeries.getData().add(new XYChart.Data<>(0, countryName));
            }

            record.setFullyVaccinated(record.getFullyVaccinated());
            record.setRateOfVaccination(record.getRateOfVaccination());

            vaccinationRateTable.getItems().add(record);
        }

        if (selectedCountriesForTable.size() > 10) {
            fullyBarChart.setPrefHeight(selectedCountriesForTable.size()*50);
            rateBarChart.setPrefHeight(selectedCountriesForTable.size()*50);
        }
        else {
            fullyBarChart.setPrefHeight(445);
            rateBarChart.setPrefHeight(445);
        }
        fullyBarChart.getData().add(fullySeries);
        rateBarChart.getData().add(rateSeries);
    }

    @FXML
    private Button generateChartButton;


    /**
     *  Generate the curves corresponding to selected countries and period.
     * @param event generate chart button is clicked
     */
    @FXML
    void generateChartButtonClicked(ActionEvent event) {
        //check validity
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

        // generate line chart
        vaccinationRateLineChart.getData().clear();
        lineChartXAxis.setAutoRanging(false);
        lineChartXAxis.setLowerBound(startDate.toEpochDay());
        lineChartXAxis.setUpperBound(endDate.toEpochDay());
        VaccinationRate vaccinationRate = new VaccinationRate(startDate, endDate, selectedCountriesForChart,"COVID_Dataset_v1.0.csv");
        HashMap<String,XYChart.Series<Number,Number>> vaccinationRateHashMap = vaccinationRate.getVaccinationRateChart();
        for (String countryName : selectedCountriesForChart){
            vaccinationRateLineChart.getData().add(vaccinationRateHashMap.get(countryName));
        }

        // add event handler to every node in lines
        Collection<XYChart.Series<Number,Number>> seriesList = vaccinationRateHashMap.values();
        for(XYChart.Series<Number,Number> series : seriesList){
            //Setup for hovering on series (cleaner)
            Path seriesPath = (Path) series.getNode();
            double initialStrokeWidth = seriesPath.getStrokeWidth();

            seriesPath.setOnMouseEntered(e -> {
                updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*4, true);
                nodeLabel.setText(series.getName() + "\n " + "\n ");
            });
            seriesPath.setOnMouseExited(e -> {
                updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*2, false);
                nodeLabel.setText("");
            });

            for (XYChart.Data<Number,Number> data : series.getData()) {
//                data.getNode().setVisible(false);
                data.getNode().setStyle("-fx-background-color: transparent, transparent;\n"
                        + "    -fx-background-insets: 0, 2;\n"
                        + "    -fx-background-radius: 5px;\n"
                        + "    -fx-padding: 5px;");

                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event1 -> {
                    updatePath(seriesPath, seriesPath.strokeProperty().get(), initialStrokeWidth*4, true);
                    nodeLabel.setText(series.getName()+ "\n" + "Date : " + LocalDate.ofEpochDay((Long) data.getXValue()).format(displayDateFormatter) + "\nRate : " + data.getYValue() + "%");
                });
                data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event2 -> {
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
        clickAllOrNone(selectAllForTable, countrySelectionTableForTable);
    }

    @FXML
    private CheckBox selectAllForChart;

    /**
     * Select all countries in the chart.
     * @param event select all button in chart tab is clicked.
     */
    @FXML
    void selectAllForChartClicked(ActionEvent event) {
        clickAllOrNone(selectAllForChart, countrySelectionTableForChart);
    }

    void clickAllOrNone(CheckBox selectAll, TableView<CountrySelection> countrySelection) {
        boolean tick = selectAll.selectedProperty().get();
        if (tick)
            for (int i=0; i<countrySelection.getItems().size(); i++){
                CountrySelection row = countrySelection.getItems().get(i);
                row.getSelect().setSelected(true);
            }
        else
            for (int i=0; i<countrySelection.getItems().size(); i++){
                CountrySelection row = countrySelection.getItems().get(0);
                row.getSelect().setSelected(false);
            }
    }

    /**
     * Change the table/bar chart to be visible.
     * @param event "Table"/"Total Confirmed Cases Bar Chart"/"Total Confirmed Cases Per Million Bar Chart" radio button is clicked.
     */
    public void getGraph(ActionEvent event) {
        if (tableRadioButton.isSelected()) {
            vaccinationRateTable.setVisible(true);
            fullyBarCharScrollPane.setVisible(false);
            rateBarCharScrollPane.setVisible(false);
            remarkForBarChartLabel.setVisible(false);
        }
        else if (fullyBarChartRadioButton.isSelected()) {
            vaccinationRateTable.setVisible(false);
            fullyBarCharScrollPane.setVisible(true);
            rateBarCharScrollPane.setVisible(false);
            remarkForBarChartLabel.setVisible(true);
        } else {
            vaccinationRateTable.setVisible(false);
            fullyBarCharScrollPane.setVisible(false);
            rateBarCharScrollPane.setVisible(true);
            remarkForBarChartLabel.setVisible(true);
        }

    }
}