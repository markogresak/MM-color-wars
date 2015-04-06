package mm.structures;

import mm.structures.Matrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class MatrixTest {

    Matrix matrix;
    int[][] matrixArray;
    int[] matrix1DArray;

    @Before
    public void beforeMatrixTest() {
        // Using literal matrix initialization to know exactly what we test against.
        matrixArray = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        // Same matrix as above, in 1D expression.
        matrix1DArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        // Initialize new matrix using matrixArray defined above.
        // Reflection is used because constructor is protected.
        try {
            // Get declared constructor with expected argument int[][].
            Constructor<Matrix> constructor = Matrix.class.getDeclaredConstructor(int[][].class);
            // Set accessibility of the constructor to true to be able to instantiate new matrix.
            constructor.setAccessible(true);
            // Instantiate new matrix using found constructor using matrixArray
            matrix = constructor.newInstance((Object) matrixArray);
            // Set accessibility back to false.
            constructor.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetElement() throws Exception {
        for (int i = 0; i < matrixArray.length; i++) {
            for (int j = 0; j < matrixArray[0].length; j++) {
                // Test getter for each of 9 elements in matrixArray.
                int v = matrixArray[i][j];
                Assert.assertEquals(String.format("element at (%d, %d) should be %d", i, j, v),
                        v, matrix.getElement(i, j));
            }
        }

    }

    @Test
    public void testSetElement() throws Exception {
        for (int i = 0; i < matrixArray.length; i++) {
            for (int j = 0; j < matrixArray[0].length; j++) {
                // Set each of 9 elements in matrixArray to matrix.
                matrix.setElement(i, j, matrixArray[i][j]);
            }
        }
        // Test if all 9 elements were added to correct position.
        Assert.assertArrayEquals(matrix1DArray, matrix.as1DArray());
    }

    @Test
    public void testIsAllSame() throws Exception {
        // Test if isAllSame returns false for matrixArray (it should, since elements are 1-9).
        Assert.assertFalse(matrix.isAllSame());

        // Initialize matrix of all same elements (initial element is 0).
        Matrix allSame = new Matrix(5);
        // Test that the isAllSame method returns true for this case.
        Assert.assertTrue(allSame.isAllSame());
    }
}
