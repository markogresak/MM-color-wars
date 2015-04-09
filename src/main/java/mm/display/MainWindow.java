package mm.display;

import mm.structures.ColorField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainWindow {

    private MainFrame window;

    public MainWindow(ColorField field) {
        this.window = new MainFrame("Vojna Barv", field);
        window.setSize(500, 522);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void updateField(final ColorField field) {
        window.setField(field);
        window.repaint();
    }

    public void saveAsJPEG(String filename) {
       window.saveAsJPEG(filename);
    }
}

class MainFrame extends JFrame implements ComponentListener {

    private FieldPanel fieldPanel;

    MainFrame(String title, ColorField field) throws HeadlessException {
        super(title);
        this.fieldPanel = new FieldPanel(field);
        fieldPanel.setSize(this.getSize());
        this.add(fieldPanel);
    }

    public void setField(ColorField field) {
        fieldPanel.setField(field);
    }

    public void saveAsJPEG(String filename) {
        fieldPanel.saveAsJPEG(filename);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        fieldPanel.setSize(this.getSize());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
