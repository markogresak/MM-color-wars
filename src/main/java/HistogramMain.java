import mm.structures.ColorField;
import mm.structures.ColorPixel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HistogramMain {

    public static void main(String[] args) throws IOException {

//        final int[] Ns = new int[]{5, 10, 25};
//        final int[] Ts = new int[]{1, 5, 10, 25, 50};
//        final int[] samples = new int[]{10, 50, 100, 250, 500};
        final int[] Ns = new int[]{10, 25};
        final int[] Ts = new int[]{1, 5, 10, 25, 50, 100, 250, 500, 1000, 2000, 3000, 4000, 5000};
        final int[] samples = new int[]{1000};
        for (int s : samples) {
            for (int n : Ns) {
                new SamplingTask(n, s, Ts).run();
            }
        }
    }
}

class SamplingTask {

    int N;
    int SAMPLES;
    int[] HISTOGRAM_T;

    public SamplingTask(int N, int SAMPLES, int[] HISTOGRAM_T) {
        this.N = N;
        this.SAMPLES = SAMPLES;
        this.HISTOGRAM_T = HISTOGRAM_T;
    }

    public void run() {

        final Random random = new Random("VojnaBarv".hashCode());

        ColorPixel[] colors = new ColorPixel[]{
                new ColorPixel(new Color(124, 7, 142), 0.25),
                new ColorPixel(new Color(236, 74, 72), 0.75 ),
//                new ColorPixel(new Color(255, 252, 88), 0.05),
//                new ColorPixel(new Color(31, 162, 209), 0.3),
//                new ColorPixel(new Color(128, 207, 12), 0.1),
//                new ColorPixel(new Color(71, 34, 69), 0.05),
//                new ColorPixel(new Color(1, 153, 138), 0.125),
//                new ColorPixel(new Color(206, 23, 54), 0.075),
//                new ColorPixel(new Color(247, 88, 48), 0.05),
//                new ColorPixel(new Color(12, 8, 65), 0.1)
        };
        ColorPixel.setCodes(colors);

        ColorField initialCf = ColorField.GenerateField(N, colors, random);
        ColorField cf = initialCf;

        long sTotal = System.nanoTime();
        double[][] samples = new double[HISTOGRAM_T.length][SAMPLES];
        for (int i = 0; i < SAMPLES; i++) {
            int tIndex = 0;
            long st = System.nanoTime();
            int iterations = 0;
            while (tIndex < HISTOGRAM_T.length && !cf.isAllSame()) {
                cf = cf.updateNeighbours();
                if (iterations == HISTOGRAM_T[tIndex]) {
                    samples[tIndex++][i] = cf.getColorPercentage(colors[0]);
                }
                iterations++;
            }
            if(i % 100 == 0) {
                System.out.printf("%d / %d\n", i + 1, SAMPLES);
            }
            cf = initialCf;
        }
        System.out.printf("done, time: %.3fs\n", (System.nanoTime() - sTotal) / 1e9);

        long startSampling = System.nanoTime();

        for (int i = 0; i < HISTOGRAM_T.length; i++) {

            int t = HISTOGRAM_T[i];
            ArrayList<HistogramPoint> usedSamples = new ArrayList<>();

            for (int j = 0; j < SAMPLES; j++) {
                double xt = samples[i][j];
                if (xt > 0.0 && xt < 1) {
                    double yt = Math.log(xt / (1 - xt));
                    yt = Math.round(yt * 100.0) / 100.0;
                    HistogramPoint p = new HistogramPoint(yt, 1);
                    int ytIndex = usedSamples.indexOf(p);
                    if (ytIndex >= 0) {
                        usedSamples.get(ytIndex).addY();
                    } else {
                        usedSamples.add(p);
                    }
                }
            }

            String[] samplesJSON = new String[usedSamples.size()];
            for (int j = 0; j < samplesJSON.length; j++) {
                samplesJSON[j] = usedSamples.get(j).toString();
            }
            String json = Arrays.toString(samplesJSON);

            File file = new File(String.format("./webserver/public/data/samples-%d-%dx%d-t%d.json", SAMPLES, N, N, t));
            file.getParentFile().mkdirs();

            try {
                Files.write(Paths.get(file.getPath()), json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("done sampling samples-%d-%dx%d-t%d, time: %.3f\n",
                    SAMPLES, N, N, t, (System.nanoTime() - startSampling) / 1e9);
        }
    }
}

class HistogramPoint {

    double x;
    int y;

    public HistogramPoint(double x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addY() {
        y++;
    }

    @Override
    public String toString() {
        return String.format("{\"x\": %.10f, \"y\": %d}", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof HistogramPoint)) {
            return false;
        }

        return ((HistogramPoint) o).x == this.x;
    }
}
