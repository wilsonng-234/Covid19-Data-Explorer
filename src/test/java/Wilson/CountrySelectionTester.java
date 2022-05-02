package Wilson;

import covidData.CountrySelection;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class CountrySelectionTester extends ApplicationTest {
    @Before
    public void setup(){}

    @Test
    public void Test() {
        CountrySelection africa = new CountrySelection("Africa");
        CountrySelection hong_kong = new CountrySelection("Hong Kong");

        assertTrue(africa.equals(new CountrySelection("Africa")));
        assertFalse(africa.equals(hong_kong));
        assertFalse(africa.equals(new Double(1.0)));

        assertEquals(africa.compareTo(hong_kong),-7);
        assertEquals(africa.compareTo(africa),0);
        assertEquals(hong_kong.compareTo(africa),7);
    }
}
