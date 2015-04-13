package mm.structures;

import java.util.Arrays;

public class ColorPixel {

    private int code;
    private int count;
    private double probability;
    private java.awt.Color color;

    public ColorPixel() {
        this.color = new java.awt.Color(0, 0, 0);
    }

    public ColorPixel(java.awt.Color color) {
        this(color, 0.0);
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
    private static String substrUpTo(String s) {
        while(s.length() < 3) {
            s += "0";
        }
        return s.substring(0, 3);
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

    public int getCode() {
        return code;
    }

    protected void setCode(int code) {
        this.code = code;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }
}
