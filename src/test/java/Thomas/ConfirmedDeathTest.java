package Thomas;

import covidData.confirmedDeath;
import covidData.confirmedDeathRecord;
import javafx.scene.chart.XYChart;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class ConfirmedDeathTest {
    String dataset;
    LocalDate startDate;
    LocalDate endDate;
    HashSet<String> countries;
    confirmedDeath confirmedDeathRecord;

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
    public void getconfirmedDeathTable() {
        confirmedDeathRecord = new confirmedDeath(endDate,countries,dataset);
        HashMap<String, confirmedDeathRecord> confirmedDeathTable = confirmedDeathRecord.getconfirmedDeathTable();

        confirmedDeathRecord HongKongRecord = confirmedDeathTable.get("Hong Kong");
        assertEquals(HongKongRecord.getTotalDeath(), "212");
        assertEquals(HongKongRecord.getTotalDeathPerMillion(),"28.28");
        assertEquals(HongKongRecord.getCountry(),"Hong Kong");

        HongKongRecord.setCountry("Hong Kong");
    }

    @Test
    public void getConfirmedDeathChart() {
        confirmedDeathRecord = new confirmedDeath(startDate,endDate,countries,dataset);
        HashMap<String, XYChart.Series<Number,Number>> confirmedDeathChart = confirmedDeathRecord.getConfirmedDeathChart();

        XYChart.Series<Number,Number> HongKongRecord = confirmedDeathChart.get("Hong Kong");
        LocalDate localDate1 = LocalDate.of(2021,7,2);
        LocalDate localDate2 = LocalDate.of(2021,7,21);

        Boolean found1 = false;
        Boolean found2 = false;
        for (XYChart.Data<Number,Number> datum : HongKongRecord.getData()){
            if (datum.getXValue().equals(localDate1.toEpochDay())) {
                if (datum.getYValue().equals(28.145))
                    found1 = true;
            }
            if (datum.getXValue().equals(localDate2.toEpochDay()))
                found2 = true;
        }

        assertTrue(found1);
        assertFalse(found2);
    }

    @Test
    public void getconfirmedDeathTableWithOutdatedData() {
        confirmedDeathRecord = new confirmedDeath(LocalDate.of(2021,7,21),countries,dataset);
        HashMap<String, confirmedDeathRecord> confirmedDeathTable = confirmedDeathRecord.getconfirmedDeathTable();

        confirmedDeathRecord HongKongRecord = confirmedDeathTable.get("Hong Kong");
        String[] totalDeath = HongKongRecord.getTotalDeath().split("\n");

        assertEquals(totalDeath[0].trim(), "212");
        assertEquals(totalDeath[1].trim(), "last found on 2021-07-20");

        String[] totalDeathPerMillion = HongKongRecord.getTotalDeathPerMillion().split("\n");
        assertEquals(totalDeathPerMillion[0].trim(),"28.28");
        assertEquals(totalDeathPerMillion[1].trim(),"last found on 2021-07-20");
    }

    @Test
    public void getstartDate(){
        confirmedDeathRecord = new confirmedDeath(endDate,countries,dataset);
        assertEquals(confirmedDeathRecord.getstartDate(),endDate);
    }

    @Test
    public void getendDate(){
        confirmedDeathRecord = new confirmedDeath(endDate,countries,dataset);
        assertEquals(confirmedDeathRecord.getendDate(),endDate);
    }
}
