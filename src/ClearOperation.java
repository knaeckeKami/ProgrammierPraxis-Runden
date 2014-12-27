/**
 * Created by mkamleithner on 12/27/14.
 */
public class ClearOperation implements Operation {

    public AsciiImage execute(AsciiImage img) throws OperationException {
        AsciiImage newImage = new AsciiImage(img);
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                newImage.setPixel(x, y, newImage.getBackgroundCharacter());
            }
        }
        return newImage;
    }
}
