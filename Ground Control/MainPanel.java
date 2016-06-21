import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.zu.ardulink.Link;

public class MainPanel extends JPanel implements Runnable
{
	private static final short OFF = 0, ON1 = 1, ON2 = 2, ERROR = -1;
	private static final String ID_START = "1", ID_STOP = "2", ID_G_FORCE = "3", ID_RPM = "4", 
								ID_EDIT_CODE = "5";
	
	private IndicatorPanel indicatorP;
	private GraphPanel graphP;
	private ButtonPanel buttonP;
	private Link link;

	private boolean running;

	
	/*
	 * MainPanel constructor
	 */
	public MainPanel()
	{
		createPanels();
		initialize();
	}

	
	
	/*
	 * Creates the panels which are going to be places on this panel  
	 */
	public void createPanels()
	{
		this.link = Link.getDefaultInstance();
		
		this.indicatorP = new IndicatorPanel();
		this.graphP = new GraphPanel(this.link);
		this.buttonP = new ButtonPanel(this.link);
	}

	
	/*
	 * Initializes the whole panel, calls initializeMainP() and InitializeMainVar()
	 */
	public void initialize()
	{
		initializeMainP();
		initializeMainVar();
	}
	
	
	
	/*
	 * Initializes the Jpanel's parameters
	 */
	private void initializeMainP()
	{
		this.setLayout(new BorderLayout());
		
		//setBackground(Color.BLUE);
	}
	
	
	
	/*
	 * Initialized the MainPanel's local parameters
	 */
	private void initializeMainVar()
	{
		add(this.indicatorP, BorderLayout.CENTER);
		add(this.graphP, BorderLayout.EAST);
		add(this.buttonP, BorderLayout.SOUTH);

		this.running  = true;
		(new Thread(this)).start();
	}


	
	
	/*
	 * The run function of the runnable implementation (Engine)
	 */
	@Override
	public void run()
	{
		while (running == true)
		{
			updatePar();
			updatePanel();
		}
	}
	
	
	
	
	/*
	 * Draws all the panels
	 */
	private void updatePanel()
	{
		this.graphP.repaint();
		this.indicatorP.repaint();
		this.buttonP.repaint();
	}
	
	
	
	
	/*
	 * Reads value from panels and updates other panels
	 */
	private void updatePar()
	{
		if (this.buttonP.getAction() == true)
		{
			boolean[] ledArr = this.buttonP.getLedArr();
			
			int index = 0;
			while (ledArr[index] != true)
			{
				index++;
			}
			
			String message = "";
			
			switch (index)
			{
			case 0: this.indicatorP.setLedState(0, ON1);
					this.indicatorP.setLedState(1, ON1);
					this.indicatorP.setLedState(2, OFF);
					this.indicatorP.setLedState(4, ON2);
					this.indicatorP.setLedState(6, OFF);
					
					message = message + ID_START;
					break;
					
			case 1: this.indicatorP.setLedState(0, ON2);
					this.indicatorP.setLedState(1, OFF);
					this.indicatorP.setLedState(2, ON2);
					this.indicatorP.setLedState(4, OFF);
					this.indicatorP.setLedState(6, OFF);
					
					message = message + ID_STOP;
					break;
					
			case 4: this.indicatorP.setLedState(0, OFF);
					this.indicatorP.setLedState(1, OFF);
					this.indicatorP.setLedState(2, ON2);
					this.indicatorP.setLedState(4, OFF);
					this.indicatorP.setLedState(6, ON2);
					
					//editCode
					break;
			}
			
			
			if (ledArr[2] == true)
			{
				double g = this.buttonP.getTextFieldVal();
				if (g != -1)
				{
					this.graphP.setDesG(g);
					this.graphP.setDesRpm(-1);
					
					message = message + ID_G_FORCE + "[" + Double.toString(g) + "]";
				}
			}
			else
			{
				if (ledArr[3] == true)
				{
					int rpm = (int)this.buttonP.getTextFieldVal();
					if (rpm != -1)
					{
						this.graphP.setDesRpm(rpm);
						this.graphP.setDesG(-1);
						
						message = message + ID_RPM + "[" + Integer.toString(rpm) + "]";
					}
				}
			}
			
			if (message.equals("") == false)
			{
				message = message + "s";
				
				this.link.sendCustomMessage(message);
			}
			
			
		}
		
		
		if (this.graphP.getConnectedB() == false)
		{
			this.buttonP.setButtTextEna();
		}
	}


	/*
	private void paintScreen()
	{
		this.graphP.paintScreen();
		this.indicatorP.paintScreen();
		this.buttonP.paintScreen();
	}
	 */
}
