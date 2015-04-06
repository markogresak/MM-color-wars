package mm.structures;

/**
 * Implementation of matrix using 1D array.
 */
public class Matrix {

    protected int[] a;
    protected int m, n, length;

    /**
     * Initialize matrix of size m x n.
     *
     * @param m - Height of matrix.
     * @param n - Width of matrix.
     */
    public Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        this.length = m * n;
        a = new int[length];
    }

    /**
     * Initialize square matrix of size n x n.
     *
     * @param n - Dimension of matrix.
     */
    public Matrix(int n) {
        this(n, n);
    }

    /**
     * Initialize matrix using 2D array.
     *
     * @param array - Array representing the matrix.
     */
    protected Matrix(int[][] array) {
        this(array.length, array[0].length);
        for (int i = 0, ii = 0; i < m; i++) {
            for (int j = 0; j < n; j++, ii++) {
                a[ii] = array[i][j];
            }
        }
    }

    /**
     * Calculate index of matrix at location (i, j).
     *
     * @param i - mm.structures.Matrix row.
     * @param j - mm.structures.Matrix column.
     * @return - Index of matrix at location (i, j).
     */
    protected int indexOf(int i, int j) {
        return j + this.n * i;
    }

    /**
     * @param i - mm.structures.Matrix row.
     * @param j - mm.structures.Matrix column.
     * @return - The element on location (i, j).
     */
    public int getElement(int i, int j) {
        return a[indexOf(i, j)];
    }

    /**
     * Set the element at location (i, j).
     *
     * @param i - mm.structures.Matrix row.
     * @param j - mm.structures.Matrix column.
     * @param e - New element.
     */
    public void setElement(int i, int j, int e) {
        a[indexOf(i, j)] = e;
    }

    /**
     * @return - mm.structures.Matrix as 1D array.
     */
    public int[] as1DArray() {
        return a;
    }

    /**
     * @return - mm.structures.Matrix as 2D array.
     */
    public int[][] as2DArray() {
        int[][] arr = new int[m][n];
        for (int i = 0, ii = 0; i < m; i++) {
            for (int j = 0; j < n; j++, ii++) {
                arr[i][j] = a[ii];
            }
        }
        return arr;
    }

    /**
     * Check if all elements in matrix are the same.
     *
     * @return - True if all elements match, false otherwise.
     */
    public boolean isAllSame() {
        int el = a[0];
        for (int i = 1; i < length; i++) {
            if (a[i] != el) {
                return false;
            }
        }
        return true;
    }
}
