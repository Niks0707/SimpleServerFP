package com.niks;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import static com.niks.Main.PORT_COUNT;
import static com.niks.Main.PORT_FIRST;

/**
 * Window
 * <p>
 * Create window for manage server
 */
public class Window extends JFrame implements ActionListener {

    public static final String BUTTON_START_NAME = "start";
    public static final String BUTTON_STOP_NAME = "stop";
    private JPanel mContentPane;
    private JTextArea mTextMessages;
    private JPanel mStatusPanel;
    private ServerManager mServer;
    private JLabel mLabelCountMessages;

    /**
     * Create the window with frame.
     **/
    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setMinimumSize(new Dimension(600, 450));

        // Create main panel
        mContentPane = new JPanel();
        mContentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(mContentPane);
        this.setBackground(Color.LIGHT_GRAY);

        // Init UI elements
        init();
    }

    /**
     * Was called when action perfomed
     *
     * @param arg0 action event
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch (((AbstractButton) arg0.getSource()).getName()) {
            // If button Start pressed create and start ServerManager
            case BUTTON_START_NAME:
                if (mServer != null) {
                    mServer.interrupt();
                }
                mServer = new ServerManager(Window.this, PORT_FIRST, PORT_COUNT);
                mServer.start();
                ((JLabel) mStatusPanel.getComponent(0)).setText(Text.SERVER_STARTED);
                break;
            // If button Stop pressed interrupt ServerManager
            case BUTTON_STOP_NAME:
                if (mServer != null) {
                    mServer.interrupt();
                }
                ((JLabel) mStatusPanel.getComponent(0)).setText(Text.SERVER_STOPED);
                break;
            default:
                break;
        }

    }

    /**
     * Show count messages on label
     *
     * @param count count messages
     */
    public synchronized void showCountMessages(int count) {
        mLabelCountMessages.setText("Count messages: " + count);
    }


    /**
     * Initialize UI elements
     */
    private void init() {
        // Create main layout
        GridBagLayout grid = new GridBagLayout();
        mContentPane.setLayout(grid);

        // GridBagConstraints set UI element params in GridBagLayout
        GridBagConstraints c = new GridBagConstraints();

        // Create button for start server
        JButton buttonStartServer = new JButton(Text.START_SERVER);
        buttonStartServer.addActionListener(this);
        buttonStartServer.setName(BUTTON_START_NAME);
        buttonStartServer.setSize(20, 20);
        c.insets = new Insets(5, 10, 10, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        getContentPane().add(buttonStartServer, c);

        // Create button for stop server
        JButton buttonStopServer = new JButton(Text.STOP_SERVER);
        buttonStopServer.addActionListener(this);
        buttonStopServer.setName(BUTTON_STOP_NAME);
        buttonStopServer.setSize(20, 20);
        c.insets = new Insets(5, 10, 10, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        getContentPane().add(buttonStopServer, c);

        // Create label for show count messages
        mLabelCountMessages = new JLabel();
        mLabelCountMessages.setSize(20, 20);
        c.insets = new Insets(5, 10, 10, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        getContentPane().add(mLabelCountMessages, c);

        // Create TextArea for show server log
        mTextMessages = new JTextArea();
        mTextMessages.setEditable(false);
        mTextMessages.setSize(200, 200);
        mTextMessages.setBorder(new LineBorder(Color.BLACK));
        mTextMessages.setLineWrap(true);
        // Set TextArea like standart output stream
        PrintStream printStream = new PrintStream(new CustomOutputStream(mTextMessages));
        System.setOut(printStream);
        System.setErr(printStream);

        // Create ScrollPane for TextArea for scrolling text
        JScrollPane areaScrollPane = new JScrollPane(mTextMessages);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        areaScrollPane.setBorder(new TitledBorder(new LineBorder(Color.WHITE), Text.INPUT));
        c.insets = new Insets(0, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        getContentPane().add(areaScrollPane, c);

        // Create StatusPanel for show information about server state
        mStatusPanel = new JPanel();
        mStatusPanel.setLayout(new BorderLayout());
        mStatusPanel.add(new JLabel(Text.SERVER_STOPED), BorderLayout.CENTER);
        mStatusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.SOUTH;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 6;
        c.weightx = 1.0f;
        c.weighty = 0.005f;
        getContentPane().add(mStatusPanel, c);
    }
}

