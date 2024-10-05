package com.compiler;

import com.compiler.ui.Window;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Window v = new Window();
                v.setVisible(true);
            }
        });
    }
}
