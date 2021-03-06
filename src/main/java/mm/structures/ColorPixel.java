package mm.structures;

import java.util.Arrays;

public class ColorPixel {

    private static final String MARKER = ", \"markerColor\": \"#7C078E\", \"markerType\": \"circle\", \"markerSize\": 10";

    private int code;
    private int count;
    private double probability;
    private java.awt.Color color;


    public ColorPixel() {
        this(new java.awt.Color(0, 0, 0));
    }

    public ColorPixel(java.awt.Color color) {
        this(color, 0.5);
    }

    public ColorPixel(java.awt.Color color, double probability) {
        this.color = color;
        this.probability = probability;
    }

    public static void setCodes(ColorPixel[] colors) {
        int code = 0;
        for (ColorPixel c : colors) {
            c.setCode(code++);
        }
    }

    public static void resetAllColors(ColorPixel[] colors) {
        for (ColorPixel c : colors) {
            c.setCount(0);
        }
    }

    public static String colorsAsJSONArray(ColorPixel[] colors) {
        return Arrays.toString(Arrays.stream(colors).map(ColorPixel::colorHex).toArray());
    }

    public static String[] colorsCountAsJSONs(ColorPixel[] colors, int iterations, double fieldSize) {
        return Arrays.stream(colors).map(c -> c.toJSON(iterations, fieldSize)).toArray(String[]::new);
    }

    public static String colorsCountAsJSONArray(ColorPixel[] colors, int iterations, double fieldSize) {
        return Arrays.toString(colorsCountAsJSONs(colors, iterations, fieldSize));
    }

    private static String substrUpTo(String s) {
        while (s.length() < 3) {
            s += "0";
        }
        return s.substring(0, 3);
    }

    public static double[] getColorPercentages(ColorPixel[] colors, double fieldSize) {
        return Arrays.stream(colors).map(c -> c.count / fieldSize).mapToDouble(Double::doubleValue).toArray();
    }

    public static int[] getProbabilityArray(ColorPixel[] colors) {
        int[] decimalPlaces = Arrays.stream(colors)
                .map(c -> Integer.parseInt(substrUpTo(Double.toString(c.probability).split("\\.")[1])))
                .mapToInt(Integer::intValue).toArray();

        int[] a = new int[Arrays.stream(decimalPlaces).sum()];
        int[] codes = Arrays.stream(colors).map(ColorPixel::getCode).mapToInt(Integer::intValue).toArray();

        int index = 0;
        for (int i = 0; i < decimalPlaces.length; i++) {
            Arrays.fill(a, index, index + decimalPlaces[i], codes[i]);
            index += decimalPlaces[i];
        }

        return a;
    }

    public static String getProbabilitesJSON(ColorPixel[] colors) {
        return Arrays.toString(Arrays.stream(colors).map(ColorPixel::getProbability).toArray());
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getProbability() {
        return probability;
    }

    public void addCount() {
        this.count++;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public String toJSON(int iterations, double allFieldCount) {
        return String.format("{\"x\":%d,\"y\":%.3f}", iterations, count / allFieldCount);
    }

    public String toJSONAccuraccy(int iterations, double allFieldCount) {
        double y = count / allFieldCount;
        return String.format("{\"x\":%d,\"y\":%f%s}", iterations, y, y > 0 && y < 1 ? "" : MARKER);
    }

    public String colorHex() {
        return String.format("\"#%06X\"", color.getRGB() & 0xFFFFFF);
    }
}
