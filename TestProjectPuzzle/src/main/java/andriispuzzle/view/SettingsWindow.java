package andriispuzzle.view;

import andriispuzzle.settings.SettingsActions;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class SettingsWindow extends javax.swing.JDialog {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel jPanel1;
    public SettingsWindow() {
        initComponents();

        jTextField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT
                        || e.getKeyCode() == KeyEvent.VK_RIGHT
                        || e.getKeyCode() == KeyEvent.VK_UP
                        || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    return;
                }
                try {
                    int newValue = Integer.parseInt(jTextField1.getText());
                    SettingsActions.getInstance().setPuzzlePieceNumber(newValue);
                } catch (NumberFormatException ex) {
                }
            }

        });

    }

    public void showUiSettings() {
        if (!this.isVisible()) {
            this.setVisible(true);
            this.setLocationRelativeTo(this.getParent());
        }
        this.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new JTabbedPane();
        jScrollPane2 = new JScrollPane();
        jPanel3 = new JPanel();
        jPanel11 = new JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(400, 300));
        setName("settings"); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 650));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setName("main-tabbed-pane"); // NOI18N


        jLabel2.setText("Number of puzzlepieces:");
        jPanel7.add(jLabel2);

        jTextField1.setColumns(5);
        jTextField1.setText("200");
        jTextField1.setName("puzzlepiece-number-textfield"); // NOI18N
        jPanel7.add(jTextField1);

        jPanel11.add(jPanel7);
        jPanel11.add(jLabel3);

        jPanel3.add(jPanel11);

        jScrollPane2.setViewportView(jPanel3);

        jTabbedPane1.addTab("Puzzle", jScrollPane2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Save");
        jButton1.setName("settings-save"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 20, 20);
        jPanel1.add(jButton1, gridBagConstraints);

        jButton2.setText("Cancel");
        jButton2.setName("settings-cancel"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);

        jPanel1.add(jButton2, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            SettingsActions.getInstance().saveSettingsToFile();
            this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            SettingsActions.getInstance().loadSettingsFromFile();

            this.dispose();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            SettingsActions.getInstance().loadSettingsFromFile();
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_formWindowClosed
    // End of variables declaration//GEN-END:variables
}
