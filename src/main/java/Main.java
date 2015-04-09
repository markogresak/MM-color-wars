import mm.display.MainWindow;
import mm.structures.ColorField;
import mm.structures.ColorPixel;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        ColorPixel[] colors = new ColorPixel[]{
                new ColorPixel(new Color(124, 7, 142)),
                new ColorPixel(new Color(236, 74, 72)),
                new ColorPixel(new Color(255, 252, 88)),
                new ColorPixel(new Color(31, 162, 209)),
//                new ColorPixel(new Color(128, 207, 12)),
//                new ColorPixel(new Color(71, 34, 69)),
//                new ColorPixel(new Color(1, 153, 138)),
//                new ColorPixel(new Color(206, 23, 54)),
//                new ColorPixel(new Color(247, 88, 48)),
//                new ColorPixel(new Color(12, 8, 65))
        };

        long start = System.nanoTime();
        ColorField cf = ColorField.GenerateField(100, colors);
        System.out.printf("generiranje polja: %d\n", System.nanoTime() - start);

        MainWindow window = new MainWindow(cf);

        long allStart = System.nanoTime();
        long iterations = 0;
        while(!cf.isAllSame()) {
            cf = cf.updateNeighbours();
            window.setField(cf);
            window.repaint();
            iterations++;
        }
        long end = System.nanoTime() - allStart;
        System.out.printf("St iteracij: %d\n", iterations);
        System.out.printf("Celoten cas: %d\n", end);
    }
}
