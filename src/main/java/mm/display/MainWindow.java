package mm.display;

import mm.structures.ColorField;

import javax.swing.*;

public class MainWindow {

    public MainWindow(ColorField field){
        FieldPanel fieldPanel = new FieldPanel(field);
        JFrame window = new JFrame();
        window.setSize(500, 522);
        fieldPanel.setSize(500, 522);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(fieldPanel);
        window.setVisible(true);
    }
}
