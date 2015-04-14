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
        final int[] Ns = new int[]{25, 50};
        final int[] Ts = new int[]{10};
        final int[] samples = new int[]{10, 100, 250, 500, 1000};
        for (int s : samples) {
            for (int n : Ns) {
                for (int t : Ts) {
                    new Thread(new SamplingTask(n, s, t)).start();
                }
            }
        }
    }
}

class SamplingTask implements Runnable {

    int N, SAMPLES, HISTOGRAM_T;

    public SamplingTask(int N, int SAMPLES, int HISTOGRAM_T) {
        this.N = N;
        this.SAMPLES = SAMPLES;
        this.HISTOGRAM_T = HISTOGRAM_T;
    }

    @Override
    public void run() {

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

        long sTotal = System.nanoTime();
        ArrayList<Double>[] samples = new ArrayList[SAMPLES];
        for (int i = 0; i < SAMPLES; i++) {
            samples[i] = new ArrayList<>();
            long st = System.nanoTime();
            int iterations = 0;
            while (!cf.isAllSame()) {
                cf = cf.updateNeighbours();
                if (iterations % HISTOGRAM_T == 0) {
                    samples[i].add(cf.getColorPercentage(colors[0]));
                }
                iterations++;
            }
            System.out.printf("samples-%d-%dx%d-t%d: %.3fs, i: %d\n",
                    SAMPLES, N, N, HISTOGRAM_T, (System.nanoTime() - st) / 1e9, iterations);
            cf = initialCf;
        }
        System.out.printf("done, time: %.3fs\n", (System.nanoTime() - sTotal) / 1e9);

        long startSampling = System.nanoTime();

        ArrayList<Double> avgSamples = new ArrayList<>();
        boolean samplesExist = true;
        int sampleI = 0;
        while (samplesExist) {
            double sum = 0;
            int count = 0;
            for (int i = 0; i < SAMPLES; i++) {
                try {
                    sum += samples[i].get(sampleI);
                    count++;
                } catch (Exception e) {
                    // Number doesn't exist, don't to anything.
                }
            }
            samplesExist = count > 0;
            if (samplesExist) {
                double xt = sum / ((double) count);
                avgSamples.add(Math.log( 1 + (xt / (1 - xt)) ));
            }
            sampleI++;
        }

        String[] samplesJSON = new String[avgSamples.size()];
        for (int i = 0; i < samplesJSON.length; i++) {
            samplesJSON[i] = String.format("{\"x\": %d, \"y\": %.3f}", i * HISTOGRAM_T, avgSamples.get(i));
        }
        String json = Arrays.toString(samplesJSON);

        File file = new File(String.format("./webserver/public/data/samples-%d-%dx%d-t%d.json", SAMPLES, N, N, HISTOGRAM_T));
        file.getParentFile().mkdirs();

        try {
            Files.write(Paths.get(file.getPath()), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("done sampling samples-%d-%dx%d-t%d, time: %.3f\n",
                SAMPLES, N, N, HISTOGRAM_T, (System.nanoTime() - startSampling) / 1e9);
    }
}
