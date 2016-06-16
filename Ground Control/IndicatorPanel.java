import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.AttributedString;

import javax.swing.JButton;
import javax.swing.JPanel;

public class IndicatorPanel extends JPanel
{
	private static final short OFF = 0, ON1 = 1, ON2 = 2, ERROR = -1;
	private static final short WIDTH = 415, HEIGHT = 504;
	private static final short LED_STA_X = 350, LED_STA_Y = 80, LED_DIAMETER = 40, INTERVALS = 20;
	private static final Font TITLE_F = new Font("Arial", Font.BOLD, 28), LED_F = new Font("Arial", Font.PLAIN, 24);

	private static final String [] LED_STRINGS = {"Motor On", "Starting", "Stopping", "Accelerating/Decelerating",
			"Stabilizing", "RPM 0", "Editing Code"};
	private static final int NUM_OF_LEDS = LED_STRINGS.length;
	
	private static final short [] DRAW_TI_CORREC_VAL = {100, 40, 100, 42, 172, 3};

	private int [] ledArr;

	int tmp = 0;

	
	/*
	 * IndicatorPanel Constructor
	 */
	public IndicatorPanel()
	{
		initializeIndicatorP();

		initializeIndicatorVar();
	}


	/*
	 * Initializes the Jpanel's parameters
	 */
	private void initializeIndicatorP()
	{
		setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(this.getX(), this.getY()));
	}
	
	
	/*
	 * Initialized the IndicatorPanel's local parameters
	 */
	private void initializeIndicatorVar()
	{
		this.ledArr = new int[NUM_OF_LEDS];
		ledsOff();
	}

	
	/*
	 * Turns OFF (0) all led indicators
	 */
	private void ledsOff()
	{
		for (int i = 0; i < NUM_OF_LEDS; i++)
		{
			this.ledArr [i] = OFF;
		}
	}
	
	
	/*
	 * Return the state of an led, in case of wrong input returns ERROR (-1)
	 * INPUT: ledIndex - the index of the led
	 */
	public int getLedState(int ledIndex)
	{
		if (ledIndex >= 0 && ledIndex < NUM_OF_LEDS)
		{
			return this.ledArr[ledIndex];
		}
		
		return ERROR;
	}
	
	
	/*
	 * Sets an led to a state (OFF, ON1, ON2) and returns true, in case of wrong input returns false
	 * INPUT: ledIndex - the index of the led, newState - the new state of the led 
	 */
	public boolean setLedState(int ledIndex, int newState)
	{
		if (newState >= 0 && newState < 3 && ledIndex >= 0 && ledIndex < NUM_OF_LEDS)
		{
			this.ledArr[ledIndex] = newState;
			return true;
		}
		
		return false;
	}


	/*
	 * Draws the indicators and the other components on the panel
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		drawTitle(g2d);
		drawLeds(g2d);
		drawLedStrings(g2d);
		drawBorder(g2d);
	}
	
	
	/*
	 * Draws the title "INDICATORS"
	 * INPUT: g2d - Graphics2D
	 * DRAW_TI_CORREC_VAL = {100, 40, 100, 42, 172, 3}
	 */
	public void drawTitle(Graphics2D g2d)
	{
		g2d.setFont(TITLE_F);
		g2d.drawString("INDICATORS", DRAW_TI_CORREC_VAL[0], DRAW_TI_CORREC_VAL[1]);
		g2d.fillRect(DRAW_TI_CORREC_VAL[2], DRAW_TI_CORREC_VAL[3], DRAW_TI_CORREC_VAL[4], DRAW_TI_CORREC_VAL[5]);
	}
	
	
	/*
	 * Draws the led indicators
	 * INPUT: g2d - Graphics2D
	 */
	public void drawLeds(Graphics2D g2d)
	{
		for (int i = 0; i < NUM_OF_LEDS; i++)
		{
			switch (this.ledArr[i])
			{
			case OFF: g2d.setColor(Color.LIGHT_GRAY); break;
			case ON1: g2d.setColor(Color.BLUE); break;
			case ON2: g2d.setColor(Color.RED); break;
			}
			
			int tmpCalc = (LED_DIAMETER + INTERVALS) * i;
			
			g2d.fillOval(LED_STA_X, LED_STA_Y + tmpCalc, LED_DIAMETER, LED_DIAMETER);
			
			g2d.setColor(Color.BLACK);
			
			g2d.drawOval(LED_STA_X, LED_STA_Y + tmpCalc, LED_DIAMETER, LED_DIAMETER);
		}
	}
	
	
	/*
	 * Draws the led strings
	 * INPUT: g2d - Graphics2D
	 */
	public void drawLedStrings(Graphics2D g2d)
	{
		g2d.setFont(LED_F);
		
		int tmpCalc;
		for (int i = 0; i < NUM_OF_LEDS; i++)
		{
			tmpCalc = (LED_DIAMETER + INTERVALS) * i;
			g2d.drawString(LED_STRINGS[i], 10, LED_STA_Y + tmpCalc + 25);
			
			g2d.setColor(Color.DARK_GRAY);
			
			g2d.drawLine(5, LED_STA_Y + tmpCalc - 10, 400, LED_STA_Y + tmpCalc - 10);
			
			g2d.setColor(Color.BLACK);
		}
	}
	
	
	/*
	 * Draws the borders of the panel
	 * INPUT: g2d - Graphics2D
	 */
	public void drawBorder(Graphics2D g2d)
	{
		g2d.setColor(Color.DARK_GRAY);
		
		g2d.drawLine(WIDTH, 0, WIDTH, HEIGHT);
		
		g2d.drawLine(0, HEIGHT, WIDTH, HEIGHT);
		
		g2d.setColor(Color.BLACK);
	}
}
