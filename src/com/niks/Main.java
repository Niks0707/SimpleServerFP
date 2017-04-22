package com.niks;

import java.awt.*;

/**
 * Main
 *
 * Main class start Window
 */
public class Main {

    public static final int PORT_FIRST = 56001;
    public static final int PORT_COUNT = 48;

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window frame = new Window();
                    frame.setTitle(Text.PROGRAM_TITLE);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

