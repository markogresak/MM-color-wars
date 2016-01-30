package mm.display;

import mm.structures.ColorField;
import mm.structures.ColorPixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow {

    private MainFrame window;

    public MainWindow(ColorField field) {
        this.window = new MainFrame("Vojna Barv", field);
        window.setSize(500, 522);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    /**
     * Update JPanel with current field data.
     *
     * @param field
     * @return True if should reset, false otherwise.
     */
    public boolean updateField(ColorField field) {
        boolean end = window.shouldReset();
        if (end) {
            field = new ColorField(field.getN(), new ColorPixel[0]);
            window.unsetShouldReset();
        }
        window.setField(field);
        window.repaint();
        return end;
    }
}

class MainFrame extends JFrame implements ComponentListener, KeyListener {

    protected FieldPanel fieldPanel;
    private boolean _shouldReset;

    MainFrame(String title, ColorField field) throws HeadlessException {
        super(title);
        this.fieldPanel = new FieldPanel(field);
        fieldPanel.setSize(this.getSize());
        this.add(fieldPanel);
        this.addKeyListener(this);
    }

    public boolean shouldReset() {
        return _shouldReset;
    }

    public void setShouldReset() {
        this._shouldReset = true;
    }

    public void unsetShouldReset() {
        this._shouldReset = false;
    }

    public void setField(ColorField field) {
        fieldPanel.setField(field);
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

    @Override
    public void keyTyped(KeyEvent e) {
        if (Character.toString(e.getKeyChar()).toUpperCase().equals("R")) {
            setShouldReset();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
