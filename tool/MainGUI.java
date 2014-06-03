/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sheet.music.maker.tool;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class MainGUI extends JFrame implements MouseListener, ActionListener, KeyListener, MouseMotionListener {
    
    Timer t = new Timer(1,this);
    JPanel bottomButtonPanel = new JPanel();
    JPanel topButtonPanel = new JPanel();
    JButton newButton = new JButton("New");
    JLabel tempoLabel = new JLabel("Tempo:");
    JButton playButton = new JButton("Play");
    JButton pauseButton = new JButton("Pause");
    JButton stopButton = new JButton("Stop");
    JToggleButton loopButton = new JToggleButton("Loop");
    JComboBox tempoSelect;
    JButton helpButton = new JButton("Help");
    JToggleButton addDot = new JToggleButton();
    JToggleButton showQuarterCount = new JToggleButton("Quarter Count On");
    JToggleButton showEigthCount = new JToggleButton("Eigth Count On");
    JToggleButton showSixteenthCount = new JToggleButton("Sixteenth Count On");
    JToggleButton playLineButton = new JToggleButton("Play Line On");
    JButton clearButton = new JButton("Clear Notes");
    Staff staff;
    NewButtonGUI newProject = null;
    
    public MainGUI() {
        
        t.start();
        setSize(1050,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        this.setLayout(new BorderLayout());
        
        addButtonFunctions();
        
        setUpBottomButtons();
        setUpTopButtons();
        
        staff = new Staff();
        
        staff.addMouseListener(this);
        staff.addKeyListener(this);
        staff.addMouseMotionListener(this);
        addKeyListener(this);
        topButtonPanel.addMouseListener(this);
        bottomButtonPanel.addMouseListener(this);
                
        addMouseListener(this);
        
        add(bottomButtonPanel, BorderLayout.SOUTH);
        add(topButtonPanel,BorderLayout.NORTH);
        add(staff,BorderLayout.CENTER);
        validate();
    }
    
    public void init() {
        t.start();
        setSize(1050,400);
        //setLocationRelativeTo(null);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        //setResizable(false);
        this.setLayout(new BorderLayout());
        
        addButtonFunctions();
        
        setUpBottomButtons();
        setUpTopButtons();
        
        staff = new Staff();
        
        staff.addMouseListener(this);
        staff.addKeyListener(this);
        staff.addMouseMotionListener(this);
        addKeyListener(this);
        topButtonPanel.addMouseListener(this);
        bottomButtonPanel.addMouseListener(this);
                
        addMouseListener(this);
        
        add(bottomButtonPanel, BorderLayout.SOUTH);
        add(topButtonPanel,BorderLayout.NORTH);
        add(staff,BorderLayout.CENTER);
        validate();
    }
    
    private void setUpBottomButtons() {
        
        playLineButton.addKeyListener(this);
        showQuarterCount.addKeyListener(this);
        showEigthCount.addKeyListener(this);
        showSixteenthCount.addKeyListener(this);
        clearButton.addKeyListener(this);
        
        bottomButtonPanel.add(clearButton);
        bottomButtonPanel.add(playLineButton);
        bottomButtonPanel.add(showQuarterCount);
        bottomButtonPanel.add(showEigthCount);
        bottomButtonPanel.add(showSixteenthCount);
    }

    private void setUpTopButtons() {
        
        String[] tempos = new String[169]; 
        topButtonPanel.setLayout(new GridLayout(1,8));
        JPanel tempoPanel = new JPanel();
        tempoPanel.setLayout(new GridLayout(1,2));
        
        for (int i = 0; i < 169; i++) {
            tempos[i] = String.valueOf(i+40);
        }
        
        tempoSelect = new JComboBox(tempos);
        tempoSelect.setSelectedIndex(110);
        tempoSelect.setSize(30, WIDTH);
        
        newButton.addKeyListener(this);
        playButton.addKeyListener(this);
        pauseButton.addKeyListener(this);
        stopButton.addKeyListener(this);
        loopButton.addKeyListener(this);
        tempoLabel.addKeyListener(this);
        tempoSelect.addKeyListener(this);
        helpButton.addKeyListener(this);
        
        topButtonPanel.add(newButton);
        topButtonPanel.add(playButton);
        topButtonPanel.add(pauseButton);
        topButtonPanel.add(stopButton);
        topButtonPanel.add(loopButton);
        tempoPanel.add(tempoLabel);
        tempoPanel.add(tempoSelect);
        topButtonPanel.add(tempoPanel);
        topButtonPanel.add(helpButton);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        staff.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        staff.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        staff.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (staff != null) {
            if (tempoSelect != null)
                staff.tempo = tempoSelect.getSelectedIndex();
            staff.actionPerformed(e);
        }
        
        if (newProject != null) {

            if (staff.numberOfBars != Integer.parseInt((String)newProject.barsChoices.getSelectedItem())) {
                staff.changeNumberOfBars(Integer.parseInt((String)newProject.barsChoices.getSelectedItem()));
                staff.staffShiftX = 0;
            }
            
            if (newProject.timeSigChoices.getSelectedItem().equals("4 - 4")) {
                
                boolean clearNotes = false;
                
                if (staff.timeSigCode != 44)
                    clearNotes = true;
                
                staff.timeSig = 4;
                staff.timeSigCode = 44;
                
                if (clearNotes) staff.clearNotes();
            }
            else {
                
                boolean clearNotes = false;
                
                if (staff.timeSigCode != 34)
                    clearNotes = true;
                
                staff.timeSig = 3;
                staff.timeSigCode = 34;
                
                if (clearNotes) staff.clearNotes();
            }
            
        }
    }

    public void addButtonFunctions() {

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!staff.playBack) {
                    staff.playBack = true;
                    staff.timePassed = System.currentTimeMillis() + staff.pauseDelay;
                    if (!staff.pause) {
                        staff.staffShiftX = 0;
                        staff.resetNotePositions();
                    }
                    staff.pause = false;
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                staff.playBack = false;
                staff.pause = true;
                staff.oneTimePause = true;
                staff.staffShiftVelX = 0;
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                staff.playBack = false;
                staff.playBackPosition = 0;
                staff.playLineX = 120;
                staff.pauseDelay = 0;
                staff.staffShiftVelX = 0;
                staff.staffShiftX = 0;
                staff.resetNotePositions();
            }
        });
        
        loopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton tBtn = (JToggleButton)e.getSource();
                if (tBtn.isSelected()) {
                    staff.loop = true;
                } else {
                    staff.loop = false;
                }
            }
        });
        
        playLineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton tBtn = (JToggleButton)e.getSource();
                if (tBtn.isSelected()) {
                    playLineButton.setText("Play Line Off");
                    staff.playLineOn = false;
                } else {
                    playLineButton.setText("Play Line On");
                    staff.playLineOn = true;
                }
            }
        });
        
        showQuarterCount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton tBtn = (JToggleButton)e.getSource();
                if (tBtn.isSelected()) {
                    staff.showQuarterCount = false;
                    showQuarterCount.setText("Quarter Count Off");
                } else {
                    staff.showQuarterCount = true;
                    showQuarterCount.setText("Quarter Count On");
                }
            }
        });
        
        showEigthCount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton tBtn = (JToggleButton)e.getSource();
                if (tBtn.isSelected()) {
                    staff.showEigthCount = false;
                    showEigthCount.setText("Eigth Count Off");
                } else {
                    staff.showEigthCount = true;
                    showQuarterCount.setText("Eigth Count On");
                }
            }
        });
        
        showSixteenthCount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton tBtn = (JToggleButton)e.getSource();
                if (tBtn.isSelected()) {
                    staff.showSixteenthCount = false;
                    showSixteenthCount.setText("Sixteenth Count Off");
                } else {
                    staff.showSixteenthCount = true;
                    showSixteenthCount.setText("Sixteenth Count On");
                }
            }
        });
        
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                newProject = new NewButtonGUI(staff.numberOfBars,staff.timeSigCode);

            }
        });
        
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String note = "Click and drag the staff to move it left or right \n"
                        + "This can also be accomplished using the arrow keys";
                
                JOptionPane.showMessageDialog(null,note);
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                staff.clearNotes();
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        staff.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        staff.keyReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        staff.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

}
