/**
 * 
 * Reads the .gif image files as Icons, and attach them to JLabels that 
 *  can be displayed on a JFrame.
 */
import javax.swing.*;
import java.awt.*;

public class Assig5 {
	
	// 52 + 4 jokers + 1 back-of-card
	static final int NUM_CARD_IMAGES = 57; 
	// Holds 57 icons and their corresponding labels										
	static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];

	/**
	 * Instantiates each of the 57 Icons in the icon[] array.
	 */
	static void loadCardIcons()
	{
		String inputFileName = "src/images/";
		String fileExtension = ".gif";
		
 		final int SUITS_LENGTH=4;
		final int VALUES_LENGTH=14;
		int arrIndex=0;
		
		
		for (Integer i = 0; i <  SUITS_LENGTH ; i++)
		{
			for (int j = 0; j < VALUES_LENGTH; j++)
			{
				icon[arrIndex] = new ImageIcon(
						inputFileName + turnIntIntoCardValue(j)
						+ turnIntIntoCardSuit(i) + fileExtension);
				arrIndex++;
			}
		}
		//back card is the last card in the array (should it be last?)
		icon[NUM_CARD_IMAGES-1] =new ImageIcon(inputFileName+"BK"+fileExtension);
	}


	/**
	 * Turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
	 * @param j the corresponding card value in the array index
	 * @return the card value
	 */
	static String turnIntIntoCardValue(int j)
	{
		String values[] = { "A", "2", "3", "4", "5", "6", "7", 
				"8", "9", "T", "J", "Q", "K", "X" };
		return values[j];
	}

	/**
	 * Turns 0 - 3 into "C", "D", "H", "S"
	 * @param i the corresponding suit value in the array index
	 * @return
	 */
	static String turnIntIntoCardSuit(int i)
	{
		String suits[] = { "C", "D", "H", "S" };
		return suits[i];
	}

	// a simple main to throw all the JLabels out there for the world to see
	public static void main(String[] args)
	{
		int k;

		// prepare the image icon array
		loadCardIcons();

		// establish main frame in which program will run
		JFrame frmMyWindow = new JFrame("Card Room");
		frmMyWindow.setSize(1150, 650);
		frmMyWindow.setLocationRelativeTo(null);
		frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set up layout which will control placement of buttons, etc.
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
		frmMyWindow.setLayout(layout);

		// prepare the image label array
		JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
		for (k = 0; k < NUM_CARD_IMAGES; k++)
			labels[k] = new JLabel(icon[k]);

		// place your 3 controls into frame
		for (k = 0; k < NUM_CARD_IMAGES; k++)
			frmMyWindow.add(labels[k]);

		// show everything to the user
		frmMyWindow.setVisible(true);
	}
}