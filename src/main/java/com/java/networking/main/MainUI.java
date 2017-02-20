package com.java.networking.main;

import com.java.networking.api.NetworkMethod;
import com.java.networking.function.GoBackN;
import com.java.networking.function.NetworkReceiver;
import com.java.networking.function.SAW;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JOptionPane;

public class MainUI extends javax.swing.JFrame {

    private NetworkMethod saw;
    private NetworkMethod gobackN;
    
    /**
     * Creates new form MainUI
     */
    public MainUI() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnGoBack = new javax.swing.JButton();
        btnStopWait = new javax.swing.JButton();
        btnReceive = new javax.swing.JButton();
        txtIP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtLoss = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtMessage.setEditable(false);
        txtMessage.setBackground(new java.awt.Color(0, 0, 0));
        txtMessage.setColumns(20);
        txtMessage.setForeground(Color.white);
        txtMessage.setRows(5);
        jScrollPane1.setViewportView(txtMessage);

        jLabel1.setText("Message");

        btnGoBack.setText("Go back N");
        btnGoBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackActionPerformed(evt);
            }
        });

        btnStopWait.setText("Stop And Wait");
        btnStopWait.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopWaitActionPerformed(evt);
            }
        });

        btnReceive.setText("Receive");
        btnReceive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiveActionPerformed(evt);
            }
        });

        jLabel2.setText("IP Address");

        jLabel3.setText("Port");

        jLabel4.setText("Loss %");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 292, Short.MAX_VALUE)
                        .addComponent(btnReceive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStopWait)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGoBack))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtLoss))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtLoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGoBack)
                    .addComponent(btnStopWait)
                    .addComponent(btnReceive))
                .addContainerGap())
        );

        pack();
    }

    private void btnReceiveActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            int port;
            try {
                port = Integer.parseInt(txtPort.getText());
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
            new NetworkReceiver(port, this);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, "Input not valid!");
        }
    }

    private void btnStopWaitActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            String host = txtIP.getText();
            int port;
            int loss;
            try {
                port = Integer.parseInt(txtPort.getText());
                loss = Integer.parseInt(txtLoss.getText());
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
            saw = new SAW(loss, this);
            try (InputStream inputStream = new FileInputStream("COSC635_P2_DataSent.txt")) {
                saw.start(host, port, inputStream);
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }

        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, "Input not valid!");
        }
    }

    private void btnGoBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackActionPerformed

        try {
            int port;
            int loss;
            String host = txtIP.getText();
            try {
                port = Integer.parseInt(txtPort.getText());
                loss = Integer.parseInt(txtLoss.getText());
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
            gobackN = new GoBackN(loss, this);
            try (InputStream inputStream = new FileInputStream("COSC635_P2_DataSent.txt")) {
                gobackN.start(host, port, inputStream);
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, "given file not found!");
                throw new RuntimeException(e2);
            }

        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, "Invalid input!");
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }

    // Variables declaration 
    private javax.swing.JButton btnGoBack;
    private javax.swing.JButton btnReceive;
    private javax.swing.JButton btnStopWait;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtLoss;
    private javax.swing.JTextArea txtMessage;
    private javax.swing.JTextField txtPort;
    public void appendMessage(String text) {
        txtMessage.append(text + "\n");
    }

}
