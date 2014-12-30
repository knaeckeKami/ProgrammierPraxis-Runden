/**
 * Created by mkamleithner on 12/30/14.
 */
public class SaveOperation implements Operation {

    private MetricSet<AsciiImage> saved;

    public SaveOperation(MetricSet<AsciiImage> saved) {
        this.saved = saved;
    }

    public AsciiImage execute(AsciiImage img) throws OperationException {

        if (img == null || !saved.add(img)) {
            throw new OperationException();
        }
        return new AsciiImage(img);

    }

    public MetricSet<AsciiImage> getSaved() {
        return saved;
    }
}
