import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class IndicatorPanel extends JPanel
{
	JButton b2 = new JButton("bbbbb");
	JButton b4 = new JButton("Move Back");
	
	int tmp = 0;
	public IndicatorPanel()
	{
		setBackground(Color.RED);
		this.setPreferredSize(new Dimension(this.getX(), this.getY()));
		
		this.add(b2);
		this.add(b4);
		
		b2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tmp = tmp + 50;
				
				//repaint();
			}
		});
		
		b4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tmp = tmp - 50;
				
				//repaint();
			}
		});
		
		
		//repaint();
	}
	
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawRect(10 + tmp, 10 + tmp, 50, 50);
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
