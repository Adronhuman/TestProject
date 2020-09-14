package andriispuzzle.view;

import andriispuzzle.CreatePuzzleFile;
import andriispuzzle.PuzzleApp;
import andriispuzzle.SolveFromFile;
import andriispuzzle.functions.PuzzleActions;
import andriispuzzle.puzzle.Puzzle;
import andriispuzzle.puzzle.PuzzlePieceGroup;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;


public class PuzzleMainWindow extends javax.swing.JFrame {

    private final PuzzleArea puzzlearea;

    private final PuzzleWindow puzzleWindow;

    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private JMenuItem jMenuItem2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;

    public PuzzleMainWindow(PuzzleWindow puzzleWindow) {
        this.puzzleWindow = puzzleWindow;
        initComponents();

        puzzlearea = new PuzzleArea();

        jPanel1.add(puzzlearea);
        setPuzzleAreaStart(new Point(0, 0));

    }
    public void gameEnd(){
        System.out.println("Game end!");
        JDialog d = new JDialog(PuzzleApp.getInstance().getPuzzleWindow().getPuzzleMainWindow(), "Game end!!!");
        JLabel text = new JLabel(" ВИ СПРАВИЛИСЬ!");
        text.setFont(text.getFont().deriveFont((float)50));
        try {
            text.setIcon(new ImageIcon(ImageIO.read(new File("src\\main\\resources\\image\\011.jpg")).getScaledInstance(200,200,Image.SCALE_DEFAULT)));
        }catch (IOException ioException){
            System.out.println(ioException.getLocalizedMessage());
        }
        text.setHorizontalTextPosition(4);
        d.add(text);
        d.setLocation(300,200);
        d.setSize(700, 400);
        d.setVisible(true);
    }
    public void bringToFront(PuzzlePieceGroup puzzlepieceGroup) {
        puzzlearea.bringToFront(puzzlepieceGroup);
    }

    public Rectangle getPuzzleAreaBounds() {
        return new Rectangle(puzzlearea.getSize());
    }

    void setPuzzleAreaStart(Point p) {
        puzzlearea.setPuzzleAreaStart(p);
    }

    public void showPuzzleWindow() {
        this.setVisible(true);
    }

    public void setNewPuzzle(Puzzle puzzle) {
        puzzlearea.setNewPuzzle(puzzle);

        jMenuItem4.setEnabled(true);
        jMenuItem5.setEnabled(true);
    }

    public Puzzle getPuzzle() {
        return puzzlearea.getPuzzle();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenuItem1 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
        jMenuItem4 = new JMenuItem();
        jMenuItem5 = new JMenuItem();
        jMenu2 = new JMenu();
        jMenuItem8 = new JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Puzzle");
        setPreferredSize(new java.awt.Dimension(1200, 900));

        jPanel1.setName("puzzleArea-panel"); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setName("main-menu"); // NOI18N

        jMenu1.setText("Puzzle");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("add puzzle");
        jMenuItem1.setName("add-puzzle"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);


        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("shuffle puzzle");
        jMenuItem4.setName("puzzle-shuffle"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("restart puzzle");
        jMenuItem5.setName("puzzle-restart"); // NOI18N
        jMenuItem5.addActionListener(this::jMenuItem5ActionPerformed);
        jMenu1.add(jMenuItem5);

        jMenuItem2.setText("solve puzzle");
        jMenuItem2.setName("puzzle-solve");
        jMenuItem2.addActionListener(this::jMenuItem2ActionPerformed);
        jMenu1.add(jMenuItem2);


        jMenuBar1.add(jMenu1);

        jMenu2.setText("settings");
        jMenu2.setName("settings"); // NOI18N

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("settings");
        jMenuItem8.setName("appearance-settings"); // NOI18N
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(ActionEvent event) {
        new Thread(() -> {
            Puzzle puzzle = PuzzleActions.getInstance().getPuzzle();
            try {
                ImageIO.write(SolveFromFile.solve("puzzleRepo", puzzle.getRowCount(), puzzle.getColumnCount()), puzzle.getImageType(), new File("final\\kit." + puzzle.getImageType()));
            } catch (Exception ec) {
                System.out.println(ec.getLocalizedMessage());
            }
        }).start();
    }

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        this.puzzleWindow.showUiSettings();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        File selectedFile;

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("bmp, jpg , jpeg, png", "bmp", "jpg", "jpeg", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("jpg, jpeg", "jpg", "jpeg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        selectedFile = fileChooser.getSelectedFile();
        try {
            PuzzleActions.getInstance().newPuzzle(selectedFile);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        Puzzle puzzle = PuzzleActions.getInstance().getPuzzle();
        CreatePuzzleFile.saveToFile(puzzle);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new Thread(() -> {
            PuzzleActions.getInstance().shufflePuzzlePieces();
        }).start();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new Thread(() -> {
            PuzzleActions.getInstance().restartPuzzle();
        }).start();
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    // End of variables declaration//GEN-END:variables

}
