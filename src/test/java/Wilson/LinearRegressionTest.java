package Wilson;

import covidData.LinearRegression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class LinearRegressionTest {
    List<Double> x,y;

    @Before
    public void setup(){
        final int n = 4;

        x = new ArrayList<>(n);
        y = new ArrayList<>(n);

        x.add(-1.0);    x.add(0.0);     x.add(1.0);     x.add(2.0);
        y.add(0.0);     y.add(2.0);     y.add(4.0);     y.add(5.0);
    }

    @Test
    public void intercept() {
        LinearRegression linearRegression = new LinearRegression(x,y);

        assertEquals(0, Double.compare(linearRegression.intercept(), 1.9));
    }

    @Test
    public void slope() {
        LinearRegression linearRegression = new LinearRegression(x,y);

        assertEquals(0, Double.compare(linearRegression.slope(), 1.7));
    }

    @Test
    public void r2() {
        LinearRegression linearRegression = new LinearRegression(x,y);
        Double epsilon = 0.0001;

        System.out.println(linearRegression.R2());
        assertTrue(Math.abs(linearRegression.R2()-0.979661016949) <= epsilon);
    }

    @Test
    public void predict() {
        LinearRegression linearRegression = new LinearRegression(x,y);
        Double x = 1.7;

        assertEquals(0, (Double.compare(linearRegression.predict(x), x * linearRegression.slope() + linearRegression.intercept())));
    }

    @Test
    public void exception() {
        try{
            x.add(1.0);
            LinearRegression linearRegression = new LinearRegression(x,y);
        }
        catch (IllegalArgumentException lengthNotEqual){
            assert(true);
        }
        finally {
            x.remove(x.size()-1);
        }
    }
}
