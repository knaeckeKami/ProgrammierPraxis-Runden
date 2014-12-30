/**
 * Created by mkamleithner on 12/30/14.
 */
public class SearchOperation implements Operation {

    MetricSet<AsciiImage> saved;
    Metric<AsciiImage> metric;

    public SearchOperation(MetricSet<AsciiImage> saved, Metric<AsciiImage> metric) {
        this.saved = saved;
        this.metric = metric;
    }

    public AsciiImage execute(AsciiImage img) throws OperationException {

        if (saved.isEmpty()) {
            throw new OperationException();
        }
        return saved.search(img, metric).iterator().next();

    }
}
