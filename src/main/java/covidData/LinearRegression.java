package covidData;

import java.util.List;

/**
 *  Instances of linearRegression calculate the equation of regression line based on input (x,y) pair
 */
public class LinearRegression {

    private final double intercept, slope;
    private final double r2;
    private final double svar0, svar1;

    /**
     * Calculate the regression line based on input list of x and list of y
     * @param x List of x values
     * @param y List of y values
     */
    public LinearRegression(List<Double> x, List<Double> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.size();

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx  += x.get(i);
            sumx2 += x.get(i)*x.get(i);
            sumy  += y.get(i);
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x.get(i)- xbar) * (x.get(i) - xbar);
            yybar += (y.get(i) - ybar) * (y.get(i) - ybar);
            xybar += (x.get(i) - xbar) * (y.get(i) - ybar);
        }
        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;

        // more statistical analysis
        double rss = 0.0;      // residual sum of squares
        double ssr = 0.0;      // regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = slope*x.get(i) + intercept;
            rss += (fit - y.get(i)) * (fit - y.get(i));
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = n-2;
        r2    = ssr / yybar;
        double svar  = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar/n + xbar*xbar*svar1;
    }

    /**
     * @return intercept
     */
    public double intercept() {
        return intercept;
    }

    /**
     *
     * @return slope
     */
    public double slope() {
        return slope;
    }

    /**
     *
     * @return r^2
     */
    public double R2() {
        return r2;
    }

    /**
     * use x datum value to predict y value
     * @param x x datum value
     * @return predicted y value
     */
    public double predict(double x) {
        return slope*x + intercept;
    }


    public String toString() {
        return "y = " + slope + "x + " + intercept;
    }
}