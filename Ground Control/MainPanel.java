import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable
{
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
			this.graphP.repaint();
			this.indicatorP.repaint();
			this.buttonP.repaint();
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
