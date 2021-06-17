
 /*
public class test2 implements ActionListener, Runnable {    
     
} */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progressbartest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
/**
 *
 * @author Andrew
 */
public class ProgressbarTest implements ActionListener {

    private JProgressBar jpb = new JProgressBar(0,100);
    private JProgressBar jpb2 = new JProgressBar(0,100);
    private JButton aButton;
    private JButton bButton;
    private MyProgressBar myPb1;
    private MyProgressBar myPb2;    
     
    private void createAndShowGUI(){
        JFrame aFrame = new JFrame("Swing Thread Example:  Fixed Threading");
        aButton = new JButton("Player 1");
        aButton.addActionListener(this);        
        bButton = new JButton("Player 2");
        bButton.addActionListener(this);        
        JPanel aPanel = new JPanel(new GridLayout(2,2));
 //       aPanel.setLayout(new GridLayout(2,2));
 //       aPanel.add(new JLabel("Progress:"));
 //       jpb = new JProgressBar(0,100);      
        aPanel.add(jpb);
        aPanel.add(aButton);
//        jpb2 = new JProgressBar(0,100);      
        aPanel.add(jpb2);
        aPanel.add(bButton);
        aFrame.getContentPane().add(aPanel);                              
        aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aFrame.pack();
        aFrame.setVisible(true);  
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]){
        ProgressbarTest test = new ProgressbarTest(); 
       javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            test.createAndShowGUI();
        }
    });
       test.startProgressBarThreads();
    }
    
    private void startProgressBarThreads()
    {
        myPb1 = new MyProgressBar(jpb);  
            new Thread(myPb1).start();
        myPb2 = new MyProgressBar(jpb2);  
            new Thread(myPb2).start(); 
    }
     
    public void actionPerformed(ActionEvent ae){            
    // signal the worker thread to get crackin
    System.out.println("actionPerformed starts");
        if(ae.getSource() == aButton)
        {
            System.out.println("aButton");
            myPb1.setReStart(true);
        }
        if(ae.getSource() == bButton)
        {
            System.out.println("bButton");
            myPb2.setReStart(true);
        }
        
        synchronized(this){notifyAll();}
    }     
}

class MyProgressBar implements Runnable
{
    private JProgressBar jpbForThread;
    private boolean reStart = false;
    MyProgressBar(JProgressBar jpb)
    {
        jpbForThread = jpb;        
    }
    
    void setReStart(boolean start)
    {
        reStart = start;
    }
    public void run()
    {
        while(true){
         // wait for the signal from the GUI
            try{wait();}
            catch (InterruptedException e)
            {
            }
            catch (IllegalMonitorStateException e)
            {
            }
            if(!reStart) continue;
         // simulate some long-running process like parsing a large file
            for (int i = 0; i <= 100; i++){
               if(reStart)
               {
                   i = 0;
                   reStart = false;
               }
               jpbForThread.setValue(i);
               try{Thread.sleep(50);} // make the process last a while
               catch (InterruptedException e){}
           }
        }
    }
}
 