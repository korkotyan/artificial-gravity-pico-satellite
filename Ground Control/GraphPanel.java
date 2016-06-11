import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GraphPanel extends JPanel
{
	JButton b = new JButton("aaaa");
	JButton b3 = new JButton("smaller");
	
	int tmp = 0;
	
	public GraphPanel()
	{
		this.setPreferredSize(new Dimension(850, this.getY()));
		this.add(b);
		this.add(b3);
		
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tmp = tmp + 50;
				
				//repaint();
			}
		});
		
		b3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				tmp = tmp - 50;
				
				//repaint();
			}
		});
		
		this.setBackground(Color.BLUE);
		
		//repaint();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawRect(10, 10, 50 + tmp, 50 + tmp);
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
