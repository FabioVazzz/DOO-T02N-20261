public class WeatherParser {

    public WeatherData parse(String json) {

        WeatherData w = new WeatherData();

        w.city = get(json, "resolvedAddress");

        String current = block(json, "\"currentConditions\"");
        String day = array(json, "days");

        w.temp = toDouble(current, "temp");
        w.humidity = toDouble(current, "humidity");
        w.condition = get(current, "conditions");

        w.tempInfo = new TempInfo(
                toDouble(current, "temp"),
                toDouble(day, "tempmax"),
                toDouble(day, "tempmin")
        );

        w.wind = new WindInfo(
                toDouble(current, "windspeed"),
                toDouble(current, "winddir")
        );

        w.precipitation = toDouble(day, "precip");

        return w;
    }

    private String get(String text, String key) {
        int i = text.indexOf("\"" + key + "\"");
        if (i == -1) return "—";

        int start = text.indexOf(":", i) + 1;
        int end = text.indexOf(",", start);
        if (end == -1) end = text.indexOf("}", start);

        return text.substring(start, end).replace("\"", "").trim();
    }

    private String block(String json, String key) {
        int i = json.indexOf(key);
        int start = json.indexOf("{", i);

        int count = 1;
        int end = start + 1;

        while (count > 0 && end < json.length()) {
            if (json.charAt(end) == '{') count++;
            if (json.charAt(end) == '}') count--;
            end++;
        }

        return json.substring(start, end);
    }

    private String array(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        int start = json.indexOf("[", i);
        int end = json.indexOf("]", start);

        return json.substring(start, end);
    }

    private double toDouble(String text, String key) {
        try {
            int i = text.indexOf("\"" + key + "\"");
            if (i == -1) return 0;

            int start = text.indexOf(":", i) + 1;
            int end = text.indexOf(",", start);

            if (end == -1) end = text.indexOf("}", start);

            return Double.parseDouble(text.substring(start, end).trim());

        } catch (Exception e) {
            return 0;
        }
    }
}