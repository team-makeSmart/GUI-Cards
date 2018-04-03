import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Phase3 {
	static CardGameFramework highCardGame;
	static int NUM_CARDS_PER_HAND = 7;
	static int NUM_PLAYERS = 2;
	static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
	static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
	static JLabel[] scoreTextLabel = new JLabel[NUM_PLAYERS];
	static String[] playerNames = { "Computer", "You" };
	static Card[] cardPlayed = new Card[NUM_PLAYERS];
	static int[] score = { 0, 0 };
	static boolean cardsClickable = true;
	static boolean computerPlaysFirst = true;

	public static void main(String[] args) {

		// instantiate the CardGameFramework, used for gameplay
		int numPacksPerDeck = 1;
		int numJokersPerPack = 0;
		int numUnusedCardsPerPack = 0;
		Card[] unusedCardsPerPack = null;

		highCardGame = new CardGameFramework(numPacksPerDeck, numJokersPerPack, numUnusedCardsPerPack,
				unusedCardsPerPack, NUM_PLAYERS, NUM_CARDS_PER_HAND);

		// deal the cards
		highCardGame.deal();

		// establish main GUI frame in which the program will run
		CardTable myCardTable = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
		myCardTable.setSize(800, 600);
		myCardTable.setLocationRelativeTo(null);
		myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		System.out.println("AI Hand: ");
		System.out.println(highCardGame.getHand(0).toString());

		System.out.println("\nPlayer Hand: ");
		System.out.println(highCardGame.getHand(1).toString());
		for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
			// fill the arrays with cards
			computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
			// (uncomment to view computer hand) computerLabels[i] = new
			// JLabel(GUICard.getIcon((highCardGame.getHand(0).inspectCard(i))));

			humanLabels[i] = new JLabel(GUICard.getIcon((highCardGame.getHand(1).inspectCard(i))));

			// add the back cards for the computer and the cards for the hand
			myCardTable.pnlComputerHand.add(computerLabels[i]);
			myCardTable.pnlHumanHand.add(humanLabels[i]);

			// make the human cards clickable for gameplay
			humanLabels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					onMouseClicked(e);
				}
			});
		}

		// Add cards and score to the GUI
		for (int i = 0; i < NUM_PLAYERS; i++) {
			String playedCardText = "<html><div style='text-align: center;'>" + playerNames[i] + "<br/>Score: "
					+ score[i];

			// add cards for each player
			playedCardLabels[i] = new JLabel(playedCardText);
			playedCardLabels[i].setBorder(new EmptyBorder(0, 0, 20, 0));
			playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
			playedCardLabels[i].setVerticalAlignment(JLabel.BOTTOM);

			myCardTable.pnlPlayArea.add(playedCardLabels[i]);

		}
		// show everything to the user
		myCardTable.setVisible(true);

		// have the computer play a card
		if (computerPlaysFirst)
			computerPlayCard();
	}

	/**
	 * Used for handling clicks on the human cards
	 * 
	 * @param e
	 */
	private static void onMouseClicked(MouseEvent e) 
	{
		
		// check to make sure a card isn't already being played
		if (cardsClickable)
		{
			// temporarily make the other cards unclickable
			cardsClickable = false;
			
			for (int i = 0; i < NUM_CARDS_PER_HAND; i++) 
			{
				if (e.getSource() == humanLabels[i]) 
				{
					// get the card
					Card card = highCardGame.playCard(1, i);

					// play the card
					cardPlayed[1] = card;

					// add the card to the playing area
					playedCardLabels[1].setIcon(GUICard.getIcon(card));
					playedCardLabels[1].setHorizontalTextPosition(JLabel.CENTER);
					playedCardLabels[1].setVerticalTextPosition(JLabel.BOTTOM);

					// remove the card from the hand
					humanLabels[i].setIcon(null);

					// if computer has not played a card
					if (cardPlayed[0] == null) {
						// creates a delay of one second
						final int ONE_SECOND = 1000;
						Timer timer = new Timer(ONE_SECOND, new ActionListener() 
						{
							public void actionPerformed(ActionEvent e) {
								computerPlayCard();
							}
						});
						
						// start the timer
						timer.setRepeats(false);
						timer.start();
						
					} else // computer has played a cared
					{
						checkWinner();
					}
					
				}
			}
		}
	}

	private static int computerChooseCard() {
		// Going second.
		if (cardPlayed[1] != null) {

			int opponentCardRank = Card.getSortRanking(cardPlayed[1]);
			System.out.println("Competition: " + cardPlayed[1].toString() + " " + opponentCardRank);

			// Make a temporary hand to try different scenarios.
			Card[] ascHand = new Card[highCardGame.getHand(0).getNumOfCards()];

			// Add cards to temporary hand.
			for (int i = 0; i < highCardGame.getHand(0).getNumOfCards(); i++) {
				ascHand[i] = highCardGame.getHand(0).inspectCard(i);
			}

			// Sort temporary hand in ascending order of sort rank.
			Card.arraySort(ascHand, ascHand.length);
			System.out.println("Possible plays: ");
			for (int a = 0; a < ascHand.length; a++) {
				System.out.println(ascHand[a].toString() + " -> rank " + Card.getSortRanking(ascHand[a]));
			}
			// First we try to play a card higher than the opponent's.
			for (int x = 0; x < ascHand.length; x++) {
				if (Card.getSortRanking(ascHand[x]) > opponentCardRank) {
					for (int y = 0; y < highCardGame.getHand(0).getNumOfCards(); y++) {
						if (ascHand[x].toString().equals(highCardGame.getHand(0).inspectCard(y).toString())) {
							return y;
						}
					}
				}
			}

			// Next we try to play a card equal to the opponent's.
			for (int x = 0; x < ascHand.length; x++) {
				if (Card.getSortRanking(ascHand[x]) == opponentCardRank) {
					for (int y = 0; y < highCardGame.getHand(0).getNumOfCards(); y++) {
						if (ascHand[x].toString().equals(highCardGame.getHand(0).inspectCard(y).toString())) {
							return y;
						}
					}
				}
			}

			// If we can't beat their card, we play our lowest card instead.
			for (int y = 0; y < highCardGame.getHand(0).getNumOfCards(); y++) {
				if (ascHand[0].toString().equals(highCardGame.getHand(0).inspectCard(y).toString())) {
					return y;
				}
			}

		}

		// Going first.
		// Play a random available card.
		return (int) (Math.random() * highCardGame.getHand(0).getNumOfCards());
	}

	/**
	 * Plays a card from the computers hand
	 */
	private static void computerPlayCard() {

		int cardIndex = computerChooseCard();
		Card card = highCardGame.getHand(0).inspectCard(cardIndex);

		// set the card
		card = highCardGame.playCard(0, cardIndex);

		// play the card
		cardPlayed[0] = card;

		System.out.println("Optimal card: " + card.toString() + " -> rank " + Card.getSortRanking(card));

		// update GUI
		playedCardLabels[0].setIcon(GUICard.getIcon(card));
		playedCardLabels[0].setHorizontalTextPosition(JLabel.CENTER);
		playedCardLabels[0].setVerticalTextPosition(JLabel.BOTTOM);

		// remove the card from the hand
		removeGUICard(0, card, true);

		// if human has played their card
		if (cardPlayed[1] != null)
			checkWinner();

		System.out
				.println("\nAI Hand: " + highCardGame.getHand(0).getNumOfCards() + highCardGame.getHand(0).toString());
	}

	private static void removeGUICard(int playerID, Card card, boolean isHidden) {
		Icon removeIcon = GUICard.getBackCardIcon();

		// Remove a card with the back icon if they are hidden
		if (!isHidden) {
			removeIcon = GUICard.getIcon(card);
		}

		if (playerID == 0) {
			for (int i = 0; i < computerLabels.length; i++) {
				if (computerLabels[i].getIcon() != null) {
					if (computerLabels[i].getIcon().equals(removeIcon)) {
						computerLabels[i].setIcon(null);
						return;
					}
				}
			}
		} else if (playerID == 1) {

			for (int i = 0; i < humanLabels.length; i++) {
				if (humanLabels[i] != null) {
					if (humanLabels[i].equals(removeIcon)) {
						humanLabels[i].setIcon(null);
						return;
					}
				}
			}
		}
	}

	/**
	 * When both cards (computer and human) have been played, checks for a winner,
	 * updates scores, and resets the playing area
	 */
	private static void checkWinner() {
		// make sure both cards have been played
		if (cardPlayed[0] == null || cardPlayed[1] == null)
			return;

		// if computer card is higher than players card
		if (Card.getSortRanking(cardPlayed[0]) > Card.getSortRanking(cardPlayed[1])) {
			// increment computers score
			score[0]++;
		} else {
			// increment players card
			score[1]++;
		}

		// Creates a delay of two seconds, so that the user can see the result of the
		// current round before scoring
		final int TWO_SECONDS = 2000;
		Timer timer = new Timer(TWO_SECONDS, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// reset both played cards
				for (int i = 0; i < NUM_PLAYERS; i++) {
					String playedCardText = "<html><div style='text-align: center;'>" + playerNames[i] + "<br/>Score: "
							+ score[i];
					cardPlayed[i] = null;
					playedCardLabels[i].setText(playedCardText);
					playedCardLabels[i].setIcon(null);
					playedCardLabels[i].setBorder(new EmptyBorder(0, 0, 20, 0));
					playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
					playedCardLabels[i].setVerticalAlignment(JLabel.BOTTOM);
				}
				
				// make the cards clickable again
				cardsClickable = true;
				
				// alternate between the computer playing first and the human playing first
				if (computerPlaysFirst)
					computerPlaysFirst = false;
				else {
					computerPlaysFirst = true;
					
					// create a small delay between the round ending and the computer playing
					int QUARTER_SECOND = 250;
					Timer computerPlayTimer = new Timer(QUARTER_SECOND, new ActionListener() 
					{
						public void actionPerformed(ActionEvent e) 
						{
							computerPlayCard();
						}
					});
					
					// start the timer
					computerPlayTimer.setRepeats(false);
					computerPlayTimer.start();
				}
			}
		});
		
		// start the timer
		timer.setRepeats(false);
		timer.start();
	}
}

class CardGameFramework {
	private static final int MAX_PLAYERS = 50;

	private int numPlayers;
	private int numPacks; // # standard 52-card packs per deck
							// ignoring jokers or unused cards
	private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
	private int numUnusedCardsPerPack; // # cards removed from each pack
	private int numCardsPerHand; // # cards to deal each player
	private Deck deck; // holds the initial full deck and gets
						// smaller (usually) during play
	private Hand[] hand; // one Hand for each player
	private Card[] unusedCardsPerPack; // an array holding the cards not used
										// in the game. e.g. pinochle does not
										// use cards 2-8 of any suit

	public CardGameFramework(int numPacks, int numJokersPerPack, int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
			int numPlayers, int numCardsPerHand) {
		int k;

		// filter bad values
		if (numPacks < 1 || numPacks > 6)
			numPacks = 1;
		if (numJokersPerPack < 0 || numJokersPerPack > 4)
			numJokersPerPack = 0;
		if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
			numUnusedCardsPerPack = 0;
		if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
			numPlayers = 4;
		// one of many ways to assure at least one full deal to all players
		if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack) / numPlayers)
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
	public CardGameFramework() {
		this(1, 0, 0, null, 4, 13);
	}

	public Hand getHand(int k) {
		// hands start from 0 like arrays

		// on error return automatic empty hand
		if (k < 0 || k >= numPlayers)
			return new Hand();

		return hand[k];
	}

	public Card getCardFromDeck() {
		return deck.dealCard();
	}

	public int getNumCardsRemainingInDeck() {
		return deck.getNumCards();
	}

	public void newGame() {
		int k, j;

		// clear the hands
		for (k = 0; k < numPlayers; k++)
			hand[k].resetHand();

		// restock the deck
		deck.init(numPacks);

		// remove unused cards
		for (k = 0; k < numUnusedCardsPerPack; k++)
			deck.removeCard(unusedCardsPerPack[k]);

		// add jokers
		for (k = 0; k < numPacks; k++)
			for (j = 0; j < numJokersPerPack; j++)
				deck.addCard(new Card('X', Card.Suit.values()[j]));

		// shuffle the cards
		deck.shuffle();
	}

	public boolean deal() {
		// returns false if not enough cards, but deals what it can
		int k, j;
		boolean enoughCards;

		// clear all hands
		for (j = 0; j < numPlayers; j++)
			hand[j].resetHand();

		enoughCards = true;
		for (k = 0; k < numCardsPerHand && enoughCards; k++) {
			for (j = 0; j < numPlayers; j++)
				if (deck.getNumCards() > 0)
					hand[j].takeCard(deck.dealCard());
				else {
					enoughCards = false;
					break;
				}
		}

		return enoughCards;
	}

	void sortHands() {
		int k;

		for (k = 0; k < numPlayers; k++)
			hand[k].sort();
	}

	Card playCard(int playerIndex, int cardIndex) {
		// returns bad card if either argument is bad
		if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
			// Creates a card that does not work
			return new Card('M', Card.Suit.SPADES);
		}

		// return the card played
		return hand[playerIndex].playCard(cardIndex);

	}

	boolean takeCard(int playerIndex) {
		// returns false if either argument is bad
		if (playerIndex < 0 || playerIndex > numPlayers - 1)
			return false;

		// Are there enough Cards?
		if (deck.getNumCards() <= 0)
			return false;

		return hand[playerIndex].takeCard(deck.dealCard());
	}

}
