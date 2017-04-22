package com.niks;

import java.util.ArrayList;

/**
 * ServerManager
 *
 * Server manager create servers, interrupt it and count messages from client.
 */
public class ServerManager extends Thread {

    private int mFirstHostPort;
    private int mPortsCount;
    private ArrayList<ServerThread> mServerThreads;
    private Window mWindow;

    /**
     * Create ServerManager
     *
     * @param window        the Window instance for set count messages
     * @param firstHostPort first port for servers
     * @param portsCount    count ports
     */
    public ServerManager(Window window, int firstHostPort, int portsCount) {
        super();
        this.mFirstHostPort = firstHostPort;
        this.mPortsCount = portsCount;
        this.mWindow = window;
        this.mServerThreads = new ArrayList<>();
    }

    /**
     * Run when thread start
     */
    @Override
    public void run() {

        // Create mServerThreads
        for (int i = mFirstHostPort; i < mFirstHostPort + mPortsCount; i++) {
            mServerThreads.add(new ServerThread(i));
        }
        // Start mServerThreads
        for (ServerThread serverThread : mServerThreads) {
            serverThread.start();
        }

        // While ServerManager is not interrupted get count messages from all servers
        // and show on the window
        while (!this.isInterrupted()) {
            int countMessages = 0;
            for (ServerThread serverThread : mServerThreads) {
                countMessages += serverThread.getCountMessages();
            }
            mWindow.showCountMessages(countMessages);
        }

        // When ServerManager interrupted, interrupt all mServerThreads
        for (ServerThread serverThread : mServerThreads) {
            serverThread.interrupt();
        }
    }

}
