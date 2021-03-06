package mm.display;

import mm.structures.ColorField;
import mm.structures.ColorPixel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FieldPanel extends JPanel {

    private ColorField field;
    private ColorPixel[] colors;

    public FieldPanel(ColorField field) {
        this.field = field;
        this.colors = field.getColors();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle bounds = g.getClipBounds();
        int n = field.getN();
        int elementSize = bounds.height / n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ColorPixel pixel = colors[field.getElement2D(i, j)];
                g.setColor(pixel.getColor());
                g.fillRect(j * elementSize, i * elementSize, elementSize, elementSize);
            }
        }
    }

    public void setField(ColorField field) {
        this.field = field;
    }

    private void makePanelImage(Component panel) {
        Dimension size = panel.getSize();
        BufferedImage image = new BufferedImage(
                size.width, size.height
                , BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        panel.paint(g2);
        try {
            ImageIO.write(image, "png", new File("snapshot.png"));
            System.out.println("Panel saved as Image.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
