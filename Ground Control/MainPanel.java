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

	public MainPanel()
	{
		creatPanels();
		initialize();
	}

	public void creatPanels()
	{
		this.graphP = new GraphPanel();
		this.indicatorP = new IndicatorPanel();
		this.buttonP = new ButtonPanel();
	}

	public void initialize()
	{
		this.setLayout(new BorderLayout());
		add(this.graphP, BorderLayout.EAST);
		add(this.indicatorP, BorderLayout.CENTER);
		add(this.buttonP, BorderLayout.SOUTH);

		//setBackground(Color.BLUE);

		this.running  = true;
		(new Thread(this)).start();
	}


	@Override
	public void run()
	{
		while (running == true)
		{
			updatePar();
			updatePanel();
		}
	}
	
	
	private void updatePanel()
	{
		this.graphP.repaint();
		this.indicatorP.repaint();
		this.buttonP.repaint();
	}
	
	
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
