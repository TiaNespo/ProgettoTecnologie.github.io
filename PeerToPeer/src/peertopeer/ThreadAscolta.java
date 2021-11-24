/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author Mattia
 */
public class ThreadAscolta extends Thread {

    public DatagramSocket Socket;
    boolean attesa = true;
    String contentuto="";

    public ThreadAscolta() throws SocketException {
        Socket = new DatagramSocket(12345);
    }

    @Override
    public void run() {
        while (attesa == true) {
            byte[] buffer = new byte[1500];
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
            try {
                Socket.receive(Packet);
                String messaggio = new String(Packet.getData());
                if (messaggio.substring(0, 1).equals("A")) {
                    attesa = true;
                    Ascolta();
                }

            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ThreadAscolta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    public void Ascolta(){
        
    }


}
