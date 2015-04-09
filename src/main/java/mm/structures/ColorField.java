package mm.structures;

import java.util.Arrays;
import java.util.Random;

/**
 * Square matrix (n x n) of m (colorCount) colors.
 */
public class ColorField extends Matrix {

    private int n, colorCount;
    private int[][] neighbourMask;
    private ColorPixel[] colors;

    /**
     * Initialize mm.structures.ColorField as n x n matrix using given colors array as colors.
     *
     * @param n      - Dimension of matrix.
     * @param colors - Colors to be used in field.
     */
    public ColorField(int n, ColorPixel[] colors) {
        super(n);
        init(colors, true);
    }

    private ColorField(int n, ColorPixel[] colors, int[][] neighbourMask) {
        super(n);
        this.neighbourMask = neighbourMask;
        init(colors, false);
    }

    /**
     * Initialize mm.structures.ColorField as n x n matrix using given colors array as colors.
     *
     * @param array  - Array representing the field matrix.
     * @param colors - Colors to be used in field.
     */
    private ColorField(int[][] array, ColorPixel[] colors) {
        super(array);
        colors = colors != null ? colors : new ColorPixel[0];
        init(colors, true);
    }

    /**
     * Initialize colors array and color map (array of ints representing colors).
     *
     * @param colors - Colors to be used in field.
     */
    private void init(ColorPixel[] colors, boolean calculateNeighbourMask) {
        this.n = super.n;
        this.colors = colors;
        this.colorCount = colors.length;
        // Set codes for each color.
        for (int i = 0; i < colorCount; i++) {
            colors[i].setCode(i);
        }
        if (calculateNeighbourMask) {
            // Calculate neighbourMask array in advance, used to improve performance
            //  compared to calculating indices for every `getNeighbours` lookup.
            neighbourMask = new int[super.length][8];
            for (int i = 0, index = 0; i < m; i++) {
                for (int j = 0; j < n; j++, index++) {
                    int ind = 0;
                    for (int ii = i - 1; ii <= i + 1; ii++) {
                        for (int jj = j - 1; jj <= j + 1; jj++) {
                            if ((ii != i || jj != j) && (ii >= 0 && ii < m) && (jj >= 0 && jj < n)) {
                                neighbourMask[index][ind++] = indexOf(ii, jj);
                            }
                        }
                    }
                    neighbourMask[index] = Arrays.copyOf(neighbourMask[index], ind);
                }
            }
        }
    }

    public int getN() {
        return n;
    }

    public int getColorCount() {
        return colorCount;
    }

    public ColorPixel[] getColors() {
        return colors;
    }

    /**
     * Find values of neighbours of (i, j).
     *
     * @param i - mm.structures.Matrix row.
     * @param j - mm.structures.Matrix column.
     * @return - Vector (1xn) (n = [1, 8]) of existent neighbours.
     */
    public int[] getNeighbours(int i, int j) {
        return getNeighbours(indexOf(i, j));
    }

    public int[] getNeighbours(int i) {
        int[] map = neighbourMask[i];
        int[] neighbours = new int[map.length];
        for (int ii = 0; ii < map.length; ii++) {
            neighbours[ii] = a[map[ii]];
        }
        return neighbours;
    }

    public ColorField updateNeighbours() {
        final ColorField cf = new ColorField(n, colors, neighbourMask);
        final Random r = new Random();

        for (int i = 0; i < length; i++) {
            final int[] neighbours = getNeighbours(i);
            cf.setElement(i, neighbours[r.nextInt(neighbours.length)]);
        }

        return cf;
    }

    public static ColorField GenerateField(int n, ColorPixel[] colors) {
        ColorField cf = new ColorField(n, colors);
        Random r = new Random();
        for (int i = 0; i < cf.length; i++) {
            cf.setElement(i, colors[r.nextInt(cf.colorCount)].getCode());
        }
        return cf;
    }
}
