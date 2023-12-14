package me.tapeline.quail.qdk.debugclient.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DebuggerWindow extends JFrame {
    private JList<String> feedbackList;
    private JButton evaluateButton;
    private JTextField expr;
    private JTable scopeTable;
    private JButton refreshScopeButton;
    private JLabel debuggerTargetLabel;
    private JLabel currentLineLabel;
    private JPanel root;
    private JButton nextBreakpointButton;
    private JButton nextLineButton;
    private JButton stopButton;
    private GUIDebugClient client;

    public DebuggerWindow(String host, int port, HashMap<String, Set<Integer>> breakpoints) throws IOException {
        super("Quail Remote Debugger Client");
        setSize(640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(root);
        blockUI();
        feedbackList.setModel(new DefaultListModel<>());
        debuggerTargetLabel.setText(host + ":" + port);
        client = new GUIDebugClient(host, (short) port);
        client.sendInitialBreakpoints(breakpoints);
        client.breakpointReachListeners.add((file, line) -> {
            unlockUI();
            currentLineLabel.setText(line + ":" + file);
        });
        nextBreakpointButton.addActionListener(e -> {
            try {
                client.resumeRunning();
                blockUI();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        nextLineButton.addActionListener(e -> {
            try {
                client.nextLine();
                blockUI();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        refreshScopeButton.addActionListener(e -> loadMem());
        evaluateButton.addActionListener(e -> {
            try {
                ((DefaultListModel<String>) feedbackList.getModel())
                        .insertElementAt(client.evalExpression(expr.getText()), 0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        stopButton.addActionListener(e -> {
            try {
                client.stopDebugger();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void blockUI() {
        evaluateButton.setEnabled(false);
        refreshScopeButton.setEnabled(false);
        nextLineButton.setEnabled(false);
        nextBreakpointButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    private void unlockUI() {
        evaluateButton.setEnabled(true);
        refreshScopeButton.setEnabled(true);
        nextLineButton.setEnabled(true);
        nextBreakpointButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    private void loadMem() {
        try {
            HashMap<String, String> mem = client.getScopeContents();
            ((DefaultTableModel) scopeTable.getModel()).setRowCount(0);
            ((DefaultTableModel) scopeTable.getModel()).setColumnCount(0);
            ((DefaultTableModel) scopeTable.getModel()).addColumn("Name");
            ((DefaultTableModel) scopeTable.getModel()).addColumn("Value");
            for (Map.Entry<String, String> entry : mem.entrySet())
                ((DefaultTableModel) scopeTable.getModel()).addRow(
                        new Object[] {entry.getKey(), entry.getValue()});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runGuiDebugger() throws IOException {
        String target = JOptionPane.showInputDialog("Enter host:port to connect");
        if (target == null || target.isEmpty()) return;
        String[] parts = target.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        String packedBreakpoints = PromptBreakpointsDialog.dialog();
        HashMap<String, Set<Integer>> breakpoints = new HashMap<>();
        Arrays.stream(packedBreakpoints.split("\n")).forEach((bpLine) -> {
            String fileName = bpLine.substring(0, bpLine.indexOf(';'));
            String lines = bpLine.substring(bpLine.indexOf(';') + 1);
            breakpoints.put(fileName, Arrays.stream(lines.split(";"))
                    .map(Integer::parseInt).collect(Collectors.toSet()));
        });
        DebuggerWindow window = new DebuggerWindow(host, port, breakpoints);
        window.setVisible(true);
    }

    public static void runGuiDebuggerDebug() throws IOException {
        String packedBreakpoints = PromptBreakpointsDialog.dialog();
        HashMap<String, Set<Integer>> breakpoints = new HashMap<>();
        Arrays.stream(packedBreakpoints.split("\n")).forEach((bpLine) -> {
            String fileName = bpLine.substring(0, bpLine.indexOf(';'));
            String lines = bpLine.substring(bpLine.indexOf(';') + 1);
            breakpoints.put(fileName, Arrays.stream(lines.split(";"))
                    .map(Integer::parseInt).collect(Collectors.toSet()));
        });
        DebuggerWindow window = new DebuggerWindow("localhost", 4004, breakpoints);
        window.setVisible(true);
    }

}
