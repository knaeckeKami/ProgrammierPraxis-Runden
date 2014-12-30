import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mkamleithner on 12/29/14.
 */
public class Histogram {

    /**
     * complete height of the histogram
     */
    private static final int HEIGHT = 16;
    /**
     * height of the histogram minus the x-axis
     */
    private static final int DRAWABLE_HEIGHT = HEIGHT - 1;
    private static final char BAR_SYMBOL = '#';
    private static final char BG_SYMBOL = '.';

    /**
     * returns a histogram of the given AsciiImage, which shows the relative distribution
     * of the different chars of the charset.
     *
     * @param image
     * @return
     */
    public static AsciiImage getHistogram(AsciiImage image) {
        String originalCharset = image.getCharset();
        //create the new charset for the histogram
        String newCharset = correctCharset(originalCharset);
        //create the empty histogram
        AsciiImage histogram = new AsciiImage(originalCharset.length() + 3, HEIGHT, newCharset);
        //count, which char how often occurs in the image
        Map<Character, Integer> charCountMap = countChars(image);
        Collection<Integer> values = charCountMap.values();
        //look up, how often the most common char occurs

        int maxValue = Integer.MIN_VALUE;
        for (Integer i : values) {
            maxValue = i > maxValue ? i : maxValue;
        }
        //calculate the percentage, how often the most common char occurs
        double maxPercent = (maxValue * 1.0 / (image.getHeight() * image.getWidth()) * 100.0);
        //scale the occurence of chars the pixels, round up using Math.ceil()
        Map<Character, Integer> charPixelCountMap = new HashMap<Character, Integer>();
        for (Character c : charCountMap.keySet()) {
            charPixelCountMap.put(c, (int) Math.ceil(1.0 * DRAWABLE_HEIGHT * charCountMap.get(c) / maxValue));
        }

        char[] charset = originalCharset.toCharArray();
        //draw the histogram
        for (int i = 0; i < DRAWABLE_HEIGHT; i++) {
            int currentPercent = (int) Math.round(1.0 * maxPercent - (maxPercent * i / DRAWABLE_HEIGHT));
            if (i % 2 == 0) {
                drawEvenRowDescriptor(histogram, i, currentPercent);
            }
            drawRow(histogram, charPixelCountMap, charset, i);
        }
        drawXAxis(histogram, originalCharset);
        return histogram;
    }

    /**
     * helper method for drawing a single row of the histogram without y axis description.
     *
     * @param histogram
     * @param charPixelCountMap
     * @param charset
     * @param row
     */
    private static void drawRow(AsciiImage histogram, Map<Character, Integer> charPixelCountMap, char[] charset, int row) {
        for (int j = 0; j < charset.length; j++) {
            Integer pixelForChar = charPixelCountMap.get(charset[j]);
            if (pixelForChar != null) {
                if (DRAWABLE_HEIGHT - row <= Math.ceil(pixelForChar)) {
                    histogram.setPixel(j + 3, row, BAR_SYMBOL);
                }
            }
        }
    }

    /**
     * returns a map, which contains for each character of the
     * image the absolute number of occurrences.
     *
     * @param image
     * @return
     */
    private static Map<Character, Integer> countChars(AsciiImage image) {
        HashMap<Character, Integer> charCountMap = new HashMap<Character, Integer>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                char currentChar = image.getPixel(x, y);
                if (charCountMap.containsKey(currentChar)) {
                    charCountMap.put(currentChar, charCountMap.get(currentChar) + 1);
                } else {
                    charCountMap.put(currentChar, 1);
                }
            }
        }
        return charCountMap;
    }

    /**
     * draws the y axis descriptor for the given row
     *
     * @param image
     * @param row
     * @param percent
     */
    private static void drawEvenRowDescriptor(AsciiImage image, int row, Integer percent) {
        if (percent > 100 || percent < 0) {
            throw new IllegalArgumentException("percent:" + percent);
        }
        char thirdDigit = Character.forDigit(percent / 100, 10);
        char secondDigit = Character.forDigit(percent / 10 % 10, 10);
        char firstDigit = Character.forDigit(percent % 10, 10);
        image.setPixel(2, row, firstDigit);
        if (secondDigit != '0' || thirdDigit == '1') {
            image.setPixel(1, row, secondDigit);
        }
        if (thirdDigit != '0') {
            image.setPixel(0, row, thirdDigit);
        }
    }

    /**
     * draws the x axis descriptor
     *
     * @param img
     * @param originalCharset
     */
    private static void drawXAxis(AsciiImage img, String originalCharset) {
        char[] charset = originalCharset.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            img.setPixel(3 + i, DRAWABLE_HEIGHT, charset[i]);
        }

    }

    /**
     * calculates the new charset for the histogram
     *
     * @param originalCharset
     * @return
     */
    private static String correctCharset(String originalCharset) {
        StringBuilder newCharsetBuilder = new StringBuilder(originalCharset);
        //add the '#' char to the charset if its not already in it
        int barSymbolIndex = newCharsetBuilder.indexOf("" + BAR_SYMBOL);
        if (barSymbolIndex == -1) {
            newCharsetBuilder.append(BAR_SYMBOL);
        }
        //add the digits 0-9 if not present
        for (char c = '0'; c <= '9'; c++) {
            if (newCharsetBuilder.indexOf("" + c) == -1) {
                newCharsetBuilder.append(c);
            }
        }
        //add the dot '.' if not present. if present, use it as background character.
        int bgSymbolIndex = newCharsetBuilder.indexOf("" + BG_SYMBOL);
        if (bgSymbolIndex != -1) {
            newCharsetBuilder.deleteCharAt(bgSymbolIndex);
        }
        newCharsetBuilder.append(".");
        return newCharsetBuilder.toString();
    }


}
