import mm.display.MainWindow;
import mm.graph.SendInfinite;
import mm.graph.SocketServer;
import mm.structures.ColorField;
import mm.structures.ColorPixel;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        int port = 8887;
        SocketServer ws = new SocketServer(port);
        ws.start();

        final int N = 50;
        final double FIELD_SIZE = N * N * 1.0;
        final Random random = new Random("VojnaBarv".hashCode());

        ColorPixel[] colors = new ColorPixel[]{
                new ColorPixel(new Color(124, 7, 142), 0.3),
                new ColorPixel(new Color(236, 74, 72), 0.05),
                new ColorPixel(new Color(255, 252, 88), 0.05),
                new ColorPixel(new Color(31, 162, 209), 0.1),
                new ColorPixel(new Color(128, 207, 12), 0.1),
                new ColorPixel(new Color(71, 34, 69), 0.05),
                new ColorPixel(new Color(1, 153, 138), 0.125),
                new ColorPixel(new Color(206, 23, 54), 0.075),
                new ColorPixel(new Color(247, 88, 48), 0.05),
                new ColorPixel(new Color(12, 8, 65), 0.1)
        };
        ColorPixel.setCodes(colors);

        ColorField initialCf = ColorField.GenerateField(N, colors, random);
        ColorField cf = initialCf;
        MainWindow window = new MainWindow(cf);

        String colorsJSON = String.format("{\"message\": \"init\", \"colors\": %s, \"p\": %s}",
                ColorPixel.colorsAsJSONArray(cf.getColors()),
                ColorPixel.getProbabilitesJSON(cf.getColors()));
        System.out.println("send: " + colorsJSON);
        new Thread(new SendInfinite(ws, colorsJSON)).start();

        long ss = System.nanoTime();

        int[] zmage = new int[100];
        for (int i = 0; i < 10; i++) {

            String colorsJSON = String.format("{\"message\": \"colors\", \"value\": %s}", ColorPixel.colorsAsJSONArray(cf.getColors()));
            s.sendToAll(colorsJSON);

            long start = System.nanoTime();

            long allStart = System.nanoTime();
            int iterations = 0;
            while (!cf.isAllSame()) {
                cf = cf.updateNeighbours();
                window.updateField(cf);
                if (iterations % 50 == 0) {
                    ws.sendToAll(ColorPixel.colorsCountAsJSONArray(cf.getColors(), iterations, FIELD_SIZE));
                }
                iterations++;
            }
            zmage[i] = cf.getColorWon().getCode();
            System.out.println("Zmagala: " + zmage[i]);
            long end = System.nanoTime() - allStart;
            System.out.printf("Celoten cas: %.3fs\n", end / 1e9);
//            Thread.sleep(2000);
            cf = initialCf;
        }

        System.out.printf("end: %.3fs\n", (System.nanoTime() - ss) / 1e9);
        System.out.println(Arrays.toString(zmage));
        int[] st = new int[colors.length];
        for (int z : zmage) {
            st[z]++;
        }
        System.out.println("razmerje: " + Arrays.toString(st));
    }
}
