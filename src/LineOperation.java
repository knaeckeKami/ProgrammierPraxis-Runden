/**
 * Created by mkamleithner on 12/27/14.
 */
public class LineOperation implements Operation {

    private int x0, x1, y0, y1;
    private char c;

    public LineOperation(int x0, int x1, int y0, int y1, char c) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.c = c;
    }

    public AsciiImage execute(AsciiImage img) throws OperationException {
        AsciiImage newImage = new AsciiImage(img);
        boolean swapped = false;
        int help;
        int deltaX = x1 - x0;
        int deltaY = y1 - y0;
        //check if the axis are inverted
        if (Math.abs(deltaY) > Math.abs(deltaX)) {
            swapped = true;
            //swap all x with y
            help = deltaX;
            deltaX = deltaY;
            deltaY = help;
            help = x0;
            x0 = y0;
            y0 = help;
            help = x1;
            x1 = y1;
            y1 = help;

        }
        if (x1 < x0) {
            help = x0;
            x0 = x1;
            x1 = help;
            help = y0;
            y0 = y1;
            y1 = help;

        }
        double y = y0;
        for (int x = x0; x <= x1; x++, y += (((double) deltaY) / deltaX)) {
            if (swapped) {
                newImage.setPixel((int) Math.round(y), x, c);
            } else
                newImage.setPixel(x, (int) Math.round(y), c);

        }
        return newImage;
    }
}
