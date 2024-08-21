package utils;

import java.awt.*;

import javax.swing.*;

public class UIUtils {
    public static void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
    }
}