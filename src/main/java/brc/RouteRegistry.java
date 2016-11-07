package brc;

import java.util.*;


public class RouteRegistry {

    private final Map<Integer, NavigableSet<RouteEntry>> stationsRoutes = new HashMap<>();

    public void addRoute(Integer routeId, List<Integer> stationIds) {
        for (int i = 0; i < stationIds.size(); i++) {
            stationsRoutes.computeIfAbsent(stationIds.get(i), key -> new TreeSet<>(RouteEntry.ROUTE_ID_COMPARATOR)).add(new RouteEntry(routeId, i));
        }
    }

    public boolean hasDirectConnection(Integer departureStationId, Integer arrivalStationId) {
        if (Objects.equals(departureStationId, arrivalStationId))
            return true;

        NavigableSet<RouteEntry> departureStationRoutes = stationsRoutes.get(departureStationId);
        if (departureStationRoutes == null || departureStationRoutes.isEmpty())
            return false;

        NavigableSet<RouteEntry> arrivalStationRoutes = stationsRoutes.get(arrivalStationId);
        if (arrivalStationRoutes == null || arrivalStationRoutes.isEmpty())
            return false;

        Iterator<RouteEntry> depIterator = departureStationRoutes.iterator();
        Iterator<RouteEntry> arrIterator = arrivalStationRoutes.iterator();

        RouteEntry depEntry = depIterator.next();
        RouteEntry arrEntry = arrIterator.next();
        do {
            if (Objects.equals(depEntry.routeId, arrEntry.routeId)) {
                // stations are on the same route
                if (depEntry.order < arrEntry.order) {
                    // everything is fine
                    return true;
                } else {
                    // departure station is after arrival station
                    depEntry = nextOrNull(depIterator);
                    arrEntry = nextOrNull(arrIterator);
                }
            } else if (depEntry.routeId < arrEntry.routeId) {
                depEntry = nextOrNull(depIterator);
            } else {
                arrEntry = nextOrNull(arrIterator);
            }

        } while (depEntry != null && arrEntry != null);

        return false;
    }

    private static <T> T nextOrNull(Iterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Represents a station within a route.
     */
    private static class RouteEntry {
        static final Comparator<RouteEntry> ROUTE_ID_COMPARATOR = (o1, o2) -> o1.routeId.compareTo(o2.routeId);

        final Integer routeId;
        final Integer order;

        RouteEntry(Integer routeId, Integer order) {
            this.routeId = Objects.requireNonNull(routeId);
            this.order = Objects.requireNonNull(order);
        }
    }
}
