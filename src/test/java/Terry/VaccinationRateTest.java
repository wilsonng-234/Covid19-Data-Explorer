package Terry;

import covidData.ConfirmedCases;
import covidData.ConfirmedCasesRecord;
import covidData.VaccinationRate;
import covidData.VaccinationRateRecord;
import javafx.scene.chart.XYChart;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class VaccinationRateTest {
    String dataset;
    LocalDate startDate;
    LocalDate endDate;
    HashSet<String> countries;
    VaccinationRate vaccinationRateRecord;

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
    public void getVaccinationRateTable() {
        vaccinationRateRecord = new VaccinationRate(endDate,countries,dataset);
        HashMap<String, VaccinationRateRecord> vaccinationRateTable = vaccinationRateRecord.getVaccinationRateTable();

        VaccinationRateRecord HongKongRecord = vaccinationRateTable.get("Hong Kong");
        assertEquals(HongKongRecord.getFullyVaccinated(), "2,065,375");
        assertEquals(HongKongRecord.getRateOfVaccination(),"27.55%");
        assertEquals(HongKongRecord.getCountry(),"Hong Kong");

        HongKongRecord.setCountry("Hong Kong");
    }

    @Test
    public void getVaccinationRateChart() {
        vaccinationRateRecord = new VaccinationRate(startDate,endDate,countries,dataset);
        HashMap<String, XYChart.Series<Number,Number>> vaccinationRateChart = vaccinationRateRecord.getVaccinationRateChart();

        XYChart.Series<Number,Number> HongKongRecord = vaccinationRateChart.get("Hong Kong");
        LocalDate localDate1 = LocalDate.of(2021,7,2);
        LocalDate localDate2 = LocalDate.of(2021,7,21);

        final double THRESHOLD = .0001;
        boolean found1 = false;
        boolean found2 = false;
        for (XYChart.Data<Number,Number> datum : HongKongRecord.getData()){
            if (datum.getXValue().equals(localDate1.toEpochDay())) {
                if (Math.abs(datum.getYValue().doubleValue() - 20.42) < THRESHOLD)
                    found1 = true;
            }
            if (datum.getXValue().equals(localDate2.toEpochDay()))
                found2 = true;
        }

        assertTrue(found1);
        assertFalse(found2);
    }

    @Test
    public void getVaccinationRateTableWithOutdatedData() {
        vaccinationRateRecord = new VaccinationRate(LocalDate.of(2021,7,21),countries,dataset);
        HashMap<String, VaccinationRateRecord> vaccinationRateTable = vaccinationRateRecord.getVaccinationRateTable();

        VaccinationRateRecord HongKongRecord = vaccinationRateTable.get("Hong Kong");
        String[] fullyVaccinated = HongKongRecord.getFullyVaccinated().split("\n");

        assertEquals("2,065,375", fullyVaccinated[0].trim());
        assertEquals("last found on 2021-07-20", fullyVaccinated[1].trim());

        String[] totalCasesPerMillion = HongKongRecord.getRateOfVaccination().split("\n");
        assertEquals("27.55%", totalCasesPerMillion[0].trim());
        assertEquals("last found on 2021-07-20", totalCasesPerMillion[1].trim());
    }

    @Test
    public void getStartDate(){
        vaccinationRateRecord = new VaccinationRate(endDate,countries,dataset);
        assertEquals(vaccinationRateRecord.getstartDate(),endDate);
    }

    @Test
    public void getEndDate(){
        vaccinationRateRecord = new VaccinationRate(endDate,countries,dataset);
        assertEquals(vaccinationRateRecord.getendDate(),endDate);
    }
}
