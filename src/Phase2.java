import javax.swing.*;


public class Phase2
{
   public static void main(String[] args)
   {
      Deck deck = new Deck();
      Hand hand = new Hand();
      
      deck.shuffle();
      
      while(deck.getNumCards() > 0)
      {
         hand.takeCard(deck.dealCard());
      }
      System.out.println("Shuffled");
      System.out.println(hand.toString());
      System.out.println();
      
      hand.sort();

      System.out.println(hand.toString());
      //JLabel myLabel = new JLabel( "My Text", JLabel.CENTER );
      //Card card = new Card('X', Card.Suit.CLUBS);
      //System.out.println(card.getValue() + " -> " + card.valueAsInt());
   }
}

class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2; // for now, we only allow 2 person games

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
   }
}

class GUICard
{
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   
   static void loadCardIcons()
   {
      
   }
   
   static public Icon getIcon(Card card)
   {
      return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
   }
   
   static public Icon getBackCardIcon()
   {
      return iconBack;
   }
}

