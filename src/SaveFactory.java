import java.util.Scanner;

/**
 * Created by mkamleithner on 12/30/14.
 */
public class SaveFactory implements Factory {

    private MetricSet<AsciiImage> saved;

    public SaveFactory(MetricSet<AsciiImage> saved) {
        this.saved = saved;
    }

    public Operation create(Scanner scanner) throws FactoryException {
        return new SaveOperation(saved);
    }

    public String getKeyWord() {
        return "save";
    }
}
