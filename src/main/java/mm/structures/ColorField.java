package mm.structures;


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
        init(colors);
    }

    /**
     * Initialize mm.structures.ColorField as n x n matrix using given colors array as colors.
     *
     * @param array  - Array representing the field matrix.
     * @param colors - Colors to be used in field.
     */
    private ColorField(int[][] array, ColorPixel[] colors) {
        super(array);
        init(colors);
        colors = colors != null ? colors : new ColorPixel[0];
    }

    /**
     * Initialize colors array and color map (array of ints representing colors).
     * @param colors - Colors to be used in field.
     */
    private void init(ColorPixel[] colors) {
        this.n = super.n;
        this.colors = colors;
        this.colorCount = colors.length;
        // Set codes for each color.
        for(int i = 0; i < colorCount; i++) {
            colors[i].setCode(i);
        }
        // Calculate neighbourMask array in advance, used to improve performance
        //  compared to calculating indices for every `getNeighbours` lookup.
        neighbourMask = new int[super.length][8];
        for (int i = 0, index = 0; i < m; i++) {
            for (int j = 0; j < n; j++, index++) {
                for (int ii = i - 1, ind = 0; ii <= i + 1; ii++) {
                    for (int jj = j - 1; jj <= j + 1; jj++) {
                        if (ii == i && jj == j) {
                            continue;
                        }
                        neighbourMask[index][ind++] =
                                ((ii < 0 || ii >= m) || (jj < 0 || jj >= n))
                                        ? -1 : indexOf(ii, jj);
                    }
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
     * @return - Vector (1x8) of neighbours. If neighbour index is out of bounds, -1 is used as value.
     */
    public int[] getNeighbours(int i, int j) {
        int[] map = neighbourMask[indexOf(i, j)];
        int[] neighbours = new int[8];
        for (int ii = 0; ii < 8; ii++) {
            neighbours[ii] = map[ii] == -1 ? -1 : a[map[ii]];
        }
        return neighbours;
    }
}
