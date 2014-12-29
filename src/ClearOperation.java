/**
 * Created by mkamleithner on 12/27/14.
 */
public class ClearOperation implements Operation {
    /**
     * Sets all characters of the image to the background image.
     *
     * @param img The AsciiImage to use as basis for executing the Operation, it will remain
     *            unchanged
     * @return new image with only background characters.
     */
    public AsciiImage execute(AsciiImage img) {
        AsciiImage newImage = new AsciiImage(img.getWidth(), img.getHeight(), img.getCharset());
        return newImage;
    }
}
