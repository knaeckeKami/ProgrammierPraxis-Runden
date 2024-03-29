import java.util.Scanner;

/**
 * Created by mkamleithner on 12/30/14.
 */
public class SearchFactory implements Factory {

    private MetricSet<AsciiImage> saved;

    public SearchFactory(MetricSet<AsciiImage> saved) {
        this.saved = saved;
    }

    /**
     * Returns a SearchOperation. Which metric is used, is determined by the String read
     * from the scanner.
     * 2 Metrics are supported: "pixelcount" and "uniquechars".
     *
     * @param scanner, reads "pixelcount" or "uniquechars"
     * @return SearchOperation
     * @throws FactoryException if an invalid metric is read by the scanner
     */
    public SearchOperation create(Scanner scanner) throws FactoryException {
        if (!scanner.hasNext()) {
            throw new FactoryException();
        }
        String command = scanner.next();
        Metric<AsciiImage> metric;
        if (command.equals("pixelcount")) {
            metric = new PixelCountMetric();
        } else if (command.equals("uniquechars")) {
            metric = new UniqueCharsMetric();
        } else {
            throw new FactoryException();
        }
        return new SearchOperation(saved, metric);
    }

    public String getKeyWord() {
        return "search";
    }
}
