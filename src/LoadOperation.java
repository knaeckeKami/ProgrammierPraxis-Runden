import java.util.Scanner;

/**
 * Created by mkamleithner on 12/27/14.
 */
public class LoadOperation implements Operation {

    String data;

    public LoadOperation(String data) {
        this.data = data;
    }

    public AsciiImage execute(AsciiImage image) throws OperationException {

        AsciiImage newImage = new AsciiImage(image);
        int width = image.getWidth();
        int height = image.getHeight();
        Scanner scanner = new Scanner(data);

        for (int i = 0; i < height; i++) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException(AsciiShop.ERRORS.INPUT_ERROR.toString());
            }
            String line = scanner.nextLine();
            if (line.length() != width) {
                throw new IllegalArgumentException(AsciiShop.ERRORS.INPUT_ERROR.toString());
            }
            for (int j = 0; j < line.length(); j++) {
                newImage.setPixel(j, i, line.charAt(j));
            }
        }
        return newImage;
    }
}
