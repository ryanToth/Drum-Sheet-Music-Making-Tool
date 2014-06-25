/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sheet.music.maker.tool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Ryan
 */
public class Staff extends JPanel implements ActionListener, KeyListener, MouseMotionListener {

    int height = 290, width = 1050;
    Timer t = new Timer(1, this);
    int timeSigCode = 44;
    int timeSig = 4;
    int noteGetsBeat = 1;
    int numberOfBars = 1;
    double staffShiftX = 0;
    double staffShiftVelX = 0;
    double tempo = 120;
    Rectangle[] hiHatNotePlaces;
    Rectangle[] snareNotePlaces;
    Rectangle[] bassNotePlaces;
    Note[] hiHatNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
    Note[] snareNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
    Note[] bassNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
    boolean playBack = false;
    double wait;
    int playBackPosition = 0;
    long timePassed = 0;
    boolean oneTimePause = false;
    boolean pause = false;
    long pauseDelay = 0;
    boolean loop = false;
    double playLineX;
    double playLineXPause;
    double playLineXPauseShift = 0;
    boolean playLineOn = true;
    long paintPauseDelay = 0;
    boolean makePauseWork = false;
    boolean showQuarterCount = true;
    boolean showEigthCount = true;
    boolean showSixteenthCount = true;
    boolean drag = false;
    boolean setStartingDragX = false;
    double startingDragX = 0;
    double currentDragX = 0;
    double beforeStaffShiftX = 0;

    Image timeSignature;
    URL imageurl;

    public Staff() {
       
        t.start();
    }
    
    public void init() {
        t.start();
    }
    
    public void paint(Graphics g) {

        super.paint(g);
        repaint();

        setNotePlaces();

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);
        g2.fill(new Rectangle(0, 0, width, height));

        g2.setStroke(new BasicStroke(1));

        g2.setColor(Color.gray);
        
        for (int j = 120; j < (width - 50) + ((width - 155) * (numberOfBars - 1)); j += (width - 140) / (timeSig * 4)) {

                g2.setStroke(new BasicStroke(1));
                g2.setColor(Color.gray);
                g2.drawLine(j - (int) staffShiftX, 80, j - (int) staffShiftX, 200);
            }

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new Rectangle(50 - (int) staffShiftX, 80, width - 110, 120));
        g2.drawLine(50 - (int) staffShiftX, 140, width - 60 - (int) staffShiftX, 140);

        for (int i = 1; i < numberOfBars; i++) {
            
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Rectangle((width - 60) + (width-155)*(i - 1) - (int) staffShiftX, 80, width - 155, 120));
            g2.drawLine((width - 60) + (width-155)*(i - 1) - (int) staffShiftX, 140, (width - 60) + (width - 155) * i - (int) staffShiftX, 140);
        }

        drawEndLine(g2, numberOfBars);

        if (timeSigCode == 34) {
            imageurl = getClass().getResource("3-4.png");
        } else {
            imageurl = getClass().getResource("4-4.png");
        }
        timeSignature = Toolkit.getDefaultToolkit().getImage(imageurl);

        g.drawImage(timeSignature, 50 - (int) staffShiftX, 80, 55, 120, null);

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        int j = 0;
        
        for (int i = 0; i < timeSig * 4 * numberOfBars; i++) {

            if (showQuarterCount && i % 4 == 0) {
                g2.drawString(String.valueOf(j / 4 + 1), 115 + (i) * (width - 152) / (timeSig * 4) - (int) staffShiftX, 280);
            }
            if (showEigthCount && (i + 2) % 4 == 0) {
                g2.drawString("+", 115 + (i) * (width - 152) / (timeSig * 4) - (int) staffShiftX, 280);
            }
            if (showSixteenthCount && (i + 1) % 4 == 0) {
                g2.drawString("a", 115 + (i) * (width - 152) / (timeSig * 4) - (int) staffShiftX, 280);
            }
            if (showSixteenthCount && (i + 3) % 4 == 0) {
                g2.drawString("e", 115 + (i) * (width - 152) / (timeSig * 4) - (int) staffShiftX, 280);
            }

            j++;
            
            if (j == timeSig * 4) j = 0;
        }

        for (int i = 0; i < hiHatNotes.length; i++) {
            if (hiHatNotes[i] != null) {
                hiHatNotes[i].paint(g);
            }
            if (snareNotes[i] != null) {
                snareNotes[i].paint(g);
            }
            if (bassNotes[i] != null) {
                bassNotes[i].paint(g);
            }
        }

        if (((playBack || pauseDelay != 0) || System.currentTimeMillis() - timePassed + pauseDelay < wait && playBackPosition > 0) && playLineOn) {
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.red);
            
            int x;
            
            if (pause) x = (int)playLineXPause + (int)staffShiftX;
            else x = (int)playLineX;
            
            g2.drawLine(x, 80, x, 200);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        wait = 60000 / (tempo * 4);
        
        if (staffShiftX + staffShiftVelX > 0 && staffShiftX + staffShiftVelX < getLocationOfEndOfBar() && !playBack) {
            
            staffShiftX += staffShiftVelX;
            
            if (pause) {
                playLineXPauseShift += staffShiftVelX;
            }
        }
        
        double dragShift = beforeStaffShiftX + startingDragX - currentDragX;
        
        if (drag && dragShift > 0 && dragShift <  getLocationOfEndOfBar()) {
            staffShiftX = beforeStaffShiftX + startingDragX - currentDragX;
        }
        
        moveNotes();
        
        if (oneTimePause) {
            pauseDelay = System.currentTimeMillis() - timePassed;
            paintPauseDelay = pauseDelay;
            oneTimePause = false;
            playLineXPauseShift = 0;
        }
        
        if (playBack) {
            
            if (playBackPosition == 0) playLineX = 120;
            
            if (pauseDelay != 0 && !makePauseWork) {
                timePassed = System.currentTimeMillis();
                makePauseWork = true;
            }
            
            if (System.currentTimeMillis() - timePassed + pauseDelay > wait) {
                
                pauseDelay = 0;
                makePauseWork = false;
                
                if (hiHatNotes[playBackPosition] != null) {
                    makeNoise(1);
                }
                if (snareNotes[playBackPosition] != null) {
                    makeNoise(2);
                }
                if (bassNotes[playBackPosition] != null) {
                    makeNoise(3);
                }
                
                playBackPosition++;
                
                timePassed = System.currentTimeMillis();

                if (playBackPosition == timeSig * 4 * numberOfBars) {

                    playBackPosition = 0;
                    staffShiftX = 0;

                    resetNotePositions();

                    if (!loop) {
                        playBack = false;
                    }
                }
            }
            
            int beginScroll;
            
            if (timeSig == 4) beginScroll = 9;
            else beginScroll = 6;
            
            if (playBackPosition < beginScroll)
                playLineX = 120 + ((playBackPosition) * (width - 152) / (timeSig * 4));
            
            else if (playBackPosition < (timeSig * 4 * numberOfBars - (beginScroll - 2))) {
                staffShiftX = ((playBackPosition-(beginScroll - 1)) * ((width - 152) / (timeSig * 4)));
                moveNotes();
            }
            else 
                playLineX = 120 - staffShiftX + ((playBackPosition) * (width - 152) / (timeSig * 4));  

            playLineXPause = playLineX;
            playLineXPauseShift = staffShiftX;
            paintPauseDelay = pauseDelay;
        }
    }

    public void mouseClicked(MouseEvent e) {

        for (int i = 0; i < hiHatNotePlaces.length; i++) {

            if (hiHatNotePlaces[i].contains(e.getPoint())) {

                if (hiHatNotes[i] == null) {
                    hiHatNotes[i] = new Note(timeSig, hiHatNotePlaces[i].getCenterX() + staffShiftX, hiHatNotePlaces[i].getCenterY(), 1, (width - 152) / (timeSig * 4));
                    hiHatNotes[i].shift = staffShiftX;
                } else {
                    hiHatNotes[i] = null;
                }
            } else if (snareNotePlaces[i].contains(e.getPoint())) {

                if (snareNotes[i] == null) {
                    snareNotes[i] = new Note(timeSig, snareNotePlaces[i].getCenterX() + staffShiftX, snareNotePlaces[i].getCenterY(), 2, (width - 152) / (timeSig * 4));
                    snareNotes[i].shift = staffShiftX;
                } else {
                    snareNotes[i] = null;
                }

            } else if (bassNotePlaces[i].contains(e.getPoint())) {

                if (bassNotes[i] == null) {
                    bassNotes[i] = new Note(timeSig, bassNotePlaces[i].getCenterX() + staffShiftX, bassNotePlaces[i].getCenterY(), 3, (width - 152) / (timeSig * 4));
                    bassNotes[i].shift = staffShiftX;
                } else {
                    bassNotes[i] = null;
                }
            }
        }
        updateNotes();
    }

    public void mousePressed(MouseEvent e) {
        
        drag = true;
        
        if (!setStartingDragX) {
            setStartingDragX = true;
            startingDragX = e.getX();
            currentDragX = e.getX();
            beforeStaffShiftX = staffShiftX;
        }
        
    }

    public void mouseReleased(MouseEvent e) {
        
        drag = false;
        setStartingDragX = false;
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }
    
    public void drawEndLine(Graphics2D g2, int barNumber) {
        
        g2.draw(new Rectangle((width-60)+(width-155)*(barNumber-1) - (int) staffShiftX,80,10,120));
        g2.drawLine((width-60)+(width-155)*(barNumber-1) - (int) staffShiftX,140, (width-50)+(width-155)*(barNumber-1) - (int) staffShiftX, 140);
    }
    
    public void setNotePlaces() {
        
        hiHatNotePlaces = new Rectangle[timeSig * 4 * noteGetsBeat * numberOfBars];
        snareNotePlaces = new Rectangle[timeSig * 4 * noteGetsBeat * numberOfBars];
        bassNotePlaces = new Rectangle[timeSig * 4 * noteGetsBeat * numberOfBars];
        
        for (int i = 0; i < timeSig*4*noteGetsBeat*numberOfBars; i++) {
            hiHatNotePlaces[i] = new Rectangle(100+i*(width-152)/(timeSig*4*noteGetsBeat) - (int) staffShiftX,60,40,40);
            snareNotePlaces[i] = new Rectangle(100+i*(width-152)/(timeSig*4*noteGetsBeat) - (int) staffShiftX,120,40,40);
            bassNotePlaces[i] = new Rectangle(100+i*(width-152)/(timeSig*4*noteGetsBeat) - (int) staffShiftX,180,40,40);
        }
    }
    
    public void updateNotes() {
        
        helpUpdateNotes(hiHatNotes);
        helpUpdateNotes(snareNotes);
        helpUpdateNotes(bassNotes);
    }
    
    public void helpUpdateNotes(Note[] tmp) {
        boolean stop = false;
        double noteValue = 1;
        
        for (int i = 0; i < timeSig*4*noteGetsBeat*numberOfBars; i++) {
            
            stop = false;
            noteValue = 1;
            
            for (int j = i+1; i < timeSig*4*noteGetsBeat*numberOfBars && !stop && tmp[i] != null && !(j+1 > timeSig*4*noteGetsBeat*numberOfBars); j++) {
                if (j%(timeSig*4) == 0) stop = true;
                else if (tmp[j] == null) noteValue++;
                else stop = true;
            }
            
            if (tmp[i] != null) {
                tmp[i].value = noteValue/4;
            }
        }
        
        for (int i = 0; i < timeSig*4*noteGetsBeat*numberOfBars; i++) {
            if (tmp[i] != null) {
                tmp[i].connected = false;
                tmp[i].dottedConnected = false;
                
                if (i > 2 && tmp[i-3] != null && (tmp[i-3].value == 0.75 || tmp[i-3].value == 0.5 || (tmp[i-2].value == 0.5 && tmp[i-3].value == 0.25)) && tmp[i].value < 1
                        && (i+1)%4 == 0)
                    tmp[i].dottedConnected = true;
                
                if (i > 1 && tmp[i-2] != null && tmp[i-2].value == tmp[i].value && i%4 != 0)
                    tmp[i].connected = true;
                
                if (i > 0 && tmp[i-1] != null && tmp[i-1].value == tmp[i].value && (i%4 != 0 || tmp[i].dottedConnected) && !tmp[i-1].dottedConnected)
                    tmp[i].connected = true;
                
            }
        }
    }

    public void makeNoise(int drum) {

        InputStream in = null;
        try {
            if (drum == 1) {
                in = getClass().getResourceAsStream("/sheet/music/maker/tool/A_HAT_1.wav");
            } else if (drum == 2) {
                in = getClass().getResourceAsStream("/sheet/music/maker/tool/D_SNARE_01.wav");
            } else if (drum == 3) {
                in = getClass().getResourceAsStream("/sheet/music/maker/tool/B_KICK_01.wav");
            }
            AudioStream audios = new AudioStream(in);
            AudioPlayer.player.start(audios);
        } catch (Exception e) {
            System.out.println("No");
        }

    }
    
    
    public void keyTyped(KeyEvent e) {
        
    }

    
    public void keyPressed(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if (code == e.VK_LEFT) {
            staffShiftVelX = -1;
        }
        
        if (code == e.VK_RIGHT) {
            staffShiftVelX = 1;
        }
    }

        public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == e.VK_LEFT || code == e.VK_RIGHT) {
            staffShiftVelX = 0;
        }
    }

    public double getLocationOfEndOfBar() {
        return (width - 130) * (numberOfBars - 1);
    }

    public void moveNotes() {

        for (int i = 0; i < hiHatNotes.length; i++) {

            if (staffShiftX + staffShiftVelX > 0 && staffShiftX + staffShiftVelX < getLocationOfEndOfBar()) {
                if (hiHatNotes[i] != null) {
                    hiHatNotes[i].shift = staffShiftX;
                }
                if (snareNotes[i] != null) {
                    snareNotes[i].shift = staffShiftX;
                }
                if (bassNotes[i] != null) {
                    bassNotes[i].shift = staffShiftX;
                }
            }
        }
    }

    public void resetNotePositions() {

        for (int i = 0; i < hiHatNotes.length; i++) {

            if (hiHatNotes[i] != null) {
                hiHatNotes[i].shift = 0;
            }
            if (snareNotes[i] != null) {
                snareNotes[i].shift = 0;
            }
            if (bassNotes[i] != null) {
                bassNotes[i].shift = 0;
            }
        }
    }

    public void clearNotes() {

        hiHatNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        snareNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        bassNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];

        setNotePlaces();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentDragX = e.getX();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
    public void changeNumberOfBars(int numberOfBars) {
        
        int temporaryBars;
        
        if (numberOfBars > this.numberOfBars) temporaryBars = this.numberOfBars;
        else temporaryBars = numberOfBars;
        
        Note[] tmp1 = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        Note[] tmp2 = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        Note[] tmp3 = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        
        for (int i = 0; i < timeSig * 4 * noteGetsBeat * temporaryBars; i++) {
            tmp1[i] = hiHatNotes[i];
            tmp2[i] = snareNotes[i];
            tmp3[i] = bassNotes[i];
        }
        
        hiHatNotes = tmp1;
        snareNotes = tmp2;
        bassNotes = tmp3;
        
        this.numberOfBars = numberOfBars;
    }
    
    public void saveStaff(String songName) throws IOException {
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("files/saved_songs.txt", true));
            writer.append("\n" + songName);
            writer.close();

            PrintWriter writer2 = new PrintWriter(new File("files/" + songName + ".txt"));

            writer2.println(timeSigCode);
            writer2.println(tempo);
            writer2.println(numberOfBars);
            
            for (int i = 0; i < hiHatNotes.length; i++) {
                if (hiHatNotes[i] != null)
                    writer2.print("o");
                else
                    writer2.print("-");
            }
            
            writer2.print("\n");
            
            for (int i = 0; i < snareNotes.length; i++) {
                if (snareNotes[i] != null)
                    writer2.print("o");
                else
                    writer2.print("-");
            }
            
            writer2.print("\n");
            
            for (int i = 0; i < bassNotes.length; i++) {
                if (bassNotes[i] != null)
                    writer2.print("o");
                else
                    writer2.print("-");
            }
            
            writer2.close();
        } 
        
        catch (Exception e) {
            Object[] options = {"Return"};
                JOptionPane.showOptionDialog(null, "/files/ not found", "Error",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);
        }
    }
    
    public void loadStaff(int timeSigCode, double tempo, int numberOfBars, String[] hiHat, String[] snare, String[] bass) {
        
        clearNotes();
        
        this.timeSigCode = timeSigCode;
        this.tempo = tempo;
        this.numberOfBars = numberOfBars;
        timeSig = timeSigCode/10;
        
        hiHatNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        snareNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        bassNotes = new Note[timeSig * 4 * noteGetsBeat * numberOfBars];
        
        changeNumberOfBars(numberOfBars);
        setNotePlaces();
        
        for (int i = 0; i < hiHatNotes.length; i++) {
            
            if (hiHat[i+1].equals("o")) hiHatNotes[i] = new Note(timeSig, hiHatNotePlaces[i].getCenterX() + staffShiftX, hiHatNotePlaces[i].getCenterY(), 1, (width - 152) / (timeSig * 4));
            if (snare[i+1].equals("o")) snareNotes[i] = new Note(timeSig, snareNotePlaces[i].getCenterX() + staffShiftX, snareNotePlaces[i].getCenterY(), 2, (width - 152) / (timeSig * 4));
            if (bass[i+1].equals("o")) bassNotes[i] = new Note(timeSig, bassNotePlaces[i].getCenterX() + staffShiftX, bassNotePlaces[i].getCenterY(), 3, (width - 152) / (timeSig * 4));
        }
        
        updateNotes();
    }
}
