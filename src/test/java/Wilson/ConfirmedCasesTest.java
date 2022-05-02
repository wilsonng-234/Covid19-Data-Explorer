package Wilson;

import covidData.ConfirmedCases;
import covidData.ConfirmedCasesRecord;
import javafx.scene.chart.XYChart;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class ConfirmedCasesTest {
    String dataset;
    LocalDate startDate;
    LocalDate endDate;
    HashSet<String> countries;
    ConfirmedCases confirmedCasesRecord;

    @Before
    public void setUp(){
        dataset = "COVID_Dataset_v1.0.csv";
        startDate = LocalDate.of(2020,3,1);
        endDate = LocalDate.of(2021,7,20);
        countries = new HashSet<>();
        countries.add("Hong Kong");
        countries.add("India");
        countries.add("Israel");
        countries.add("Japan");
        countries.add("Macao");
        countries.add("Singapore");
        countries.add("United Kingdom");
        countries.add("United States");
        countries.add("World");
    }

    @Test
    public void getconfirmedCasesTable() {
        confirmedCasesRecord = new ConfirmedCases(endDate,countries,dataset);
        HashMap<String, ConfirmedCasesRecord> confirmedCasesTable = confirmedCasesRecord.getconfirmedCasesTable();

        ConfirmedCasesRecord HongKongRecord = confirmedCasesTable.get("Hong Kong");
        assertEquals(HongKongRecord.getTotalCases(), "11,965");
        assertEquals(HongKongRecord.getTotalCasesPerMillion(),"1,595.97");
        assertEquals(HongKongRecord.getCountry(),"Hong Kong");

        HongKongRecord.setCountry("Hong Kong");
    }

    @Test
    public void getConfirmedCasesChart() {
        confirmedCasesRecord = new ConfirmedCases(startDate,endDate,countries,dataset);
        HashMap<String, XYChart.Series<Number,Number>> confirmedCasesChart = confirmedCasesRecord.getConfirmedCasesChart();

        XYChart.Series<Number,Number> HongKongRecord = confirmedCasesChart.get("Hong Kong");
        LocalDate localDate1 = LocalDate.of(2021,7,2);
        LocalDate localDate2 = LocalDate.of(2021,7,21);

        Boolean found1 = false;
        Boolean found2 = false;
        for (XYChart.Data<Number,Number> datum : HongKongRecord.getData()){
            if (datum.getXValue().equals(localDate1.toEpochDay())) {
                if (datum.getYValue().equals(1592.373))
                    found1 = true;
            }
            if (datum.getXValue().equals(localDate2.toEpochDay()))
                found2 = true;
        }

        assertTrue(found1);
        assertFalse(found2);
    }

    @Test
    public void getconfirmedCasesTableWithOutdatedData() {
        confirmedCasesRecord = new ConfirmedCases(LocalDate.of(2021,7,21),countries,dataset);
        HashMap<String, ConfirmedCasesRecord> confirmedCasesTable = confirmedCasesRecord.getconfirmedCasesTable();

        ConfirmedCasesRecord HongKongRecord = confirmedCasesTable.get("Hong Kong");
        String[] totalCases = HongKongRecord.getTotalCases().split("\n");

        assertEquals(totalCases[0].trim(), "11,965");
        assertEquals(totalCases[1].trim(), "last found on 2021-07-20");

        String[] totalCasesPerMillion = HongKongRecord.getTotalCasesPerMillion().split("\n");
        assertEquals(totalCasesPerMillion[0].trim(),"1,595.97");
        assertEquals(totalCasesPerMillion[1].trim(),"last found on 2021-07-20");
    }

    @Test
    public void getstartDate(){
        confirmedCasesRecord = new ConfirmedCases(endDate,countries,dataset);
        assertEquals(confirmedCasesRecord.getstartDate(),endDate);
    }

    @Test
    public void getendDate(){
        confirmedCasesRecord = new ConfirmedCases(endDate,countries,dataset);
        assertEquals(confirmedCasesRecord.getendDate(),endDate);
    }
}
