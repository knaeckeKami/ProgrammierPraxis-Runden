/**
 * Created by mkamleithner on 12/27/14.
 */
public class TransposeOperation implements Operation {

    /**
     * returns a new, transposed image
     *
     * @param img The AsciiImage to use as basis for executing the Operation, it will remain
     *            unchanged
     * @return a new, transposed image
     * @throws OperationException
     */
    public AsciiImage execute(AsciiImage img) throws OperationException {
        //create a new asciiimage with the dimensions of the old image, but width and height swapped
        AsciiImage newImage = new AsciiImage(img.getHeight(), img.getWidth(), img.getCharset());

        //get the char on position x,y on the original image and set it on the new image on position y/x
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                newImage.setPixel(y, x, img.getPixel(x, y));
            }
        }
        return newImage;
    }
}
