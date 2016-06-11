import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JFrame;

public class GroundControlMain
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Ground Control");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1050, 700);
		frame.setLayout(new BorderLayout());
		
		MainPanel mainP = new MainPanel();
		frame.add(mainP);
		
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
	}

}
