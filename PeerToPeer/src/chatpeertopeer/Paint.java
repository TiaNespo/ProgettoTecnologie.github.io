/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatpeertopeer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Mattia
 */
public class Paint extends Thread {
    
    JFrame f;
    int FPS;

    public Paint(JFrame f, int i) {
        this.f = f;
        FPS = 1000 / i;
    }

    @Override
    public void run() {
        while (true) {
            f.repaint();
            try {
                Thread.sleep(FPS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
