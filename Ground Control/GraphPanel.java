import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GraphPanel extends JPanel
{
	private static final short NUM_OF_AXIS_P = 15, NUM_OF_TOTAL_P = 20;
	
	private static final short G_X_AXIS = 250, G_RPM_Y_AXIS = 50, G_RPM_Y_LENGTH = 300;
	private static final short TIME_Y_AXIS = G_RPM_Y_AXIS + G_RPM_Y_LENGTH, TIME_X_LENGTH = 630;
	private static final short RPM_X_AXIS = G_X_AXIS + TIME_X_LENGTH;
	private static final short G_RPM_SPACER = G_RPM_Y_LENGTH / NUM_OF_AXIS_P, 
								TIME_SPACER = (TIME_X_LENGTH - 30) / NUM_OF_TOTAL_P,
								SPACER = 10;
	private static final short RESIDUE = 30;
	
	private static final Font AXIS_TITLE_F = new Font("Arial", Font.PLAIN, 22),
							BIG_TITLE_F = new Font("Arial", Font.PLAIN, 24),
							SMALL_TITLE_F = new Font("Arial", Font.PLAIN, 18),
							SMALL_TITLE_B_F = new Font("Arial", Font.BOLD, 18);
	private static final short TEXT_F_WIDTH = 80, TEXT_F_HEIGHT = 40;
	private static final short NUM_TEXT_FIELDS = 4;
	
	private JButton confirm;
	
	private JTextField gForceS, gForceE, rpmS, rpmE;
	
	private double gForceDes, currentGForce;
	private int rpmDes, currentRpm;
	
	private double minGForceRange, maxGForceRange;
	private int minRpmRange, maxRpmRange;
	
	private boolean [] textFieldsArr;
	private double [] newVal;
	
	
	/*
	 * GraphPoint - represents a point on the graph
	 */
	private class GraphPoint
	{
		public double gForce;
		public int rpm;
		public long time;
		
		/*
		 * GraphPoint constructor
		 */
		public GraphPoint(double gForce, int rpm, long time)
		{
			this.gForce = gForce;
			this.rpm = rpm;
			this.time = time;
		}
		
		
		/*
		 * Sets the GraphPoint parameters
		 * INPUS: gForce - new g force, rpm - new rpm, time - new time
		 */
		public void set(double gForce, int rpm, long time)
		{
			this.gForce = gForce;
			this.rpm = rpm;
			this.time = time;
		}
		
		
		/*
		 * Returns the gForce
		 */
		public double getGForce()
		{
			return this.gForce;
		}
		
		
		/*
		 * Returns the rpm
		 */
		public int getRpm()
		{
			return this.rpm;
		}
		
		
		/*
		 * Returns the rpm
		 */
		public long getTime()
		{
			return this.time;
		}
		
		
		/*
		 * Sets the GraphPoint parameters
		 * INPUT: copy - a new GraphPoint (gets its parameters)
		 */
		public void set(GraphPoint copy)
		{
			set(copy.getGForce(), copy.getRpm(), copy.getTime());
		}
	}
	
	private GraphPoint [] forGraph, tmpMem, chunk;
	private int forGIndex, tmpMemIndex, chunkIndex;		//The index of the oldest cell (first in)
	
	int tmp = 0;
	
	
	/*
	 * GraphPanel constructor
	 */
	public GraphPanel()
	{
		initializeGraphP();
		initializeGraphVar();
		//repaint();
	}
	
	
	
	/*
	 * Initializes the Jpanel's parameters
	 */
	private void initializeGraphP()
	{
		this.setPreferredSize(new Dimension(950, this.getY()));
		this.setBackground(Color.WHITE);
		this.setLayout(null);
	}
	
	
	
	/*
	 * Initialized the IndicatorPanel's local parameters
	 */
	private void initializeGraphVar()
	{
this.confirm = new JButton("Confirm");
		
		this.gForceS = new JTextField("min");
		this.gForceE = new JTextField("max");
		
		this.rpmS = new JTextField("min");
		this.rpmE = new JTextField("max");
		
		
		this.add(this.confirm);
		
		this.add(this.gForceS);
		this.add(this.gForceE);
		
		this.add(this.rpmS);
		this.add(this.rpmE);
		
		
		
		this.gForceS.setBounds(350, 455, TEXT_F_WIDTH, TEXT_F_HEIGHT);
		this.gForceE.setBounds(350 + TEXT_F_WIDTH + 50, 455, TEXT_F_WIDTH, TEXT_F_HEIGHT);
		
		int tmpCalc = (2 * TEXT_F_WIDTH) + 50 + 150;
		
		this.rpmS.setBounds(350 + tmpCalc, 455, TEXT_F_WIDTH, TEXT_F_HEIGHT);
		
		tmpCalc = tmpCalc + TEXT_F_WIDTH + 50;
		this.rpmE.setBounds(350 + tmpCalc, 455, TEXT_F_WIDTH, TEXT_F_HEIGHT);
		
		
		this.confirm.setBounds(350 + tmpCalc, 410, TEXT_F_WIDTH, TEXT_F_HEIGHT);
		
		
		this.textFieldsArr = new boolean[NUM_TEXT_FIELDS];
		this.newVal = new double[NUM_TEXT_FIELDS];
		
		
		
		Listeners();
		
		
		
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
	
	
	
	private void Listeners()
	{
		buttonListener();
		textFieldListener();
	}
	
	
	
	
	private void buttonListener()
	{
		this.confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String tmpStr = "";
				
				for (int i = 0; i < NUM_TEXT_FIELDS; i++)
				{
					if (textFieldsArr[i] == true)
					{
						switch (i)
						{
						case 0: tmpStr = gForceS.getText(); break;
						case 1: tmpStr = gForceE.getText(); break;
						case 2: tmpStr = rpmS.getText(); break;
						case 3: tmpStr = rpmE.getText(); break;
						}
						
						newVal[i] = checkTextFieldVal(tmpStr);
					}
				}
				
				
				
				for (int i = 0; i < NUM_TEXT_FIELDS; i++)
				{
					if (newVal[i] != -1)
					{
						switch (i)
						{
						case 0: if ((newVal[0] < newVal[1] && textFieldsArr[1] == true) || newVal[0] < maxGForceRange)
								{
									minGForceRange = newVal[0];
								}
								break;
								
						case 1: if ((newVal[1] > newVal[0] && textFieldsArr[0] == true) || newVal[1] > minGForceRange)
								{
									maxGForceRange = newVal[1];
								}
								break;
								
						case 2: if ((newVal[2] < newVal[3] && textFieldsArr[3] == true) || newVal[2] < maxRpmRange)
								{
									minRpmRange = (int)newVal[2];
								}
								break;
								
						case 3: if ((newVal[3] > newVal[2] && textFieldsArr[2] == true) || newVal[3] > minRpmRange)
								{
									maxRpmRange = (int)newVal[3];
								}
								break;
						}
					}
				}
				
				repaint();
			}
		});
	}
	
	
	
	private void textFieldListener()
	{
		this.gForceS.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (gForceS.getText().equals("") == false)
				{
					textFieldsArr[0] = true;
				}
				else
				{
					gForceS.setText("min");
				}

				//repaint();
			}

			@Override
			public void focusGained(FocusEvent arg0)
			{
				gForceS.setText("");
			}
		});
		
		
		this.gForceE.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (gForceE.getText().equals("") == false)
				{
					textFieldsArr[1] = true;
				}
				else
				{
					gForceE.setText("max");
				}

				//repaint();
			}

			@Override
			public void focusGained(FocusEvent arg0)
			{
				gForceE.setText("");
			}
		});
		
		
		this.rpmS.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (rpmS.getText().equals("") == false)
				{
					textFieldsArr[2] = true;
				}
				else
				{
					rpmS.setText("min");
				}

				//repaint();
			}

			@Override
			public void focusGained(FocusEvent arg0)
			{
				rpmS.setText("");
			}
		});
		
		
		
		this.rpmE.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (rpmE.getText().equals("") == false)
				{
					textFieldsArr[3] = true;
				}
				else
				{
					rpmE.setText("max");
				}

				//repaint();
			}

			@Override
			public void focusGained(FocusEvent arg0)
			{
				rpmE.setText("");
			}
		});
	}
	
	
	
	/*
	 * Converts a String (gForce and rpm string values) to a double value, and returns it
	 * This function converts only up to the second digit after the '.'
	 * INPUT: TextFieldV - the string to convert to a double
	 */
	private double checkTextFieldVal(String TextFieldV)
	{
		double stringToD = 0;
		char tmp;
		int counter = 1;
		boolean pAppeared = false;
		
		for (int i = 0; i < TextFieldV.length(); i++)
		{
			tmp = TextFieldV.charAt(i);
			
			if ((tmp < '0' || tmp > '9') && tmp != '.')
			{
				return -1;
			}
			else
			{
				if (tmp != '.')
				{
					if (pAppeared == false)
					{
						stringToD = (stringToD * 10) + Character.getNumericValue(tmp);
					}
					else
					{
						if (tmp != '0' && counter < 3)
						{
							stringToD = stringToD + ((double)(Character.getNumericValue(tmp)) / (Math.pow(10, counter)));
						}
						
						counter++;
					}
				}
				else
				{
					if (pAppeared == true)
					{
						return -1;
					}
					
					pAppeared = true;
				}
			}
		}
		
		return stringToD;
	}
	
	
	
	
	
	/*
	 * Sets the g force destination (goal) value
	 * INPUT: newG - the new g force goal value
	 */
	public void setDesG(double newG)
	{
		this.gForceDes = newG;
	}
	
	
	/*
	 * Sets the rpm destination (goal) value
	 * INPUT: newRpm - the new rpm goal value
	 */
	public void setDesRpm(int newRpm)
	{
		this.rpmDes = newRpm;
	}
	
	
	
	/*
	 * Initializes the GraphPoint storing arrays
	 * Array Purpose:
	 * forGraph is for displaying the points of the graph
	 * tmpMem, temporarily stores GraphPoints. When full, copies the GraphPoints to chunkArr
	 * chunk, when full, copies the GraphPoints to a file
	 */
	private void initGraphPointArr()
	{
		initForGraphArr();
		initTmpMemArr();
		initChunkArr();
	}
	
	
	//Initializes forGraphArr array
	private void initForGraphArr()
	{
		this.forGIndex = 0;
		
		for (int i = 0; i < this.forGraph.length; i++)
		{
			this.forGraph[i] = new GraphPoint(0, 0, 0);
		}
	}
	
	
	
	/*
	 * Initializes tmpMemArr array
	 */
	private void initTmpMemArr()
	{
		this.tmpMemIndex = 0;
		
		for (int i = 0; i < this.tmpMem.length; i++)
		{
			this.tmpMem[i] = new GraphPoint(0, 0, 0);
		}
	}
	
	
	
	/*
	 * initializes chunkArr array
	 */
	private void initChunkArr()
	{
		this.chunkIndex = 0;
		
		for (int i = 0; i < this.chunk.length; i++)
		{
			this.chunk[i] = new GraphPoint(0, 0, 0);
		}
	}
	
	
	
	
	//call from event trigger
	/*
	 * Sets a new GraphPoint, the new GraphPoint is stores in the forGraph array 
	 * This function is called from the trigger generated by the microcontroller
	 * INPUT: gForce - new g force, rpm - new rpm, time - new time 
	 */
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
	/*
	 * Sets a new GraphPoint for backUp, to save to a file
	 * INPUT: newGP - a new GraphPoint
	 */
	private void generateBackUpGraphPoint(GraphPoint newGP)
	{
		this.tmpMem[this.tmpMemIndex].set(newGP);
		
		this.tmpMemIndex++;
		
		if (this.tmpMemIndex == NUM_OF_TOTAL_P)
		{
			copyFromTmpToChunk();
			
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
	
	
	
	
	/*
	 * Copies the GraphPoints from the themMem array to the chunk array
	 */
	private void copyFromTmpToChunk()
	{
		for (int index = 0; index < this.tmpMem.length; index++)
		{
			this.chunk[this.chunkIndex].set(this.tmpMem[index]);
			this.chunkIndex++;
		}
		
		this.tmpMemIndex = 0;
	}
	
	
	
	
	/*
	 * Copies GraphPoints from chunk array to a file
	 */
	private void copyChunkToFile()
	{
		//COPY TO FILE
		
		this.chunkIndex = 0;
	}
	
	
	
	
	/*
	 * Draws the whole graph, current value, destination (goal) values, separation lines, border and other
	 * components on the panel 
	 */
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
		
		drawSetRangeTitles(g2d);
		
		drawGraph(g2d);
	}
	
	
	/*
	 * Draws the Graph
	 */
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
	
	
	
	
	/*
	 * Draws the axis titles
	 */
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
	
	
	
	
	
	/*
	 * Draws the axis values
	 */
	private void drawAxisVal(Graphics2D g2d)
	{
		int tmp = 0;
		
		double middleG = minGForceRange + (this.maxGForceRange - this.minGForceRange) / 2;
		
		tmp = (int)(middleG * 100);
		middleG = ((double)tmp) / 100;
		
		int middleRPM = minRpmRange + ((int) ((this.maxRpmRange - this.minRpmRange) / 2));
		
		double tmpCalc;
		int gPoss;
		
		tmpCalc = (middleG - this.minGForceRange) / (this.maxGForceRange - this.minGForceRange);
		gPoss  = TIME_Y_AXIS - G_RPM_Y_AXIS + 5;
		gPoss = gPoss - (int)(gPoss * tmpCalc) + G_RPM_Y_AXIS;
		
		//int tmpY = TIME_Y_AXIS - (7 * G_RPM_SPACER);
		
		g2d.setFont(SMALL_TITLE_F);
		
		g2d.setColor(Color.RED);
		g2d.drawString(Double.toString(this.maxGForceRange), 190, 55);
		
		if (middleG != this.gForceDes && middleG > minGForceRange && middleG < maxGForceRange)
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
		
		
		
		tmpCalc = ((double)middleRPM - (double)this.minRpmRange) / ((double)this.maxRpmRange - (double)this.minRpmRange);
		gPoss  = TIME_Y_AXIS - G_RPM_Y_AXIS + 5;
		gPoss = gPoss - (int)(gPoss * tmpCalc) + G_RPM_Y_AXIS;
		
		g2d.setFont(SMALL_TITLE_F);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString(Integer.toString(this.maxRpmRange), 915, 55);
		
		
		if (middleRPM != this.rpmDes && middleRPM > minRpmRange && middleRPM < maxRpmRange)
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
	
	
	
	
	/*
	 * Draws the current measurement
	 */
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
	
	
	
	/*
	 * Draws the destination (goal) values
	 */
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
	
	
	
	
	private void drawSetRangeTitles(Graphics2D g2d)
	{
		g2d.setFont(SMALL_TITLE_F);
		g2d.setColor(Color.RED);
		g2d.drawString("g force from:", 240, 480);
		g2d.drawString("to:", 240 + (2 * TEXT_F_WIDTH) + 40, 480);
		
		g2d.setColor(Color.BLUE);
		g2d.drawString("rpm from:", 620, 480);
		g2d.drawString("to:", 620 + (2 * TEXT_F_WIDTH) + 20, 480);
		
		
		g2d.setFont(AXIS_TITLE_F);
		g2d.setColor(Color.BLACK);
		
		g2d.drawString("SET RANGE", 510, 430);
		g2d.fillRect(510, 432, 129, 2);
	}
	
	
	
	
	/*
	 * Draws separation lines
	 */
	private void drawLine(Graphics2D g2d)
	{
		g2d.setColor(Color.DARK_GRAY);
		
		g2d.drawLine(150, 10, 150, 403);
		g2d.drawLine(10, 403, 940, 403);
	}
	
	
	
	/*
	 * Draws the border of the panel
	 */
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
