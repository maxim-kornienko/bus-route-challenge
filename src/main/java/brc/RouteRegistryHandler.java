package brc;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class RouteRegistryHandler implements HttpHandler {

    private final RouteRegistry registry;

    public RouteRegistryHandler(RouteRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            handleInternal(httpExchange);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
        }
    }

    private void handleInternal(HttpExchange httpExchange) throws IllegalArgumentException, IOException {
        Map<String, String> params = getParameters(httpExchange.getRequestURI().getQuery());
        Integer depSid = getIntegerParameter(params, "dep_sid");
        Integer arrSid = getIntegerParameter(params, "arr_sid");

        String response = buildResponse(depSid, arrSid, registry.hasDirectConnection(depSid, arrSid));

        httpExchange.sendResponseHeaders(HTTP_OK, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private static Map<String, String> getParameters(String query) {
        Map<String, String> result = new HashMap<>();
        for (String paramValue : query.split("&")) {
            String[] pv = paramValue.split("=");
            if (pv.length != 2)
                throw new IllegalArgumentException();
            result.put(pv[0], pv[1]);
        }
        return result;
    }

    private static Integer getIntegerParameter(Map<String, String> params, String key) {
        String value = params.get(key);
        if (value == null)
            throw new IllegalArgumentException();
        return Integer.valueOf(value);
    }

    private static String buildResponse(Integer depSid, Integer arrSid, boolean directBusRoute) {
        return String.format(
                "{\n" +
                    "    \"dep_sid\": %d,\n" +
                    "    \"arr_sid\": %d,\n" +
                    "    \"direct_bus_route\": %b" +
                "\n}"
        , depSid, arrSid, directBusRoute);
    }
}
