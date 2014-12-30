import java.util.Scanner;

/**
 * Created by mkamleithner on 12/27/14.
 */
public class LoadOperation implements Operation {

    String data;

    /**
     * @param data the data of the AsciiImage which should be loaded.
     *             Must be one String with newlines to indicate a new row
     *             in the image.
     */
    public LoadOperation(String data) {
        this.data = data;
    }

    /**
     * Sets the content of the given image to the string given
     * in the constructor of this object.
     *
     * @param image
     * @return
     * @throws OperationException if the data was invalid.
     *                            The data is considered invalid if it does not match the given
     *                            width/height of the image or if not every line is equally long.
     */
    public AsciiImage execute(AsciiImage image) throws OperationException {
        try {
            AsciiImage newImage = new AsciiImage(image);
            int width = image.getWidth();
            int height = image.getHeight();
            Scanner scanner = new Scanner(data);

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            for (int i = 0; i < height; i++) {
                if (!scanner.hasNextLine()) {
                    throw new OperationException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                String line = scanner.nextLine();
                if (line.length() != width) {
                    System.out.println(line.length() + " " + width + " " + data);
                    throw new OperationException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                for (int j = 0; j < line.length(); j++) {
                    newImage.setPixel(j, i, line.charAt(j));
                }
            }
            return newImage;
        } catch (IndexOutOfBoundsException e) {
            throw new OperationException();
        }
    }
}
