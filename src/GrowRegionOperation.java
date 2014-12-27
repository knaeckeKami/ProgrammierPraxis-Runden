/**
 * Created by mkamleithner on 12/27/14.
 */
public class GrowRegionOperation implements Operation {

    private char charToGrow;

    public GrowRegionOperation(char charToGrow) {
        this.charToGrow = charToGrow;
    }

    /**
     * grows the region of all points with the character c. All characters, which are neighbours of any point containing
     * c, which are set to the background character, will be set to c.
     */
    public AsciiImage execute(AsciiImage img) throws OperationException {
        AsciiImage newImage = new AsciiImage(img);
        for (AsciiPoint point : newImage.getPointList(charToGrow)) {
            for (AsciiPoint neighbour : newImage.get4Neighbors(point)) {
                if (newImage.getPixel(neighbour) == newImage.getBackgroundCharacter()) {
                    newImage.setPixel(neighbour, charToGrow);
                }
            }
        }
        return newImage;
    }
}
