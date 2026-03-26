package com.example;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program should allow the client to send it messages. The messages should then 
 * become visible to all other clients.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example). 
 * 
 * This version of the program creates a new thread for
 * every connection request.
 */
public class ChatServerWithThreads {

    public static final int LISTENING_PORT = 9876;

    public static void main(String[] args) {

        ServerSocket listener;  // Listens for incoming connections.
        Socket connection;      // For communication with the connecting program.

        /* Accept and process connections forever, or until some error occurs. */

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
            ConnectionHandler h = new ConnectionHandler(connection);
            h.start();
                  // Accept next connection request and handle it.
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }

    }  // end main()


    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */
    private static class ConnectionHandler extends Thread {
        private static ArrayList<ConnectionHandler> handlers;
        Socket client;
          ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
      


        ConnectionHandler(Socket socket) {
            client = socket;
            if (handlers == null){
                handlers = new ArrayList();
            }  
            handlers.add(this);
        }

        public void run() {
            
            try {
                ois = new ObjectInputStream(client.getInputStream());
                oos = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            String clientAddress = client.getInetAddress().toString();
            
            while(true) {
	            try {
                      String message = (String) ois.readObject();
                      if (message.equals("disconnect")){
                        System.out.println(message + " Closing Connection");
                        break;
                      } else {
                        //loop through all the handlers and tell their output streams the message
                        for (int i = 0; i < handlers.size(); i++){
                            handlers.get(i).oos.writeObject(message);
                        }
                        System.out.println(message);
                      }
	            	//your code to send messages goes here.
	            }
                 catch(EOFException e){
                    System.out.println("the client disconnected, bye!!!");
                    handlers.remove(this);
                    break;
                }
	            catch (Exception e){
	                System.out.println("Error on connection with: " 
	                        + clientAddress + ": " + e);
	            }
            }
        }
    }


}
