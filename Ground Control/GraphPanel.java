import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GraphPanel extends JPanel
{
	private static final int NUM_OF_AXIS_P = 15, NUM_OF_TOTAL_P = 20;
	
	private static final int G_X_AXIS = 250, G_RPM_Y_AXIS = 50, G_RPM_Y_LENGTH = 300;
	private static final int TIME_Y_AXIS = G_RPM_Y_AXIS + G_RPM_Y_LENGTH, TIME_X_LENGTH = 630;
	private static final int RPM_X_AXIS = G_X_AXIS + TIME_X_LENGTH;
	private static final int G_RPM_SPACER = G_RPM_Y_LENGTH / NUM_OF_AXIS_P, 
								TIME_SPACER = (TIME_X_LENGTH - 30) / NUM_OF_TOTAL_P,
								SPACER = 10;
	private static final int RESIDUE = 30;
	
	private double gForceDes, currentGForce;
	private int rpmDes, currentRpm;
	
	private double minGForceRange, maxGForceRange;
	private int minRpmRange, maxRpmRange;
	
	private class GraphPoint
	{
		public double gForce;
		public int rpm;
		public long time;
		
		public GraphPoint(double gForce, int rpm, long time)
		{
			this.gForce = gForce;
			this.rpm = rpm;
			this.time = time;
		}
		
		public void set(double gForce, int rpm, long time)
		{
			this.gForce = gForce;
			this.rpm = rpm;
			this.time = time;
		}
		
		public double getGForce()
		{
			return this.gForce;
		}
		
		public int getRpm()
		{
			return this.rpm;
		}
		
		public long getTime()
		{
			return this.time;
		}
		
		public void set(GraphPoint copy)
		{
			set(copy.getGForce(), copy.getRpm(), copy.getTime());
		}
	}
	
	private GraphPoint [] forGraph, tmpMem, chunk;
	private int forGIndex, tmpMemIndex, chunkIndex;		//The index of the oldest cell (first in)
	
	int tmp = 0;
	
	public GraphPanel()
	{
		initializeGraphP();
		initializeGraphVar();
		//repaint();
	}
	
	
	private void initializeGraphP()
	{
		this.setPreferredSize(new Dimension(950, this.getY()));
		this.setBackground(Color.WHITE);
	}
	
	
	private void initializeGraphVar()
	{
		this.currentGForce = 0;
		this.currentRpm = 0;
		
		this.gForceDes = 1;
		this.rpmDes = -1;
		
		this.minGForceRange = 0;
		this.maxGForceRange = 1.5;
		
		this.minRpmRange = 0;
		this.maxRpmRange = 240;
		
		this.forGraph = new GraphPoint[NUM_OF_TOTAL_P];
		this.tmpMem = new GraphPoint[NUM_OF_TOTAL_P];
		this.chunk = new GraphPoint[NUM_OF_TOTAL_P * 5];
		
		initGraphPointArr();
	}
	
	
	private void initGraphPointArr()
	{
		initForGraphArr();
		initTmpMemArr();
		initChunkArr();
	}
	
	
	private void initForGraphArr()
	{
		this.forGIndex = 0;
		
		for (int i = 0; i < this.forGraph.length; i++)
		{
			this.forGraph[i] = new GraphPoint(0, 0, 0);
		}
	}
	
	private void initTmpMemArr()
	{
		this.tmpMemIndex = 0;
		
		for (int i = 0; i < this.tmpMem.length; i++)
		{
			this.tmpMem[i] = new GraphPoint(0, 0, 0);
		}
	}
	
	private void initChunkArr()
	{
		this.chunkIndex = 0;
		
		for (int i = 0; i < this.chunk.length; i++)
		{
			this.chunk[i] = new GraphPoint(0, 0, 0);
		}
	}
	
	
	//call from event trigger
	private void generateGraphPoint(double gForce, int rpm, long time)
	{
		this.forGraph[this.forGIndex].set(gForce, rpm, time);
		
		this.forGIndex++;
		
		if (this.forGIndex == NUM_OF_TOTAL_P)
		{
			this.forGIndex = 0;
		}
	}
	
	
	//call from engine in MainPanel
	private void generateBackUpGraphPoint(GraphPoint newGP)
	{
		this.tmpMem[this.tmpMemIndex].set(newGP);
		
		this.tmpMemIndex++;
		
		if (this.tmpMemIndex == NUM_OF_TOTAL_P)
		{
			copyForTmpToChunk();
			
			if (this.chunkIndex == (NUM_OF_TOTAL_P * 5))
			{
				copyChunkToFile();
			}
		}
		
		if (this.forGIndex > this.tmpMemIndex)
		{
			while (this.tmpMemIndex < this.forGIndex)
			{
				this.tmpMem[this.tmpMemIndex].set(this.forGraph[this.tmpMemIndex]);
				this.tmpMemIndex++;
			}
		}
	}
	
	
	
	private void copyForTmpToChunk()
	{
		for (int index = 0; index < this.tmpMem.length; index++)
		{
			this.chunk[this.chunkIndex].set(this.tmpMem[index]);
			this.chunkIndex++;
		}
		
		this.tmpMemIndex = 0;
	}
	
	
	private void copyChunkToFile()
	{
		//COPY TO FILE
		
		this.chunkIndex = 0;
	}
	
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		
		g2d.drawLine(G_X_AXIS, TIME_Y_AXIS, RPM_X_AXIS, TIME_Y_AXIS);
		
		g2d.setColor(Color.RED);
		g2d.drawLine(G_X_AXIS, G_RPM_Y_AXIS, G_X_AXIS, TIME_Y_AXIS);
		g2d.drawLine(G_X_AXIS, G_RPM_Y_AXIS, G_X_AXIS - RESIDUE, G_RPM_Y_AXIS);
		g2d.drawLine(G_X_AXIS, TIME_Y_AXIS, G_X_AXIS - RESIDUE, TIME_Y_AXIS);
		
		g2d.setColor(Color.BLUE);
		g2d.drawLine(RPM_X_AXIS, G_RPM_Y_AXIS, RPM_X_AXIS, TIME_Y_AXIS);
		g2d.drawLine(RPM_X_AXIS, G_RPM_Y_AXIS, RPM_X_AXIS + RESIDUE, G_RPM_Y_AXIS);
		g2d.drawLine(RPM_X_AXIS, TIME_Y_AXIS, RPM_X_AXIS + RESIDUE, TIME_Y_AXIS);
		
		for (int counter = 1; counter <= NUM_OF_AXIS_P - 1; counter++)
		{
			int tmpY = TIME_Y_AXIS - (counter * G_RPM_SPACER);
			
			g2d.setColor(Color.RED);
			g2d.drawLine(G_X_AXIS - SPACER, tmpY, G_X_AXIS, tmpY);
			
			g2d.setColor(Color.BLUE);
			g2d.drawLine(RPM_X_AXIS, tmpY, RPM_X_AXIS + SPACER, tmpY);
		}
		
		
		g2d.setColor(Color.BLACK);
		for (int counter = 1; counter <= NUM_OF_TOTAL_P; counter++)
		{
			int tmpX = G_X_AXIS + (counter * TIME_SPACER);
			g2d.drawLine(tmpX, TIME_Y_AXIS, tmpX, TIME_Y_AXIS + SPACER);
		}
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
