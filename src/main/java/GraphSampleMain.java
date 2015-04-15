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

public class GraphSampleMain {

    public static void main(String[] args) {

        final int RANDOM_SEED = "VojnaBarv".hashCode();

        final ColorPixel[] colors = new ColorPixel[]{
                new ColorPixel(new Color(124, 7, 142), 0.3),
                new ColorPixel(new Color(31, 162, 209), 0.7),
//                new ColorPixel(new Color(128, 207, 12), 0.1),
//                new ColorPixel(new Color(71, 34, 69), 0.05),
//                new ColorPixel(new Color(1, 153, 138), 0.125),
//                new ColorPixel(new Color(206, 23, 54), 0.075),
//                new ColorPixel(new Color(247, 88, 48), 0.05),
//                new ColorPixel(new Color(12, 8, 65), 0.1)
        };
        ColorPixel.setCodes(colors);

        final int[] Ns = new int[]{5, 10, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100};
        final int[] samples = new int[]{10, 25, 50, 100};
        final int[] ITERATION_SAMPLE = new int[]{1, 5, 10, 25, 50, 100};

        for (int n : Ns) {
            for (int i : ITERATION_SAMPLE) {
                (new GraphSampleTask(samples, 0, n, i, RANDOM_SEED, colors)).run();
            }
        }
    }
}

class GraphSampleTask {

    int ITERATIONS_SAMPLE;
    int[] SAMPLES;
    int COLOR_INDEX;
    int N;
    double FIELD_SIZE;
    int maxSample;
    ColorField initialCf;

    public GraphSampleTask(int[] SAMPLES, int COLOR_INDEX, int N, int ITERATIONS_SAMPLE, int randomSeed, ColorPixel[] colors) {
        this.SAMPLES = SAMPLES;
        Arrays.sort(this.SAMPLES);
        this.maxSample = SAMPLES[SAMPLES.length - 1];
        this.COLOR_INDEX = COLOR_INDEX;
        this.N = N;
        this.ITERATIONS_SAMPLE = ITERATIONS_SAMPLE;
        this.FIELD_SIZE = N * N * 1.0;
        final Random random = new Random(randomSeed);
        initialCf = ColorField.GenerateField(N, colors, random);
    }

    public void run() {
        ColorField cf = initialCf;
        ArrayList<ArrayList<String>> samplesAl = new ArrayList<>();

        int sampleI = 0;
        for (int i = 0; i < maxSample; i++) {

            ArrayList<String> arr = new ArrayList<>();

            long start = System.nanoTime();
            int iterations = 0;
            while (!cf.isAllSame()) {
                cf = cf.updateNeighbours();
                if (iterations % ITERATIONS_SAMPLE == 0) {
                    arr.add(cf.getColors()[COLOR_INDEX].toJSONAccuraccy(iterations, FIELD_SIZE));
                }
                iterations++;
            }
            arr.add(cf.getColors()[COLOR_INDEX].toJSONAccuraccy(iterations, FIELD_SIZE));
            System.out.printf("Size: %d x %d | Sample: %4d | i%d | time: %.3f | i: %d\n", N, N, i, ITERATIONS_SAMPLE, (System.nanoTime() - start) / 1e9, iterations);

            samplesAl.add(arr);
            if ((i + 1) == SAMPLES[sampleI]) {
                saveSample(samplesAl, sampleI);
                sampleI++;
            }
            cf = initialCf;
        }
    }

    private void saveSample(ArrayList<ArrayList<String>> samplesAl, int sampleI) {
        StringBuilder all = new StringBuilder();
        for (ArrayList<String> al : samplesAl) {
            all.append(",");
            all.append(al.toString());
        }
        String json = String.format("[%s]", all.substring(1));

        File file = new File(String.format("./webserver/public/data/graph-%d-%dx%d-i%d.json", SAMPLES[sampleI], N, N, ITERATIONS_SAMPLE));
        file.getParentFile().mkdirs();

        try {
            Files.write(Paths.get(file.getPath()), json.getBytes());
            System.out.printf("Saved sample to: %s\n", file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
