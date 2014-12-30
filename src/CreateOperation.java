/**
 * custom operation the create a new image
 * Essentially a wrapper around the constructor.
 */
public class CreateOperation implements Operation {

    private final int width, height;
    private String charset;

    public CreateOperation(int width, int height, String charset) {
        this.height = height;
        this.width = width;
        this.charset = charset;

    }

    /**
     * @param image can be null, is ignored
     * @return new AsciiImage
     */
    public AsciiImage execute(AsciiImage image) {


        return new AsciiImage(width, height, charset);
    }

}