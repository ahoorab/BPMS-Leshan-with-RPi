package org.eclipse.leshan.server.demo;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.registration.Registration;

public class MonitorGui extends JFrame{
	
	public static JButton observeTempButton;
	
	public static JTextArea infotext;

	private static final long serialVersionUID = 1L;
	private JFrame frame ;
	public MonitorGui(){
		//frame = new JFrame("Health Monitoring Application");
		super("Blood Pressure Monitoring System App");
		
		this.setSize(400,600);
		this.setResizable(true);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.lightGray);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);


		observeTempButton=new JButton("observe TEMPΕRATURE");
		observeTempButton.setBounds(50,50,250,30);
		observeTempButton.setBackground(Color.GRAY);
		observeTempButton.setVisible(true);
		this.add(observeTempButton);
		observeTempButton.addActionListener(new ButtonObserveHandler());
		
		infotext= new JTextArea();
		infotext.setBounds(20,150,300,400);
		infotext.setVisible(true);
	

		JScrollPane scroll = new JScrollPane(infotext);
		scroll.setVisible(true);
		scroll.setBounds(20,150,300,400);
		this.getContentPane().add(scroll);
		this.setVisible(true);
		
	}
	 
	class CloseWindowAndExit extends WindowAdapter {
		public void windowClosing (WindowEvent closeWindowAndExit){
			System.exit(0);
		}
	}
}
class ButtonObserveHandler implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{ 	
			for(Registration registration : RegistrationManager.registrations.values()) {
				if(registration.getEndpoint().equals("bpms_lechan_client")) {
			    	  ObserveRequest observetemp = new ObserveRequest("/3303/5700/0");
						
						 try {
							
							LwM2mResponse response1= RegistrationManager.server.send(registration, observetemp);
							System.out.println("device response: " + response1);
							MonitorGui.infotext.setText(MonitorGui.infotext.getText() +"\n" +"server observes temperature sensor!"+ "\n");
						      
						} catch (InterruptedException e1) {
							
							e1.printStackTrace();
						}
					}
			  
		      }
			}
}
		
	



