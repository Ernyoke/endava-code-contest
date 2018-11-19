package endava.devweek.jointhecode.stockexchange;

public class GainOptimizer {
    private static float[] getNumericValues(String text) throws NumberFormatException {
        String[] words = text.split(" ");
        float[] numericValues = new float[words.length];
        for (int i = 0; i < words.length; i++) {
            numericValues[i] = Float.parseFloat(words[i]);
        }
        return numericValues;
    }

    public static PriceGain optimize(String text) throws NumberFormatException, InvalidInputException {
        float[] values = getNumericValues(text);
        if (values.length < 3) {
            throw new InvalidInputException("Input size should be at least 3!");
        }
        float[] partialMinValues = new float[values.length];
        float[] partialMaxValues = new float[values.length];

        float min = Float.MAX_VALUE;
        partialMinValues[0] = min;
        for (int i = 0; i < values.length - 1; i++) {
            partialMinValues[i + 1] = min;
            min = Math.min(values[i], min);
        }

        float max = -Float.MAX_VALUE;
        for (int i = values.length - 1; i >= 0; i--) {
            max = Math.max(values[i], max);
            partialMaxValues[i] = max;
        }

        float maxDiff = -Float.MAX_VALUE;
        PriceGain priceGain = new PriceGain();
        for (int i = 0; i < values.length; i++) {
            float currentDiff = partialMaxValues[i] - partialMinValues[i];
            if (maxDiff < currentDiff) {
                maxDiff = currentDiff;
                priceGain.setBuyPoint(partialMinValues[i]);
                priceGain.setSellPoint(partialMaxValues[i]);
            }
        }

        return priceGain;
    }
}
