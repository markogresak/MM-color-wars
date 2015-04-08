package mm.structures;

public class ColorPixel {

    private int code;
    private double probability;
    private java.awt.Color color;

    public ColorPixel() {
        this.color = new java.awt.Color(0, 0, 0);
    }

    public ColorPixel(java.awt.Color color) {
        this.color = color;
    }

    public int getCode() {
        return code;
    }

    protected void setCode(int code) {
        this.code = code;
    }

    public double getProbability() {
        return probability;
    }

    protected void setProbability(double probability) {
        this.probability = probability;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }
}
