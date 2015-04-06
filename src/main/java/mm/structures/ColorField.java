package mm.structures;

import java.awt.*;

/**
 * Square matrix (n x n) of m (colorCount) colors.
 */
public class ColorField extends Matrix {

    private int n, colorCount;
    private int[][] neighbourMask;
    private int[] colorMap;
    private Color[] colors;

    /**
     * Initialize mm.structures.ColorField as n x n matrix using given colors array as colors.
     *
     * @param n      - Dimension of matrix.
     * @param colors - Colors to be used in field.
     */
    public ColorField(int n, Color[] colors) {
        super(n);
        init(colors);
    }

    /**
     * Initialize mm.structures.ColorField as n x n matrix using given colors array as colors.
     *
     * @param array  - Array representing the field matrix.
     * @param colors - Colors to be used in field.
     */
    private ColorField(int[][] array, Color[] colors) {
        super(array);
        colors = colors != null ? colors : new Color[0];
        init(colors);
    }

    /**
     * Initialize colors array and color map (array of ints representing colors).
     * @param colors - Colors to be used in field.
     */
    private void init(Color[] colors) {
        this.n = super.n;
        this.colorCount = colors.length;
        this.colors = colors;
        // Init color map as values [0, colors.length - 1].
        this.colorMap = new int[colorCount];
        for (int i = 0; i < colorCount; i++) {
            this.colorMap[i] = i;
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
