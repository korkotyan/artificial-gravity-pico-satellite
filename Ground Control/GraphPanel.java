import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	
	private static final Font AXIS_TITLE_F = new Font("Arial", Font.PLAIN, 22),
							BIG_TITLE_F = new Font("Arial", Font.PLAIN, 24),
							SMALL_TITLE_F = new Font("Arial", Font.PLAIN, 18),
							SMALL_TITLE_B_F = new Font("Arial", Font.BOLD, 18);
	
	private JLabel currGForceL, currRPML, gForceDesL, rpmDesL;
	
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
		this.setLayout(null);
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
		
		/*
		this.currGForceL = new JLabel(Double.toString(this.currentGForce));
		this.add(this.currGForceL);
		this.currGForceL.setBounds(80, 150, 50, 40);
		*/
	}
	
	
	public void setDesG(double newG)
	{
		this.gForceDes = newG;
	}
	
	public void setDesRpm(int newRpm)
	{
		this.rpmDes = newRpm;
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
		
		
		drawLine(g2d);
		drawBorder(g2d);
		
		drawAxisVal(g2d);
		
		drawAxisTitle(g2d);
		
		drawCurrentMeas(g2d);
		drawDesVal(g2d);
		
		drawGraph(g2d);
	}
	
	
	
	private void drawGraph(Graphics2D g2d)
	{
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
	
	
	
	private void drawAxisTitle(Graphics2D g2d)
	{
		g2d.setFont(AXIS_TITLE_F);
		
		g2d.drawString("time (sec)", 500, 390);
		
		g2d.setColor(Color.RED);
		g2d.drawString("g Force (g * 9.81)", 160, 30);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString("rpm", 875, 30);
		
		g2d.setColor(Color.BLACK);
	}
	
	//-------------
	private void drawAxisVal(Graphics2D g2d)
	{
		double middleG = (this.maxGForceRange - this.minGForceRange) / 2;
		int middleRPM = (int) ((this.maxRpmRange - this.minRpmRange) / 2);
		
		double tmpCalc;
		int gPoss;
		
		tmpCalc = (middleG - this.minGForceRange) / (this.maxGForceRange - this.minGForceRange);
		gPoss  = TIME_Y_AXIS - G_RPM_Y_AXIS + 5;
		gPoss = gPoss - (int)(gPoss * tmpCalc) + G_RPM_Y_AXIS;
		
		//int tmpY = TIME_Y_AXIS - (7 * G_RPM_SPACER);
		
		g2d.setFont(SMALL_TITLE_F);
		
		g2d.setColor(Color.RED);
		g2d.drawString(Double.toString(this.maxGForceRange), 190, 55);
		
		if (middleG != this.gForceDes)
		{
			g2d.drawString(Double.toString(middleG), 200, gPoss);
		}
		
		g2d.drawString(Double.toString(this.minGForceRange), 190, 355);
		
		if (this.gForceDes != -1 && this.gForceDes > this.minGForceRange && this.gForceDes < this.maxGForceRange)
		{
			tmpCalc = (this.gForceDes - this.minGForceRange) / (this.maxGForceRange - this.minGForceRange);
			gPoss  = TIME_Y_AXIS - G_RPM_Y_AXIS;
			gPoss = gPoss - (int)(gPoss * tmpCalc) + G_RPM_Y_AXIS;
			
			g2d.fillRect(G_X_AXIS - SPACER, gPoss - 1, 2 * SPACER, 3);
			
			g2d.setFont(SMALL_TITLE_B_F);
			gPoss = gPoss + 5;
			g2d.drawString(Double.toString(this.gForceDes), 200, gPoss);
		}
		
		
		
		tmpCalc = (middleRPM - this.minGForceRange) / (this.maxGForceRange - this.minGForceRange);
		gPoss  = TIME_Y_AXIS - G_RPM_Y_AXIS + 5;
		gPoss = gPoss - (int)(gPoss * tmpCalc) + G_RPM_Y_AXIS;
		
		g2d.setFont(SMALL_TITLE_F);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString(Integer.toString(this.maxRpmRange), 915, 55);
		
		if (middleRPM != this.rpmDes)
		{
			g2d.drawString(Integer.toString(middleRPM), 895, gPoss);
		}
		g2d.drawString(Integer.toString(this.minRpmRange), 915, 355);
		
		if (this.rpmDes != -1 && this.rpmDes > this.minRpmRange && this.rpmDes < this.maxRpmRange)
		{
			tmpCalc = (double)(this.rpmDes - this.minRpmRange) / (double)(this.maxRpmRange - this.minRpmRange);
			gPoss  = TIME_Y_AXIS - G_RPM_Y_AXIS;
			gPoss = gPoss - (int)(gPoss * tmpCalc) + G_RPM_Y_AXIS;
			
			g2d.fillRect(RPM_X_AXIS - SPACER, gPoss - 1, 2 * SPACER, 3);
			
			g2d.setFont(SMALL_TITLE_B_F);
			gPoss = gPoss + 5;
			g2d.drawString(Integer.toString(this.rpmDes), 895, gPoss);
		}
		
		
	}
	
	
	
	private void drawCurrentMeas(Graphics2D g2d)
	{
		g2d.setFont(BIG_TITLE_F);
		g2d.drawString("Current", 30, 100);
		g2d.fillRect(30, 102, 82, 2);
		
		
		g2d.setFont(SMALL_TITLE_F);
		
		g2d.setColor(Color.RED);
		g2d.drawString("g force:    " + Double.toString(this.currentGForce), 10, 150);
		g2d.drawRect(80, 130, 52, 25);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString("rpm:           " + Integer.toString(this.currentRpm), 10, 180);
		g2d.drawRect(80, 160, 52, 25);
		
		g2d.setColor(Color.BLACK);
	}
	
	
	
	private void drawDesVal(Graphics2D g2d)
	{
		g2d.setFont(BIG_TITLE_F);
		
		g2d.drawString("Destination", 13, 270);
		g2d.fillRect(14, 272, 118, 2);
		
		
		
		g2d.setFont(SMALL_TITLE_F);
		
		String tmpG = "-", tmpRPM = "-";
		
		if (this.gForceDes != -1)
		{
			tmpG = Double.toString(this.gForceDes);
		}
		
		if (this.rpmDes != -1)
		{
			tmpRPM = Integer.toString(this.rpmDes);
		}
		
		g2d.setColor(Color.RED);
		g2d.drawString("g force:    " + tmpG, 10, 320);
		g2d.drawRect(80, 300, 52, 25);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString("rpm:           " + tmpRPM, 10, 350);
		g2d.drawRect(80, 330, 52, 25);
		
		g2d.setColor(Color.BLACK);
	}
	
	
	
	private void drawLine(Graphics2D g2d)
	{
		g2d.setColor(Color.DARK_GRAY);
		
		g2d.drawLine(150, 10, 150, 403);
		g2d.drawLine(10, 403, 940, 403);
	}
	
	
	private void drawBorder(Graphics2D g2d)
	{
		g2d.drawLine(0, 0, 0, 504);
		g2d.drawLine(0, 504, 950, 504);
		
		
		g2d.setColor(Color.BLACK);
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
