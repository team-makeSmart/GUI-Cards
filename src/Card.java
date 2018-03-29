// Card class is for objects that represent an individual playing card
class Card
{

   /** Constant array of valid card values acceptable for program */
   public static char[] valueRanks =
   { 'X', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };

   /**
    * Enumerated Suit values
    */
   public enum Suit
   {
      CLUBS, DIAMONDS, HEARTS, SPADES
   }

   /** Card value (e.g. 1,2,3,..., 9, T, J, K Q, A) */
   private char value;

   /** Card suit */
   private Suit suit;

   /**
    * Error flag, keeps track of invalid entries. If true, the card object does not
    * have valid data
    */
   private boolean errorFlag;

   /**
    * Default Constructor Instantiates a new card as Ace of Spades
    */
   public Card()
   {
      this.value = 'A';
      this.suit = Suit.SPADES;
   }

   /**
    * Constructor Instantiates a new card, by calling the set() method
    * 
    * @param value
    *           the value
    * @param suit
    *           the suit
    */
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   /**
    * Returns a string based on the value and suit instance variables
    */
   public String toString()
   {
      if (errorFlag == true) // Card does not contain valid data
      {
         return "** illegal **"; // Returns an error message
      } else
      {
         // Get char value, convert to string, and store in variable
         String returnValue = String.valueOf(getValue());

         // Concatenate returnValue with a string relating to suit
         if (suit == Suit.SPADES)
            returnValue += " of Spades";
         else if (suit == Suit.HEARTS)
            returnValue += " of Hearts";
         else if (suit == Suit.DIAMONDS)
            returnValue += " of Diamonds";
         else if (suit == Suit.CLUBS)
            returnValue += " of Clubs";

         return returnValue;
      }
   }

   /**
    * Sets the value and the suit
    * 
    * @param value
    *           the value
    * @param suit
    *           the suit
    * @return true, if successful
    */
   public boolean set(char value, Suit suit)
   {
      if (!isValid(value, suit))
      {
         errorFlag = true;
         return false;
      }
      this.suit = suit;
      this.value = value;
      errorFlag = false;
      return true;
   }

   /**
    * Gets the errorFlag
    * 
    * @return the error flag
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   /**
    * Gets the suit
    * 
    * @return the suit
    */
   public Suit getSuit()
   {
      return suit;
   }

   /**
    * Gets the value
    * 
    * @return the value
    */
   public char getValue()
   {
      return value;
   }

   /**
    * Gets the suit as an integer
    * 
    * @return the suit
    */
   public static int suitAsInt(Card card)
   {
      return card.getSuit().ordinal();
   }

   /**
    * Gets the value as an integer
    * 
    * @return the value
    */
   public static int valueAsInt(Card card)
   {
      String values = new String(valueRanks);
      return values.indexOf(card.getValue());
   }

   /**
    * Checks for equal values
    */
   @Override
   public boolean equals(Object obj)
   {
      final Card other = (Card) obj;

      if (this == obj)
      {
         return true;
      }

      if (obj == null || getClass() != obj.getClass() || this.value != other.value || this.errorFlag != other.errorFlag
            || this.suit != other.suit)
      {
         return false;
      }

      return true;
   }

   /**
    * Checks if is the values entered by the user are valid.
    * 
    * @param value
    *           the value
    * @param suit
    *           the suit
    * @return true, if all values are valid
    */
   private boolean isValid(char value, Suit suit)
   {
      for (char val : valueRanks)
      {
         if (value == val)
         {
            return true; // The value arg is found in VALID_CARD_VALUES array
         }
      }
      return false; // The value argument was not found
   }

   /**
    * Sorts an array of Cards using QuickSort.
    * @param cards
    *             An array of Cards.
    * @param arraySize
    *                  The number of Cards to sort from the first index.
    */
   static void arraySort(Card[] cards, int arraySize)
   {
      // Start timer.
      long startTime = System.nanoTime();

      // Don't sort if there are no cards.
      if (arraySize <= 0)
      {
         return;
      }

      quickSort(cards, 0, arraySize - 1);
      
      // End timer, display sort time.
      System.out.println("Sort complete. Took " + ((System.nanoTime() - startTime) / 100000) + " ms.");

   }

   /**
    * Sorts an array of Cards from index lowerNdx to index upperNdx.
    * @param cardArray
    *                   The array of Cards to be sorted.
    * @param lowerNdx
    *                   The starting index to sort from.
    * @param upperNdx
    *                   The ending index to sort to.
    */
   private static void quickSort(Card[] cardArray, int lowerNdx, int upperNdx)
   {
      // Return if this section is already sorted.
      if (lowerNdx >= upperNdx)
      {
         return;
      }

      // Find the mid-point between the two indices lowerNdx and upperNdx.
      int pivotNdx = lowerNdx + ((upperNdx - lowerNdx) / 2);
      
      // Get the sort ranking of the Card at the index pivotNdx.
      int pivotValue = getSortRanking(cardArray[pivotNdx]);

      // Initialize loop variables to lower and upper index bounds.
      int i = lowerNdx;
      int j = upperNdx;

      // Sort until the loop variables swap places.
      while (i <= j)
      {
         // Get the sort ranking of the cards at indices i and j.
         int lowerValue = getSortRanking(cardArray[i]);
         int upperValue = getSortRanking(cardArray[j]);
         
         // Find a card with sort ranking higher than the pivotValue in the lower section of the cardArray.
         // This loop ends when an unsorted card is found.
         while (lowerValue < pivotValue)
         {
            lowerValue = getSortRanking(cardArray[++i]);
         }
           
         // Find a card with sort ranking lower than the pivotValue in the upper section of the cardArray.
         // This loop ends when an unsorted card is found.
         while (upperValue > pivotValue)
         {
            upperValue = getSortRanking(cardArray[--j]);
         }

         // Swap the two unsorted cards.
         if (i <= j)
         {
            Card tempCard = cardArray[j];
            cardArray[j] = cardArray[i];
            cardArray[i] = tempCard;
            i++;
            j--;
         }

         // Use recursion to repeat the sorting process until all cards are sorted in ascending order.
         if (lowerNdx < j)
         {
            quickSort(cardArray, lowerNdx, j);
         }

         if (upperNdx > i)
         {
            quickSort(cardArray, i, upperNdx);
         }
      }
   }

   /**
    * Calculates a card's sort ranking based on value and suit.
    * @param card
    *             The Card to evaluate.
    * @return
    */
   static int getSortRanking(Card card)
   {
      return (valueAsInt(card) * Suit.values().length) + suitAsInt(card);
   }

}