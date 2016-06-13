import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


//Action Listener focus lost - http://www.java2s.com/Code/JavaAPI/javax.swing/JTextFieldaddFocusListenerFocusListenerl.htm
public class ButtonPanel extends JPanel
{
	private static final short BUT_TEXT_STA_X_AXIS = 30, BUT_TEXT_Y_AXIS = 120, BUT_TEXT_WIDTH = 80,
								BUT_TEXT_HEIGHT = 40, BUT_TEXT_GAP = 50, BUT_TEXT_BIG_GAP = 100;
	private JButton start, stop, confirm, editCode;
	private JTextField gForce, rpm;
	
	public ButtonPanel()
	{
		initializeButtonP();
		initializeButtonVar();
	}
	
	
	private void initializeButtonP()
	{
		setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(this.getX(), 200));
		this.setLayout(null);
	}
	
	
	private void initializeButtonVar()
	{
		this.start = new JButton("Start");
		this.stop = new JButton("Stop");
		
		this.gForce = new JTextField("g force");
		this.rpm = new JTextField("rpm");
		
		this.editCode = new JButton("Edit Code");
		
		this.confirm = new JButton("Confirm");
		
		this.add(this.start);
		this.add(this.stop);
		
		this.add(this.gForce);
		this.add(this.rpm);
		
		this.add(this.editCode);
		
		this.add(this.confirm);
		
		int tmpCalc = BUT_TEXT_STA_X_AXIS;
		
		this.start.setBounds(tmpCalc, BUT_TEXT_Y_AXIS, BUT_TEXT_WIDTH, BUT_TEXT_HEIGHT);
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + BUT_TEXT_GAP;
		this.stop.setBounds(tmpCalc, BUT_TEXT_Y_AXIS, BUT_TEXT_WIDTH, BUT_TEXT_HEIGHT);
		
		
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + (2 * BUT_TEXT_BIG_GAP);
					
		this.gForce.setBounds(tmpCalc, BUT_TEXT_Y_AXIS, BUT_TEXT_WIDTH, BUT_TEXT_HEIGHT);
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + BUT_TEXT_GAP;
		this.rpm.setBounds(tmpCalc, BUT_TEXT_Y_AXIS, BUT_TEXT_WIDTH, BUT_TEXT_HEIGHT);
		
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + (2 * BUT_TEXT_BIG_GAP);
		
		this.editCode.setBounds(tmpCalc, BUT_TEXT_Y_AXIS, BUT_TEXT_WIDTH + BUT_TEXT_GAP, BUT_TEXT_HEIGHT);
		
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + (2 * BUT_TEXT_BIG_GAP) + BUT_TEXT_GAP;
		
		this.confirm.setBounds(tmpCalc, BUT_TEXT_Y_AXIS, BUT_TEXT_WIDTH + BUT_TEXT_GAP, BUT_TEXT_HEIGHT);
		
		
		//Listeners();
	}
	
	
	private void Listeners()
	{
		buttonListeners();
		textFieldListeners();
	}
	
	
	private void buttonListeners()
	{
		this.start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//start
			}
		});
		
		
		this.stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//stop
			}
		});
		
		
		this.editCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//editCode
			}
		});
		
		
		this.confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//confirm
			}
		});
	}
	
	
	private void textFieldListeners()
	{
		this.gForce.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0)
			{
				//Check value + boolean action = true
			}
			
			@Override
			public void focusGained(FocusEvent arg0)
			{
				//Do Nothing
			}
		});
		
		
		this.rpm.addFocusListener(new FocusListener(){
			
			@Override
			public void focusLost(FocusEvent arg0)
			{
				//Check value + boolean action = true
			}
			
			@Override
			public void focusGained(FocusEvent arg0)
			{
				//Do Nothing
			}
		});
	}
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
	}
	
	private void drawBasic(Graphics2D g2d)
	{
		
	}
	
	
	/*
	public void paintScreen()
	{
		Graphics g;
		try {
			g = getGraphics();
			//Graphics2D g2d = (Graphics2D) g;
			if(g != null && dbImage != null)
			{
				g.drawImage(dbImage, 0, 0, null);
			}
			g.dispose();
		}
		catch(Exception e)
		{
			System.out.println("Graphics error");
		}
	}
	*/
}
