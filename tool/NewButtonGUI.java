/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sheet.music.maker.tool;

import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Ryan
 */
public class NewButtonGUI extends JFrame {
    
    JLabel howManyBars = new JLabel(" Number of Bars: ");
    JComboBox barsChoices;
    JLabel timeSignature = new JLabel(" Time Signature: ");
    JComboBox timeSigChoices;
    
    public NewButtonGUI(int numberOfBars, int timeSigCode) {
    
        setTitle("New Project");
        setSize(250,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        this.setLayout(new GridLayout(2,2));
        
        setButtonsUp(numberOfBars, timeSigCode);
        
        add(howManyBars);
        add(barsChoices);
        add(timeSignature);
        add(timeSigChoices);
        
    }
    
    private final void setButtonsUp(int numberOfBars, int timeSigCode) {
        
        String[] temp = new String[4];
        
        for (int i = 0; i < 4; i++) {
            temp[i] = String.valueOf(i+1);
        }
        
        barsChoices = new JComboBox(temp);
        barsChoices.setSelectedItem((String)(String.valueOf(numberOfBars)));
        
        temp = new String[] {"4 - 4", "3 - 4"};
        
        timeSigChoices = new JComboBox(temp);
        
        if (timeSigCode == 44)
            timeSigChoices.setSelectedIndex(0);
        else timeSigChoices.setSelectedIndex(1);
    }
}
