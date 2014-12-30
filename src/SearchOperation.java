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

    /**
     * Returns the image from the set of saved images, which is closest
     * to the given image. If more than one image has the same, smallest distance,
     * then any image of them is returned.
     * The distance is determined by the given metric.
     *
     * @param img The AsciiImage to use as basis for executing the Operation, it will remain
     *            unchanged
     * @return
     * @throws OperationException if no images have been saved previously
     */
    public AsciiImage execute(AsciiImage img) throws OperationException {

        if (saved.isEmpty()) {
            throw new OperationException();
        }
        //get an object from the set of found images and return it
        return saved.search(img, metric).iterator().next();

    }
}
