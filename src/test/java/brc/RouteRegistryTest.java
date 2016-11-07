package brc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RouteRegistryTest {

    private final RouteRegistry registry = new RouteRegistry();

    @Before
    public void before() {
        Starter.loadRegistry(registry, new File("src/test/resources/bus_routes_data_file.txt"));
    }

    @Test
    public void test() {
        assertTrue(registry.hasDirectConnection(1, 3));
        assertTrue(registry.hasDirectConnection(3, 1));
        assertTrue(registry.hasDirectConnection(3, 6));
        assertTrue(registry.hasDirectConnection(3, 5));
        assertFalse(registry.hasDirectConnection(5, 3));
        assertFalse(registry.hasDirectConnection(6, 3));

        assertTrue(registry.hasDirectConnection(0, 6));
        assertTrue(registry.hasDirectConnection(0, 4));
    }
}
