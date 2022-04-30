//package covidData;
//
//import org.apache.commons.csv.CSVRecord;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static comp3111.covid.DataAnalysis.getFileParser;
//import static org.junit.Assert.*;
//
//public class VaccinationRateTest {
//
//    @Test
//    public void getVaccinationRateTable() {
//        String dataset = "COVID_Dataset_v1.0.csv";
//
//        LocalDate d = LocalDate.of(2021, 7, 20);
//        System.out.println(d);
//        String[] c = new String[1];
//        c[0] = "Hong Kong";
////        c[1] = "IND";
////        c[2] = "ISR";
////        c[3] = "JPN";
////        c[4] = "SGP";
//
//        List<String> l = new ArrayList<>(Arrays.asList(c));
//
//        VaccinationRate v = new VaccinationRate(d, l, dataset);
//        System.out.println(d);
//        assertEquals(String.valueOf(2065375), v.getVaccinationRateTable().get("HKG").getFullyVaccinated());
////        assertEquals(86756243, v.getFullyVaccinated()[1]);
////        assertEquals(5257265, v.getFullyVaccinated()[2]);
////        assertEquals(29384382, v.getFullyVaccinated()[3]);
////        assertEquals(2792430, v.getFullyVaccinated()[4]);
//    }
//}