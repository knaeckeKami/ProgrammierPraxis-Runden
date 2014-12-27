import java.util.Arrays;
import java.util.Set;

/**
 * Created by mkamleithner on 12/27/14.
 */
public class MedianOperation implements Operation {

    public AsciiImage execute(AsciiImage img) {
        final AsciiImage newImage = new AsciiImage(img);
        final String charset = newImage.getCharset();
        final int[] neighborBrightnessValues = new int[9];

        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                //get all neighbors of the pixel
                Set<AsciiPoint> neighbors = img.get8Neighbors(x, y);
                //add the brightness of the pixel itself to the brightness values
                int i = 1;
                neighborBrightnessValues[0] = charset.indexOf(img.getPixel(x, y));
                //add the brightness values of the neighbors
                for (AsciiPoint point : neighbors) {
                    neighborBrightnessValues[i++] = charset.indexOf(img.getPixel(point));
                }
                //if the pixel was on the edge of the image, fill the rest of the pixels
                //up with the brightest value
                while (i < 9) {
                    neighborBrightnessValues[i++] = charset.length() - 1;
                }
                Arrays.sort(neighborBrightnessValues);
                //set the pixel to the pixel with the median brightness value of all
                //surrounding pixels
                newImage.setPixel(x, y, charset.charAt(neighborBrightnessValues[4]));
            }
        }
        return newImage;
    }
}
