package mm.structures;

import java.util.Arrays;
import java.util.Random;

/**
 * Square matrix (n x n) of m (colorCount) colors.
 */
public class ColorField extends Matrix {

//    private static final Random r = new Random("VojnaBarv".hashCode());

    private int n, colorCount;
    private int[][] neighbourMask;
    private int[] neighbourLengths;
    private ColorPixel[] colors;
    private Random r;

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

    private ColorField(int n, ColorPixel[] colors, int[][] neighbourMask, int[] neighbourLengths) {
        super(n);
        this.neighbourMask = neighbourMask;
        this.neighbourLengths = neighbourLengths;
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
     *
     * @param n
     * @param colors
     * @param r - Random generator to be used (ex.: use same seed every time)
     * @return
     */
    public static ColorField GenerateField(int n, ColorPixel[] colors, Random r) {
        ColorField cf = new ColorField(n, colors);
        for (int i = 0; i < cf.length; i++) {
            cf.setElement(i, colors[r.nextInt(cf.colorCount)].getCode());
        }
        return cf;
    }

    /**
     * Initialize colors array and color map (array of ints representing colors).
     *
     * @param colors - Colors to be used in field.
     */
    private void init(ColorPixel[] colors, boolean calculateNeighbourMask) {
        this.r = new Random();
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
            neighbourLengths = new int[super.length];
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
                    neighbourLengths[index] = ind;
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

    private int[] getNextNeighbours() {
        int[] nextNeighbours = new int[length];
        for (int i = 0; i < length; i++) {
            nextNeighbours[i] = r.nextInt(neighbourLengths[i]);
        }
        return nextNeighbours;
    }

    public ColorField updateNeighbours() {
        final Random r = new Random();
        final ColorField cf = new ColorField(n, colors, neighbourMask, neighbourLengths);

        int[] nextNeighbours = getNextNeighbours();

        for (int i = 0; i < length; i++) {
//            final int[] neighbours = getNeighbours(i);
//            cf.setElement(i, neighbours[r.nextInt(neighbours.length)]);
            cf.setElement(i, getNeighbours(i)[nextNeighbours[i]]);
        }

        return cf;
    }
}
