package endava.devweek.jointhecode.stockexchange;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DisplayName("GainOptimizer test")
public class GainOptimizerTest {
    @Test
    public void shouldSucceedWithSmall() throws InvalidInputException {
        String input = "19.35 19.30 18.88 18.93 18.95 19.03 19.00 18.97 18.97 18.98";
        PriceGain expected = new PriceGain(18.88f, 19.03f);

        PriceGain actualResult = GainOptimizer.optimize(input);

        Assertions.assertEquals(expected, actualResult);
    }

    @Test
    public void shouldSucceedWithLarge() throws InvalidInputException {
        String input = "9.20 8.03 10.02 8.08 8.14 8.10 8.31 8.28 8.35 8.34 8.39 8.45 8.38 8.38 8.32 8.36 8.28 8.28 8.38 " +
                "8.48 8.49 8.54 8.73 8.72 8.76 8.74 8.87 8.82 8.81 8.82 8.85 8.85 8.86 8.63 8.70 8.68 8.72 8.77 8.69 8.65 " +
                "8.70 8.98 8.98 8.87 8.71 9.17 9.34 9.28 8.98 9.02 9.16 9.15 9.07 9.14 9.13 9.10 9.16 9.06 9.10 9.15 9.11 " +
                "8.72 8.86 8.83 8.70 8.69 8.73 8.73 8.67 8.70 8.69 8.81 8.82 8.83 8.91 8.80 8.97 8.86 8.81 8.87 8.82 8.78 " +
                "8.82 8.77 8.54 8.32 8.33 8.32 8.51 8.53 8.52 8.41 8.55 8.31 8.38 8.34 8.34 8.19 8.17 8.16";
        PriceGain expected = new PriceGain(8.03f, 9.34f);

        PriceGain actualResult = GainOptimizer.optimize(input);

        Assertions.assertEquals(expected, actualResult);
    }

    @Test
    public void shouldSucceedWith3Values() throws InvalidInputException {
        String input = "10.30 5.20 5.00";
        PriceGain expected = new PriceGain(10.3f, 5.f);

        PriceGain actualResult = GainOptimizer.optimize(input);

        Assertions.assertEquals(expected, actualResult);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowNumberFormatException() throws InvalidInputException {
        String input = "3 4 this is a number";

        PriceGain actualResult = GainOptimizer.optimize(input);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputException() throws InvalidInputException {
        String input = "3 4";

        PriceGain actualResult = GainOptimizer.optimize(input);
    }
}
