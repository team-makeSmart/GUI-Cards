import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Phase3
{
	static CardGameFramework highCardGame;
	static int NUM_CARDS_PER_HAND = 7;
	static int NUM_PLAYERS = 2;
	static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
	static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
	static JLabel[] scoreTextLabel = new JLabel[NUM_PLAYERS];
	static String[] playerNames = {"Computer","You"};
	static Card[] cardPlayed = new Card[NUM_PLAYERS];
	static int[] score = {0,0};
	static boolean cardsClickable = true;

	public static void main(String[] args)
	{
		Icon tempIcon;
		
		// instantiate the CardGameFramework, used for gameplay
		int numPacksPerDeck = 1;
		int numJokersPerPack = 0;
		int numUnusedCardsPerPack = 0;
		Card[] unusedCardsPerPack = null;

		highCardGame = new CardGameFramework( 
				numPacksPerDeck, numJokersPerPack, 
				numUnusedCardsPerPack, unusedCardsPerPack, 
				NUM_PLAYERS, NUM_CARDS_PER_HAND);
		
		// deal the cards
		highCardGame.deal();

		// establish main GUI frame in which the program will run
		CardTable myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
		myCardTable.setSize(800, 600);
		myCardTable.setLocationRelativeTo(null);
		myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
		{
			//fill the arrays with cards
			computerLabels[i]=new JLabel(GUICard.getBackCardIcon());
			humanLabels[i]= new JLabel(GUICard.getIcon((highCardGame.getHand(1).inspectCard(i))));
			
			//add the back cards for the computer and the cards for the hand 
			myCardTable.pnlComputerHand.add(computerLabels[i]);
			myCardTable.pnlHumanHand.add(humanLabels[i]);
			
			// make the human cards clickable for gameplay
			humanLabels[i].addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (cardsClickable == true)
					{
						cardsClickable = false; //temporarily make the cards unclickable
						onMouseClicked(e);
					}
				}
			});
		}

		
		
		// Add cards and score to the GUI
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			String playedCardText = "<html><div style='text-align: center;'>" + playerNames[i] + "<br/>Score: " + score[i];

			// add cards for each player
			playedCardLabels[i] = new JLabel(playedCardText);
			playedCardLabels[i].setBorder(new EmptyBorder(0,0,20,0));
			playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
			playedCardLabels[i].setVerticalAlignment(JLabel.BOTTOM);
	
			myCardTable.pnlPlayArea.add(playedCardLabels[i]);
			
		}
		// show everything to the user
		myCardTable.setVisible(true);
		
		computerPlayCard();
	}
	

	/**
	 * Used for handling clicks on the human cards
	 * @param e the card JLabel that is clicked
	 */
	private static void onMouseClicked(MouseEvent e) 
	{
		// temporarily make the cards unclickable
		cardsClickable = false;
		
		// find which card was clicked
		for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
		{
			if (e.getSource() == humanLabels[i]) {
				// get the card
				Card card = highCardGame.getHand(1).inspectCard(i);
				
				// play the card
				cardPlayed[1] = card;
				
				// add the card to the playing area
				playedCardLabels[1].setIcon(GUICard.getIcon(card));
				playedCardLabels[1].setHorizontalTextPosition(JLabel.CENTER);
				playedCardLabels[1].setVerticalTextPosition(JLabel.BOTTOM);
				
				// remove the card from the hand
				humanLabels[i].setIcon(null);
				
				// if computer has not played a card
				if (cardPlayed[0] == null) 
				{
					// Creates a delay of one second
					final int ONE_SECOND = 1000;
					Timer timer = new Timer(1, new ActionListener() 
					{
						public void actionPerformed(ActionEvent e)
						{
							computerPlayCard();	
						}
					});
					timer.setRepeats(false);
					timer.start();
					
				} else // computer has played a cared
				{
					checkWinner();
				}
			}
		}	
	}
	
	
	/**
	 * Plays a card from the computers hand
	 */
	private static void computerPlayCard() 
	{
		Card card;
		int cardIndex;
		
		// if the player has played a card
		if (cardPlayed[1] == null) 
		{
			cardIndex = 0;
		} else // if the player has not played a card
		{
			cardIndex = 0;
		}
		
		// set the card
		card = highCardGame.getHand(0).inspectCard(cardIndex);
		
		// play the card
		cardPlayed[0] = card;
		
		// update GUI
		playedCardLabels[0].setIcon(GUICard.getIcon(card));
		playedCardLabels[0].setHorizontalTextPosition(JLabel.CENTER);
		playedCardLabels[0].setVerticalTextPosition(JLabel.BOTTOM);
		
		// remove the card from the hand
		computerLabels[cardIndex].setIcon(null);
		
		// if human has played their card
		if (cardPlayed[1] != null)
			checkWinner();
	}
	
	/**
	 * When both cards (computer and human) have been played,
	 * checks for a winner, updates scores, and resets the playing area 
	 */
	private static void checkWinner() 
	{
		// make sure both cards have been played
		if (cardPlayed[0] == null || cardPlayed[1] == null)
			return;
		
		// if computer card is higher than players card
		if (Card.getSortRanking(cardPlayed[0]) > Card.getSortRanking(cardPlayed[1]))
		{
			// increment computers score
			score[0]++; 
		} else 
		{
			// increment players card
			score[1]++;
		}
		
		
		// Creates a delay of two seconds, so that the user can see the result of the current round before scoring
		final int TWO_SECONDS = 2000;
		Timer timer = new Timer(TWO_SECONDS, new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				// reset both played cards
				for (int i = 0; i < NUM_PLAYERS; i++)
				{
					String playedCardText = "<html><div style='text-align: center;'>" + playerNames[i] + "<br/>Score: " + score[i];
					cardPlayed[i] = null;
					playedCardLabels[i].setText(playedCardText);
					playedCardLabels[i].setIcon(null);
					playedCardLabels[i].setBorder(new EmptyBorder(0,0,20,0));
					playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
					playedCardLabels[i].setVerticalAlignment(JLabel.BOTTOM);
					
					// make the cards clickable again
					cardsClickable = true;
				}		
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

}


class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
                                    // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
                                    // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
                                        // in the game.  e.g. pinochle does not
                                        // use cards 2-8 of any suit

   public CardGameFramework( int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() { return deck.dealCard(); }

   public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
          cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);      
      }
   
      // return the card played
      return hand[playerIndex].playCard(cardIndex);
   
   }


   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;
     
       // Are there enough Cards?
       if (deck.getNumCards() <= 0)
          return false;

       return hand[playerIndex].takeCard(deck.dealCard());
   }

}

