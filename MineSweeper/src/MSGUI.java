import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
public class MSGUI
{
	private JFrame frame;
	private JLabel lengthLabel;
	private ButtonGroup length;
	private JRadioButton small;
	private JRadioButton medium;
	private JRadioButton large;
	private JLabel densityLabel;
	private ButtonGroup density;
	private JRadioButton thin;
	private JRadioButton normal;
	private JRadioButton thick;
	private JButton startButton;
	private JPanel startPanel;
	private JPanel fieldPanel;
	private JLabel[][] labels;
	private BufferedImage all;
	private BufferedImage[] images;
	private MSField field;
	
	public MSGUI()
	{
		frame = new JFrame("MineSweeper");
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 600);
		lengthLabel = new JLabel("Field Length: ");
		length = new ButtonGroup();
		small = new JRadioButton("8 x 8 Field", true);
		medium = new JRadioButton("10 x 10 Field");
		large = new JRadioButton("12 x 12 Field");
		length.add(small);
		length.add(medium);
		length.add(large);
		densityLabel = new JLabel("Total % of Mines: ");
		density = new ButtonGroup();
		thin = new JRadioButton("10% Mines", true);
		normal = new JRadioButton("15% Mines");
		thick = new JRadioButton("20% Mines");
		density.add(thin);
		density.add(normal);
		density.add(thick);
		startButton = new JButton();
		//add listener for field creation
		startPanel = new JPanel();
		
		fieldPanel = new JPanel();
		//fieldPanel.setSize(256,256);
		//fieldPanel.setMaximumSize(new Dimension(256, 256));
		//fieldPanel.setMinimumSize(new Dimension(256, 256));
		setImages();
		addField();
		
		
		//JLabel test = new JLabel(new ImageIcon(images[11]));
		//fieldPanel.add(test);
		updateField();
		
		frame.add(startPanel);
		frame.add(fieldPanel);
		frame.setVisible(true);
	}
	
	private void setImages()
	{
		try
		{
			File allFile = new File("src/MSTiles.jpg");
			all = ImageIO.read(allFile);
			images = new BufferedImage[12];
			for(int y = 0; y < 2; y++)
			{
				for(int x = 1; x < 5; x++)
				{
					images[4 * y + x] = all.getSubimage(128 * (x-1), 128 * (y+1), 128, 128);
				}
			}
			images[0] = all.getSubimage(384, 0, 128, 128);
			images[9] = all.getSubimage(0, 0, 128, 128);
			images[10] = all.getSubimage(128, 0, 128, 128);
			images[11] = all.getSubimage(256, 0, 128, 128);
			for(int x = 0; x < 12; x++)
			{
				BufferedImage small = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
				Graphics g = small.createGraphics();
				g.drawImage(images[x], 0, 0, 32, 32, null);
				g.dispose();
				images[x] = small;
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void addField()
	{
		int theLength = chooseLength();
		double theDensity = chooseDensity();
		field = new MSField(theLength, theDensity);
		labels = new JLabel[theLength][theLength];
		fieldPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		for(int x = 0; x < field.getLength(); x++)
		{
			for(int y = 0; y < field.getLength(); y++)
			{
				labels[x][y] = new JLabel();
				c.gridx = x;
				c.gridy = y;
				fieldPanel.add(labels[x][y], c);
			}
		}
	}
	private int chooseLength()
	{
		if(small.isSelected())
		{
			return 8;
		}
		else if (medium.isSelected())
		{
			return 10;
		}
		else
		{
			return 12;
		}
	}
	private double chooseDensity()
	{
		if(thin.isSelected())
		{
			return .1;
		}
		else if (normal.isSelected())
		{
			return .15;
		}
		else
		{
			return .2;
		}
	}
	private void updateField()
	{
		for(int x = 0; x < field.getLength(); x++)
		{
			for(int y = 0; y < field.getLength(); y++)
			{
				labels[x][y].setIcon(new ImageIcon(images[field.getMSVertex(x, y).getImage()]));
			}
		}
	}
}