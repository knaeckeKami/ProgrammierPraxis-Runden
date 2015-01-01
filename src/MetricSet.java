import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * Custom Set, extending from LinkedHashSet.
 * Allows to search for near Objects, based on given Metrics.
 */
public class MetricSet<E> extends LinkedHashSet<E> {

    public MetricSet() {

    }

    public MetricSet(Collection<? extends E> coll) {
        super(coll);
    }

    /**
     * Searches for objects near e in this set and returns the nearest one.
     * The distance is determined by the given Metric m.
     * If more object have the same, smallest distance to e, than a random
     * object of them is chosen and returned.
     * If this Set is empty, then an empty set is returned.
     *
     * @param e Object, to search for near objects
     * @param m Metric, which determines, which object are "near" to each other
     * @return
     */
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
