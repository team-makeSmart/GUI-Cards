// Allows for processing of a deck of cards
class Deck
{

   // Holds the amount of unique cards in a deck
   public final static int CARDS_PER_DECK = 56;
   
   // Holds the max cards for all decks.  6 decks permissible
   public final static int MAX_CARDS = CARDS_PER_DECK * 6;
 
   // Holds a master pack of unique card objects
   private static Card[] masterPack = new Card[CARDS_PER_DECK];

   // Array to hold card objects that equal number of packs 
   private Card[] cards = new Card[MAX_CARDS];
   
   // For the top card in the deck
   private int topCard; 
   
   // For of copies of the masterpack 
   private int numPacks;

   /**
    * Constructor 
    * Initializes deck of cards * argument
    * @param numPacks  
    * */
   public Deck(int numPacks)
   {
      this.numPacks = numPacks; // Initialize numPacks with argument
      allocateMasterPack(); // Initialize the masterpack to copy from
      init(this.numPacks);
   }

   /**
    * Default Constructor 
    * Initializes one deck for cards to be used  
    * */
   public Deck()
   {
      this.numPacks = 1; // Will use only one pack as a default
      allocateMasterPack(); // Initialize the masterpack to copy from
      init(numPacks);
   }
   
   /**
    * Creates a masterPack of 52 unique cards with all the valid possible unique
    * combinations of the card values and suits Checks to ensure that it has not
    * been called before by checking if masterPack instance variable array was
    * already initialized. It does not execute if masterPack was already
    * initialized. note if masterPack[] contains only null values, it contains no
    * objects and therefore must not have been initialized.
    */
   private static void allocateMasterPack()
   {
      int masterPackIndex = 0;

      // Check if masterPack was already initialized, and return if it was
      if (masterPack[masterPackIndex] != null) // masterPack was initialized
      {
         return;
      } 
      else // masterPack was not initialized
      {
         // Assign cards with all unique combos of suits & values to masterPack
         for (Card.Suit suit : Card.Suit.values())
         {
            for (char validCardValue : Card.valueRanks)
            {
               masterPack[masterPackIndex] = new Card(validCardValue, suit);
               masterPackIndex++;
            }
         }
      }
   }

   /**Method initializes the array of cards with amount equal to value
    * in the argument.
    * @param numPacks amount of packs to be in the cards array
    * */
   public void init(int numPacks)
   {
      // Allocate card array with the total amount of cards
      cards = new Card[numPacks * CARDS_PER_DECK];

      int k = 0, pack = 0;

      // Only allow a valid number of cards in the deck
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;

      // Add cards to the array by making copies from the master pack
      for (pack = 0; pack < numPacks; pack++)
      {
         for (k = 0; k < CARDS_PER_DECK; k++)
            cards[(pack * CARDS_PER_DECK + k)] = masterPack[k];
      }
      this.numPacks = numPacks;
      topCard = numPacks * CARDS_PER_DECK;
   }
   
   /**Method shuffles deck of cards*/
   public void shuffle()
   {
      for (int i = 0; i < cards.length; i++)
      {
         // Get a random index in the deck
         int randomIndex = (int) (Math.random() * cards.length);

         // Make a copy of the current card
         Card temp = cards[i];

         // Swap the current and the card at the random index
         cards[i] = cards[randomIndex];
         cards[randomIndex] = temp;
      }
   }

   /**
    * Method removes a card object from top of deck 
    * @returns card object form top of deck
    * */
   public Card dealCard()
   {
      Card error = new Card('s', Card.Suit.DIAMONDS);

      if (topCard == 0)
         return error;
      else
         return cards[--topCard];
   }

   /**Method inspects the card at index K
    * @param k index of the card to be inspected
    * @Returns an error card if the card is bad
    * Else returns the card as it is 
    * */  
   public Card inspectCard(int k)
   {
      Card errorCard = new Card('s', Card.Suit.DIAMONDS);

      if (k < 0 || k >= topCard) // The card is bad
         return errorCard;
      else // The card is good
         return cards[k];
   }

   /**Method returns number of cards*/
   public int getNumCards()
   {
      return topCard;
   }
   
   /**
    * sorts the array by calling the arraySort() in Card class
    */
   public void sort()
   {
      Card.arraySort(cards, cards.length);
   }
   
/**
 *  Puts the card on the top of the deck,
 *  if there there are not too many instances 
 *  of the card in the deck 
 *  
 * @param card the card to be added
 * @return true if the card is added
 */
	public boolean addCard(Card card)
	{
		if (getNumCards() >= CARDS_PER_DECK * numPacks)
			return false;
		
		cards[topCard++] = card;
		return true;
	}
	
	/**
	 * Removes a specific card from the deck.
	 * Puts the current top card into its place.
	 * 
	 * @param card the card to be removed
	 * @return true if the card removed, otherwise false
	 */
	public boolean removeCard(Card card)
	{
		
		for (int i = 0; i < cards.length; i++)
		{
			if (cards[i].equals(card))
			{
				for (int j = i + 1; i < cards.length - 1; j++)
				{
					cards[i] = cards[j];
					i++;
				}
				return true;
			}
		}
		return false;
	}

}
