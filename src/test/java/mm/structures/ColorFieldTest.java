package mm.structures;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class ColorFieldTest {

    ColorField colorField;
    int[][] matrixArray;

    @Before
    public void beforeMatrixTest() {
        // Using literal matrix initialization to know exactly what we test against.
        matrixArray = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        // Call `colorField = new ColorField(matrixArray, null);`, used because constructor is private.
        // Get all declared constructors for ColorField class.
        try {
            // Get declared constructor with expected argument int[][] and Color[].
            Constructor<ColorField> constructor = ColorField.class.
                    getDeclaredConstructor(int[][].class, java.awt.Color[].class);
            // Set accessibility of the constructor to true to be able to instantiate new matrix.
            constructor.setAccessible(true);
            // Instantiate new colorField using found constructor using matrixArray and null as Color array.
            colorField = constructor.newInstance(matrixArray, null);
            // Set accessibility back to false.
            constructor.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNeighbours() throws Exception {
        // Declare expected neighbour values as literal array initialization.
        int[][] expectedNeighbours = new int[][]{
                {-1, -1, -1, -1, 2, -1, 4, 5},
                {-1, -1, -1, 1, 3, 4, 5, 6},
                {-1, -1, -1, 2, -1, 5, 6, -1},
                {-1, 1, 2, -1, 5, -1, 7, 8},
                {1, 2, 3, 4, 6, 7, 8, 9},
                {2, 3, -1, 5, -1, 8, 9, -1},
                {-1, 4, 5, -1, 8, -1, -1, -1},
                {4, 5, 6, 7, 9, -1, -1, -1},
                {5, 6, -1, 8, -1, -1, -1, -1}
        };

        for (int i = 0, _i = 0; i < matrixArray.length; i++) {
            for (int j = 0; j < matrixArray[0].length; j++, _i++) {
                // Get neighbours for each index in matrixArray.
                int[] neighbours = colorField.getNeighbours(i, j);
                // Test that returned neighbours are equal to what we're expecting.
                Assert.assertArrayEquals(expectedNeighbours[_i], neighbours);
            }
        }
    }
}
