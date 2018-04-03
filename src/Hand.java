// Hand class is used for the hand a player (or players) have in a card game
class Hand
{
   /** The max cards in the hand */
   public static final int MAX_CARDS = 180;

   /** holds all the cards */
   private Card[] myCards = new Card[MAX_CARDS];

   /** The number of card in the array. */
   private int numCards;

   /**
    * Default constructor
    */
   public Hand()
   {
      this.numCards = 0;
   }

   /**
    * Removes all cards from the hand
    */
   public void resetHand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }

   /**
    * Adds a card to the next available position in the myCards array
    * @param card
    *           the card
    * @return true, if successful
    */
   public boolean takeCard(Card card)
   {
      if (numCards < MAX_CARDS)
      {
         // Makes copy of new card and stores in index. & Increments numCards.
         myCards[numCards++] = new Card(card.getValue(), card.getSuit());

         if (numCards == MAX_CARDS) // The hand is full
         {
            return false;
         }
      }
      return true;
   }

   /**
    * Returns and removes the card in the top occupied position of the array
    * @parm cardIndex the index of the card that will be played
    * @return the top card
    */
   public Card playCard(int cardIndex)
   {
      Card errorCard = new Card('w', Card.Suit.SPADES);
      if (numCards == 0 || cardIndex < 0 || cardIndex > MAX_CARDS)
         return errorCard;
      
      // Save the card to play.
      Card playCard = myCards[cardIndex];
      
      // Remove card from hand.
      myCards[cardIndex] = null;
      
      // Store a temporary hand.
      Card[] tempHand = myCards;
      myCards = new Card[--numCards];
      int ndx = 0;
      
      // Add non-null cards from the temp hand to the persistent hand.
      for(Card card : tempHand)
      {
         if(card != null)
         {
            myCards[ndx++] = card;
         }
         
      }
      
      return playCard;
   }
   

   /**
    * Prints value and suit for all the cards in the hand
    */
   public String toString()
   {
      int counter = 0; // Keeps place of cards in the hand
      if (numCards == 0) // There are no cards in the hand
      {
         return "\nHand = (  )";
      } else // There are cards in the hand
      {
         String returnVal = "\nHand = ( ";

         for (int i = 0; i < numCards; i++)
         {
            returnVal += myCards[i].toString();
            if ((counter + 1) != numCards)
               returnVal += ", ";
            counter++;
            // Check if reached end of hand
            if (counter == numCards)
               returnVal += " )";

            // If more than sixth card, go to newline
            if (counter % 6 == 0)
               returnVal += "\n";
         }
         return returnVal;
      }
   }

   /**
    * Gets the number of cards.
    * @return the number of cards
    */
   public int getNumOfCards()
   {
      return numCards;
   }

   /**
    * Accessor for an individual card. Returns a card with 
    * errorFlag = true if k is bad
    * @param k 
    *    the index of the card in the array
    * @return 
    *    the card
    */
   Card inspectCard(int k)
   {
      Card errorCard = new Card('w', Card.Suit.SPADES);
      if (k < 0 || k >= numCards)
         return errorCard;
      return myCards[k];
   }
   
   void sort()
   {
      Card.arraySort(myCards, numCards);
   }

}