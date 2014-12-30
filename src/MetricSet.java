import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mkamleithner on 12/30/14.
 */
public class MetricSet<E> extends LinkedHashSet<E> {

    public MetricSet() {

    }

    public MetricSet(Collection<? extends E> coll) {
        super(coll);
    }

    public MetricSet<E> search(E e, Metric<? super E> m) {
        Map<Integer, MetricSet<E>> metricSetMap = new TreeMap<Integer, MetricSet<E>>();
        for (E element : this) {
            int metric = m.distance(e, element);
            if (!metricSetMap.containsKey(metric)) {
                metricSetMap.put(metric, new MetricSet<E>());

            }
            metricSetMap.get(metric).add(element);
        }
        if (metricSetMap.isEmpty()) {
            return new MetricSet<E>();
        }
        return metricSetMap.values().iterator().next();
    }

}
