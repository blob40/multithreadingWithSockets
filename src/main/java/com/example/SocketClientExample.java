package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SocketClientExample {

    /*
     * Modify this example so that it opens a dialogue window using java swing,
     * takes in a user message and sends it
     * to the server. The server should output the message back to all connected
     * clients
     * (you should see your own message pop up in your client as well when you send
     * it!).
     * We will build on this project in the future to make a full fledged server
     * based game,
     * so make sure you can read your code later! Use good programming practices.
     * ****HINT**** you may wish to have a thread be in charge of sending
     * information
     * and another thread in charge of receiving information.
     */

    public static void main(String[] args)
            throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {

        JFrame f = new JFrame("Sockets");
        JLabel clientL = new JLabel("Client Input Here:");
        JTextField clientText = new JTextField();
        clientText.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        JLabel serverL = new JLabel("Server Output");
        JTextArea serverText = new JTextArea();
        serverText.setBorder(BorderFactory.createLineBorder(Color.BLUE));
       
        f.setSize(300, 300);
        f.setLayout(new GridLayout(4, 1));
        f.add(clientL);
        f.add(clientText);
        f.add(serverL);
        f.add(serverText);
        f.setVisible(true);

        // get the localhost IP address, if server is running on some other IP, you need
        // to use that
        InetAddress host = InetAddress.getLocalHost();
           Socket socket = new Socket(host.getHostName(), 9876);
            // write to socket using ObjectOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
           clientText.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    
                    oos.writeObject(clientText.getText());
                    oos.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }
            
           });

            while(true){
                String message = (String) ois.readObject();
                serverText.setText(serverText.getText()+"\n"+ message);
                //recieve info and write to screen.
            }
            
            
        }
        //socket.shutdownOutput();
         //System.out.println("connection closed!");
        // Any time i write to an output stream you have to do a .flush()
    }

