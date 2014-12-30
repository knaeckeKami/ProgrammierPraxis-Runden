/**
 * Created by mkamleithner on 12/29/14.
 */
public class BinaryOperation implements Operation {

    private char threshold;

    public BinaryOperation(char threshold) {
        this.threshold = threshold;
    }

    /**
     * Returns a new AsciiImage, which contains only the darkest and brightest
     * character of the original image.
     * All Characters, which are darker then the threshold-character, will be set to the
     * darkest character, all other character will be set to the brightest character.
     *
     * @param img The AsciiImage to use as basis for executing the Operation, it will remain
     *            unchanged
     * @return
     * @throws OperationException
     */
    public AsciiImage execute(AsciiImage img) throws OperationException {
        AsciiImage newImage = new AsciiImage(img);
        String charset = newImage.getCharset();
        char brightestChar = newImage.getBackgroundCharacter();
        char darkestChar = charset.charAt(0);
        int indexOfThreshold = charset.indexOf(threshold);
        if (indexOfThreshold == -1) {
            throw new OperationException();
        }
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                if (charset.indexOf(newImage.getPixel(x, y)) < indexOfThreshold) {
                    newImage.setPixel(x, y, darkestChar);
                } else {
                    newImage.setPixel(x, y, brightestChar);
                }
            }
        }
        return newImage;

    }
}
