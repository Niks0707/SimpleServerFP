package com.niks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerThread
 * <p>
 * Realize TCP server in other thread. Server connect with client and exchange messages.
 * When messaging end, server will be restart.
 */
public class ServerThread extends Thread {

    private int mPort;
    private int mCountMessages; // Count messages from client
    private int mTimeout = 500;
    private int mBufferSize = 254;

    /**
     * Create ServerThread with port
     *
     * @param port set server port
     */
    public ServerThread(int port) {
        mPort = port;
    }

    /**
     * Run when thread start
     */
    @Override
    public void run() {
        // While server is not interrupted start server
        while (!this.isInterrupted()) {

            mCountMessages = 0;

            // Create server socket
            ServerSocket server;
            try {
                server = new ServerSocket(mPort);
                server.setReceiveBufferSize(mBufferSize); // Set buffer size
                server.setSoTimeout(mTimeout); // Set timeout for wait accept client
            } catch (IOException e) {
                System.err.println("Couldn't listen to port " + mPort);
                return;
            }

            System.out.println("Waiting for a client... " + mPort);
            // Try to accept client while server is not interrupted
            Socket socket = null;
            while (!isInterrupted()) {
                try {
                    // If client is not accepted in mTimeout, will call exception
                    socket = server.accept();
                    break;
                } catch (IOException e) {
                    // Do nothing, because after exception will be another try to accept
                }
            }
            // socket will be null if thread interrupted
            if (socket != null) {
                System.out.println("Client connected " + mPort);
                startMessaging(socket);
            }
            try {
                server.close();
            } catch (IOException e) {
                System.err.println("Error while close:  " + mPort + " " + e.getMessage());
                return;
            }
        }
    }

    /**
     * Start messaging with client on socket
     *
     * @param socket with connection to client
     */
    private void startMessaging(Socket socket) {

        // Create In and Out stream for client
        BufferedReader socketIn;
        PrintWriter socketOut;
        try {
            socketIn = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Can't get input and output streams " + mPort);
            return;
        }

        System.out.println("Wait for messages " + mPort);

        String someMessage = "Some message at "; // Text of message
        long startTime = System.currentTimeMillis(); // Time of beginning messaging

        try {
            // Wait for start message from client
            String startMessage = socketIn.readLine();
            System.out.println(startMessage);
            mCountMessages++; // Increment count messages

            // While server is not interrupted exchange messages
            while (!isInterrupted()) {
                // Send message with time from beginning
                socketOut.println(someMessage + (System.currentTimeMillis() - startTime) + " ms");
                // Get response
                String response = socketIn.readLine();
                if (response == null) {
                    break;
                }
                System.out.println(response + " on " + mPort);
                mCountMessages++; // Increment count messages
                try {
                    // Wait for timeout between messages
                    Thread.sleep(mTimeout);
                } catch (InterruptedException e) {
                    interrupt();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error while read line on " + mPort);
            return;
        }
        try {
            socketOut.close();
            socketIn.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error while close " + mPort + ": " + e.getMessage());
            return;
        }
    }

    /**
     * Returns count messages
     *
     * @return count messages
     */
    public int getCountMessages() {
        return mCountMessages;
    }
}
