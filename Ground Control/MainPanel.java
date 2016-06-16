import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable
{
	private static final short OFF = 0, ON1 = 1, ON2 = 2, ERROR = -1;
	
	private GraphPanel graphP;
	private IndicatorPanel indicatorP;
	private ButtonPanel buttonP;

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
		this.graphP = new GraphPanel();
		this.indicatorP = new IndicatorPanel();
		this.buttonP = new ButtonPanel();
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
		add(this.graphP, BorderLayout.EAST);
		add(this.indicatorP, BorderLayout.CENTER);
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
			
			switch (index)
			{
			case 0: this.indicatorP.setLedState(0, ON1);
					this.indicatorP.setLedState(1, ON1);
					this.indicatorP.setLedState(2, OFF);
					this.indicatorP.setLedState(4, ON2);
					this.indicatorP.setLedState(6, OFF);
					break;
					
			case 1: this.indicatorP.setLedState(0, ON2);
					this.indicatorP.setLedState(1, OFF);
					this.indicatorP.setLedState(2, ON2);
					this.indicatorP.setLedState(4, OFF);
					this.indicatorP.setLedState(6, OFF);
					break;
					
			case 4: this.indicatorP.setLedState(0, OFF);
					this.indicatorP.setLedState(1, OFF);
					this.indicatorP.setLedState(2, ON2);
					this.indicatorP.setLedState(4, OFF);
					this.indicatorP.setLedState(6, ON2);
					break;
			}
			
			
			if (ledArr[2] == true)
			{
				double g = this.buttonP.getTextFieldVal();
				if (g != -1)
				{
					this.graphP.setDesG(g);
					this.graphP.setDesRpm(-1);
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
					}
				}
			}
			
			
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
