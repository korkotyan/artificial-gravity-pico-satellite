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
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.zu.ardulink.Link;


//Action Listener focus lost - http://www.java2s.com/Code/JavaAPI/javax.swing/JTextFieldaddFocusListenerFocusListenerl.htm
public class ButtonPanel extends JPanel
{
	private static final short BUT_TEXT_STA_X_AXIS = 30, BUT_TEXT_Y_AXIS = 120, BUT_TEXT_WIDTH = 80,
								BUT_TEXT_HEIGHT = 40, BUT_TEXT_GAP = 50, BUT_TEXT_BIG_GAP = 100;
	private static final short NUM_OF_FIELDS = 6;
	private static final Font TITLE_F = new Font("Arial", Font.BOLD, 28), BUT_SEC_F = new Font("Arial", Font.PLAIN, 24);

	private JButton start, stop, confirm, editCode;
	private JTextField gForce, rpm;
	private Link link;

	//private String prevG = "", prevRpm = "";

	private boolean action;

	private boolean [] ledArr;

	
	/*
	 * ButtonPanel constructor
	 */
	public ButtonPanel(Link link)
	{
		initializeButtonP();
		initializeButtonVar(link);
	}
	
	
	
	/*
	 * Initializes the Jpanel's parameters
	 */
	private void initializeButtonP()
	{
		setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(this.getX(), 200));
		this.setLayout(null);
	}


	
	/*
	 * Initialized the IndicatorPanel's local parameters
	 */
	private void initializeButtonVar(Link link)
	{
		this.link = link;
		
		this.ledArr = new boolean[NUM_OF_FIELDS];
		ledsOff();

		this.action = false;


		this.start = new JButton("Start");
		
		this.stop = new JButton("Stop");

		
		this.gForce = new JTextField("g force");
		
		this.rpm = new JTextField("rpm");

		this.editCode = new JButton("Edit Code");

		this.confirm = new JButton("Confirm");
		
		setButtTextEna();

		this.add(this.start);
		this.add(this.stop);

		this.add(this.gForce);
		this.add(this.rpm);

		this.add(this.editCode);

		this.add(this.confirm);

		int tmpCalc = BUT_TEXT_STA_X_AXIS; //+ 2*BUT_TEXT_WIDTH + BUT_TEXT_GAP + BUT_TEXT_BIG_GAP;

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


		Listeners();
	}
	
	
	
	
	public void setButtTextEna()
	{
		boolean enabled = this.link.isConnected();
		
		this.start.setEnabled(enabled);
		
		this.stop.setEnabled(enabled);

		this.gForce.setEnabled(enabled);
		
		this.rpm.setEnabled(enabled);

		this.editCode.setEnabled(enabled);

		this.confirm.setEnabled(enabled);
	}
	


	
	/*
	 * Turns OFF (0) all led indicators of the fields
	 */
	private void ledsOff()
	{
		for (int i = 0; i < NUM_OF_FIELDS; i++)
		{
			this.ledArr[i] = false;
		}
	}
	
	
	/*
	 * Returns the status of the Action and then set it to False,
	 * If NO action was made returns false, otherwise if an action was made, Returns True
	 */
	public boolean getAction()
	{
		boolean tmpAction = this.action;
		this.action = false;
		
		return tmpAction;
	}
	
	
	
	/*
	 * Returns the array of the LED indicators of the fields
	 */
	public boolean[] getLedArr()
	{
		return this.ledArr;
	}
	
	
	
	/*
	 * Returns the converted value of the textFields (gForce OR rpm)
	 * It checks what to convert, the gForce or the rpm
	 * This function does not do the conversion itself, it calls checkTextFieldVal which does the conversion
	 */
	public double getTextFieldVal()
	{
		double converted = 0;
		
		if (this.ledArr[2] == true)
		{
			converted = checkTextFieldVal(this.gForce.getText());//Double.parseDouble(this.gForce.getText());
			
			if (converted >= 0 && converted <= 3)
			{
				return converted;
			}
		}
		else
		{
			if (this.ledArr[3] == true)
			{
				converted = checkTextFieldVal(this.rpm.getText());//Double.parseDouble(this.rpm.getText());
				
				if (converted >= 0 && converted <= 250 && (converted - ((int)converted) == 0.0))
				{
					return converted;
				}
			}
		}
		
		return -1;
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
	 * Listeners for all the buttons and textFields on the ButtonPanel
	 */
	private void Listeners()
	{
		buttonListeners();
		textFieldListeners();
	}


	
	/*
	 * Listeners for all the buttons on the ButtonPanel
	 */
	private void buttonListeners()
	{
		/*
		 * Listener for the button 'start'
		 */
		this.start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ledArr[0] = true;

				if (ledArr[1] == true)
				{
					ledArr[1] = false;
				}

				if (ledArr[4] == true)
				{
					ledArr[4] = false;
				}
				
				repaint();
			}
		});


		/*
		 * Listener for the button 'stop'
		 */
		this.stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ledArr[1] = true;

				for (int i = 0; i < NUM_OF_FIELDS - 1; i++)
				{
					if (i != 1)
					{
						if (ledArr[i] == true)
						{
							ledArr[i] = false;
						}

						if (i == 2 || i == 3)
						{
							gForce.setText("g force");
							rpm.setText("rpm");
						}
					}
				}

				repaint();
			}
		});


		
		/*
		 * Listener for the button 'editCode'
		 */
		this.editCode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ledArr[4] = true;

				for (int i = 0; i < NUM_OF_FIELDS - 2; i++)
				{
					if (ledArr[i] == true)
					{
						ledArr[i] = false;

						if (i == 2 || i == 3)
						{
							gForce.setText("g force");
							rpm.setText("rpm");
						}
					}
				}

				repaint();
			}
		});

		
		/*
		 * Listener for the button 'confirm'
		 */
		this.confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ledArr[5] = true;
				
				for (int i = 0; i < NUM_OF_FIELDS - 1; i++)
				{
					if (ledArr[i] == true)
					{
						action = true;
					}
				}
				
				repaint();
			}
		});
	}


	
	/*
	 * Listener for the textFields on the ButtonPanel
	 */
	private void textFieldListeners()
	{
		/*
		 * Listener for the textField 'gForce'
		 */
		this.gForce.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (gForce.getText().equals("") == false)
				{
					ledArr[2] = true;

					for (int i = 1; i < NUM_OF_FIELDS - 1; i++)
					{
						if (ledArr[i] == true && i != 2)
						{
							ledArr[i] = false;

							if (i == 3)
							{
								rpm.setText("rpm");
							}
						}
					}
				}
				else
				{
					gForce.setText("g Force");
				}

				repaint();
			}

			@Override
			public void focusGained(FocusEvent arg0)
			{
				gForce.setText("");
			}
		});


		/*
		 * Listener for the textField 'rpm'
		 */
		this.rpm.addFocusListener(new FocusListener(){

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (rpm.getText().equals("") == false)
				{
					ledArr[3] = true;

					for (int i = 1; i < NUM_OF_FIELDS - 1; i++)
					{
						if (ledArr[i] == true && i != 3)
						{
							ledArr[i] = false;

							if (i == 2)
							{
								gForce.setText("g force");
							}
						}
					}
				}
				else
				{
					rpm.setText("rpm");
				}

				repaint();
			}

			@Override
			public void focusGained(FocusEvent arg0)
			{
				rpm.setText("");
			}
		});
	}


	
	
	/*
	 * Draws the buttons, textFields, LED indicators and other components on the panel
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		drawTitles(g2d);
		drawButLed(g2d);
		drawLines(g2d);
		drawBorder(g2d);
	}

	
	
	/*
	 * Draws the titles
	 */
	private void drawTitles(Graphics2D g2d)
	{
		g2d.setColor(Color.BLACK);
		g2d.setFont(TITLE_F);
		g2d.drawString("ACTIONS", 600, 30);
		g2d.fillRect(600, 32, 124, 3);

		g2d.setFont(BUT_SEC_F);
		g2d.drawString("BASIC", 100, 70);
		g2d.drawString("SET", 520, 70);
		g2d.drawString("CODE", 880, 70);
		g2d.drawString("CONFIRM", 1190, 70);
	}


	
	/*
	 * Draws the LED indicators of each button and textField
	 */
	private void drawButLed(Graphics2D g2d)
	{
		int index = 0;

		g2d.setColor(setToColor(index));
		int tmpCalc = BUT_TEXT_STA_X_AXIS + ((BUT_TEXT_WIDTH - 20) / 2);
		g2d.fillOval(tmpCalc, BUT_TEXT_Y_AXIS - 30, 20, 20);

		index++;
		g2d.setColor(setToColor(index));
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + BUT_TEXT_GAP;
		g2d.fillOval(tmpCalc, BUT_TEXT_Y_AXIS - 30, 20, 20);

		index++;
		g2d.setColor(setToColor(index));
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + (2 * BUT_TEXT_BIG_GAP);
		g2d.fillOval(tmpCalc, BUT_TEXT_Y_AXIS - 30, 20, 20);

		index++;
		g2d.setColor(setToColor(index));
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + BUT_TEXT_GAP;
		g2d.fillOval(tmpCalc, BUT_TEXT_Y_AXIS - 30, 20, 20);

		index++;
		g2d.setColor(setToColor(index));
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + (2 * BUT_TEXT_BIG_GAP) + (BUT_TEXT_GAP / 2);
		g2d.fillOval(tmpCalc, BUT_TEXT_Y_AXIS - 30, 20, 20);

		index++;
		g2d.setColor(setToColor(index));
		tmpCalc = tmpCalc + BUT_TEXT_WIDTH + (2 * BUT_TEXT_BIG_GAP) + BUT_TEXT_GAP;
		g2d.fillOval(tmpCalc, BUT_TEXT_Y_AXIS - 30, 20, 20);

		g2d.setColor(Color.BLACK);
	}


	
	/*
	 * Draws separation lines between sections 
	 */
	private void drawLines(Graphics2D g2d)
	{
		g2d.setColor(Color.DARK_GRAY);

		int tmpCalc = BUT_TEXT_STA_X_AXIS + (2 * BUT_TEXT_WIDTH) + BUT_TEXT_GAP + BUT_TEXT_BIG_GAP;

		g2d.drawLine(10, 45, 1356, 45);

		g2d.drawLine(tmpCalc, 45, tmpCalc, 190);

		tmpCalc = (tmpCalc * 2) - BUT_TEXT_STA_X_AXIS + BUT_TEXT_BIG_GAP;

		g2d.drawLine(tmpCalc, 45, tmpCalc, 190);

		tmpCalc = tmpCalc + (2 * BUT_TEXT_BIG_GAP) + BUT_TEXT_WIDTH + BUT_TEXT_GAP;

		g2d.drawLine(tmpCalc, 45, tmpCalc, 190);

		g2d.setColor(Color.BLACK);
	}
	
	
	
	
	/*
	 * Draws the border of the panel
	 */
	private void drawBorder(Graphics2D g2d)
	{
		g2d.setColor(Color.DARK_GRAY);
		
		g2d.drawLine(0, 0, 1366, 0);
	}


	
	/*
	 * Set the correct color for the LED according to the boolean val in the LED array at index 'index'.
	 * INPUT: index - the index of the LED in the LED array
	 */
	private Color setToColor(int index)
	{
		if (this.ledArr[index] == false)
		{
			return Color.DARK_GRAY;
		}

		return Color.BLUE;
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
