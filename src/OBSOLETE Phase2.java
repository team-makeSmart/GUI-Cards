import java.awt.*;
import javax.swing.*;

public class Phase2
{
   private static int NUM_CARDS_PER_HAND = 7;
   private static int NUM_PLAYERS = 2;
   private static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   private static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
   private static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];

   public static void main(String[] args)
   {
      Icon tempIcon;

      // Establish main frame in which program will run
      CardTable myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND,
         NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Deck deck = new Deck();
      deck.shuffle();

      // Fill the arrays with cards
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         humanLabels[i] = new JLabel(GUICard.getIcon((deck.dealCard())));
      }

      // Add the back cards for the computer and the cards for the hand
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         myCardTable.pnlComputerHand.add(computerLabels[i]);
         myCardTable.pnlHumanHand.add(humanLabels[i]);
      }

      // Create the two cards in the playing area
      // NOTE: instead of JLabel[] playLabelText
      // String array it is used because otherwise
      // all the panels must be resized to fit two
      // labels vertically in the playing area
      String playerNames[] =
      { "Computer", "You" };

      for (int i = 0; i < NUM_PLAYERS; i++)
      {
         tempIcon = GUICard.getIcon((deck.dealCard()));
         playedCardLabels[i] = new JLabel(tempIcon);
         playedCardLabels[i].setText(playerNames[i]);
         playedCardLabels[i].setHorizontalTextPosition(JLabel.CENTER);
         playedCardLabels[i].setVerticalTextPosition(JLabel.BOTTOM);
         myCardTable.pnlPlayArea.add(playedCardLabels[i]);
      }

      // Show everything to the user
      myCardTable.setVisible(true);
   }

   /**
    * Method for testing Generates and returns a card object with a random suit
    * & values
    * 
    * @returns a newly generated random card object
    */
   public static Card generateRandomCard()
   {
      // Generate a random integer that will be a valid index value of the
      // array Card.VALID_CARD_VALUES
      int cardIndex = (int) (Math.random() * Card.valueRanks.length);

      // Get char value at index of array and store in char cardValue
      char cardValue = Card.valueRanks[cardIndex];

      // Generate a random number that will be associated with suit of a card
      int suitIndex = (int) (Math.random() * 4);

      // Generate and return a Card object
      if (suitIndex == 0)
      {
         return new Card(cardValue, Card.Suit.CLUBS);
      } 
      else if (suitIndex == 1)
      {
         return new Card(cardValue, Card.Suit.HEARTS);
      } 
      else if (suitIndex == 2)
      {
         return new Card(cardValue, Card.Suit.SPADES);
      } 
      else
      {
         return new Card(cardValue, Card.Suit.DIAMONDS);
      }

   }

}

/**
 * Controls the positioning of the panels and cards of the GUI
 */
class CardTable extends JFrame
{
   public static int MAX_CARDS_PER_HAND = 56;
   public static int MAX_PLAYERS = 2; // for now, we only allow 2 person games

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   /**
    * Instantiates a new card table.
    *
    * @param title
    *           the title
    * @param numCardsPerHand
    *           the num cards per hand
    * @param numPlayers
    *           the num players
    */
   public CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      super(title);// set the title on the JFrame

      if (numCardsPerHand > MAX_CARDS_PER_HAND || numPlayers > MAX_PLAYERS)
      {
         return;
      }

      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;

      // first load the icons in the 2d array
      GUICard.loadCardIcons();

      // create a default Font style
      UIManager.getDefaults().put("TitledBorder.font",
         (new Font("Arial", Font.BOLD, 14)));

      // three rows zero columns layout, 10 pixels space
      setLayout(new GridLayout(3, 0, 10, 10));

      // create the three panels with title borders
      pnlComputerHand = new JPanel();
      pnlComputerHand
         .setBorder(BorderFactory.createTitledBorder("Computer Hand"));

      pnlPlayArea = new JPanel();
      pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));

      // Zero rows,numPlayers = columns
      pnlPlayArea.setLayout(new GridLayout(0, numPlayers, 10, 10));

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
class GUICard
{
   public final static int NR_OF_VALUES = 14;
   public final static int NR_OF_SUITS = 4;

   // 14 = A thru K + joker
   private static Icon[][] iconCards = new ImageIcon[NR_OF_VALUES][NR_OF_SUITS];

   private static Icon iconBack;
   static boolean iconsLoaded = false;

   public static void loadCardIcons()
   {
      if (iconCards[0][0] != null)
      {
         iconsLoaded = true;
         return;
      } 
      else
      {
         String inputFileName = "src/images/";
         String fileExtension = ".gif";

         for (int i = 0; i < iconCards.length; i++)
         {
            for (int j = 0; j < iconCards[i].length; j++)
            {
               iconCards[i][j] = new ImageIcon(
                  inputFileName + turnIntIntoCardValue(i)
                     + turnIntIntoCardSuit(j) + fileExtension);
            }
         }

         // set the card back icon.
         iconBack = new ImageIcon(inputFileName + "BK" + fileExtension);
      }

      // testing
      System.out.println(iconBack);
      System.out.println(getIcon(new Card('4', Card.Suit.CLUBS)));

   }

   /**
    * This method creates and returns an icon representing the values of the
    * Card object received as an argument
    * 
    * @param card
    *           receives a card object
    * @return object of type Icon representing a card
    */
   static public Icon getIcon(Card card)
   {
      return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
   }

   /**
    * Accessor Method to retrieve a card Icon from the back
    * 
    * @return Icon object representing a card
    */
   static public Icon getBackCardIcon()
   {
      return iconBack;
   }

   /**
    * Turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
    * 
    * @param j
    *           the corresponding card value in the array index
    * @return the card value
    */
   public static String turnIntIntoCardValue(int j)
   {
      String values[] =
      { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "X" };
      return values[j];
   }

   /**
    * Turns 0 - 3 into "C", "D", "H", "S"
    * 
    * @param i
    *           the corresponding suit value in the array index
    * @return
    */
   public static String turnIntIntoCardSuit(int i)
   {
      String suits[] =
      { "C", "D", "H", "S" };
      return suits[i];
   }
}
