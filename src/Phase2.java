import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Phase2
{
	static int NUM_CARDS_PER_HAND = 7;
	static int NUM_PLAYERS = 2;
	static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
	static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];

	public static void main(String[] args)
	{
		int k;
		Icon tempIcon;

		// establish main frame in which program will run
		CardTable myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
		myCardTable.setSize(800, 600);
		myCardTable.setLocationRelativeTo(null);
		myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GUICard.loadCardIcons();

		Deck deck = new Deck();

		deck.shuffle();

		for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
		{
			myCardTable.pnlComputerHand.add(new JLabel(GUICard.getBackCardIcon()));
			myCardTable.pnlHumanHand.add(new JLabel(GUICard.getIcon(deck.dealCard())));
		}

		// CREATE LABELS ----------------------------------------------------
		// code goes here ...

		// ADD LABELS TO PANELS -----------------------------------------
		// code goes here ...

		// and two random cards in the play region (simulating a computer/hum
		// ply)
		// code goes here ...

		// show everything to the user
		myCardTable.setVisible(true);
	}
}

/**
 * Controls the positioning of the panels and cards of the GUI
 */
class CardTable extends JFrame
{
	static int MAX_CARDS_PER_HAND = 56;
	static int MAX_PLAYERS = 2; // for now, we only allow 2 person games

	private int numCardsPerHand;
	private int numPlayers;

	public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

	/**
	 * Instantiates a new card table.
	 *
	 * @param title
	 *            the title
	 * @param numCardsPerHand
	 *            the num cards per hand
	 * @param numPlayers
	 *            the num players
	 */
	CardTable(String title, int numCardsPerHand, int numPlayers) {
		super(title);
		// create a default Font style
		UIManager.getDefaults().put("TitledBorder.font", (new Font("Arial", Font.BOLD, 14)));

		// three rows, zero columns layout, 10 pixels space
		setLayout(new GridLayout(3, 0, 10, 10));

		// create the three panels with title borders
		pnlComputerHand = new JPanel();
		pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
		pnlPlayArea = new JPanel();
		pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
		pnlHumanHand = new JPanel();
		pnlHumanHand.setBorder(BorderFactory.createTitledBorder("Your Hand"));

		// add the panels to the JFrame
		add(pnlComputerHand);
		add(pnlPlayArea);
		add(pnlHumanHand);

	}
}

/**
 * Manages the reading and building of the card image Icons
 */
class GUICard {
	private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K +
																// joker
	private static Icon iconBack;
	static boolean iconsLoaded = false;

	static void loadCardIcons()
	{
		if (iconCards[0][0] != null)
		{
			iconsLoaded=true;
			return;
		}
		else
		{
			String inputFileName = "src/images/";
			String fileExtension = ".gif";

			for (int i = 0; i < iconCards.length ; i++)
			{
				for (int j = 0; j <  iconCards[i].length ; j++)																
				{
					iconCards[i][j] = new ImageIcon(inputFileName + Phase1.turnIntIntoCardValue(i)
							+ Phase1.turnIntIntoCardSuit(j) + fileExtension);
				}
			}
			//set the last card of the array as back card
			iconCards[13][3]=new ImageIcon(inputFileName+"BK"+fileExtension);
			iconBack = iconCards[13][3];
		}
		
		//testing
		System.out.println(iconBack);
		System.out.println(getIcon(new Card('4', Card.Suit.CLUBS)));

	}

	static public Icon getIcon(Card card) {
		return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
	}

	static public Icon getBackCardIcon() {
		return iconBack;
	}
}
