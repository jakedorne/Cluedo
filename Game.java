package cluedo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import cluedo.action.Accuse;
import cluedo.action.Action;
import cluedo.action.End;
import cluedo.action.Enter;
import cluedo.action.Guess;
import cluedo.action.Hand;
import cluedo.action.Leave;
import cluedo.action.Move;
import cluedo.action.Stairs;
import cluedo.cards.Card;
import cluedo.cards.Character;
import cluedo.cards.Room;
import cluedo.cards.Weapon;
import cluedo.gui.Frame;
import cluedo.gui.OptionsPanel;
import cluedo.tile.DoorTile;
import cluedo.tile.Tile;

public class Game {

	

	private Board board; // the board for the game
	private List<Player> players; // all players in the game (including players who have lost
	private Player currentPlayer; // player who is having their turn
	
	private List<Card> envelope = new ArrayList<Card>(); // the cards of the murderer

	private List<Room> rooms = new ArrayList<Room>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	private List<Character> characters = new ArrayList<Character>();
	
	private Frame frame;


	public Game(Frame frame) {
		this.frame = frame;
		players = setupGame(); // start the game
		if(players == null) {
			System.exit(0);
		}
		currentPlayer = players.get(0); // the first player to play is player 1
		currentPlayer.roll();
		//construct cards
		//weapons
		weapons.add(new Weapon("Dagger"));
		weapons.add(new Weapon("Revolver"));
		weapons.add(new Weapon("Lead Pipe"));
		weapons.add(new Weapon("Rope"));
		weapons.add(new Weapon("Spanner"));
		weapons.add(new Weapon("Candlestick"));
		//rooms
		rooms.add(new Room("Kitchen", null, 'K', 'U'));
		rooms.add(new Room("Ball Room", null, 'B', 'T'));
		rooms.add(new Room("Conservatory", null, 'C', 'R'));
		rooms.add(new Room("Billiard Room", null, 'I', 'Q'));
		rooms.add(new Room("Library", null, 'A', 'P'));
		rooms.add(new Room("Study", null, 'S', 'Z'));
		rooms.add(new Room("Hall", null, 'H', 'Y'));
		rooms.add(new Room("Lounge", null, 'L', 'W'));
		rooms.add(new Room("Dining Room", null, 'D', 'V'));
		rooms.add(new Room ("Swimming Pool", null, 'X', 'G'));

		rooms.get(0).setConnectedTo(rooms.get(5));
		rooms.get(5).setConnectedTo(rooms.get(0));
		rooms.get(2).setConnectedTo(rooms.get(7));
		rooms.get(7).setConnectedTo(rooms.get(2));


		//characters
		characters.add(new Character("Miss Scarlett"));
		characters.add(new Character("Colonel Mustard"));
		characters.add(new Character("Mrs. White"));
		characters.add(new Character("The Reverend Green"));
		characters.add(new Character("Mrs. Peacock"));
		characters.add(new Character("Professor Plum"));
		
		// deal cards to players and create murderer
		distributeCards();
		//set up the board
		board = new Board(players,this);
	}
	
	
	public List<Player> setupGame() {
		int amount = 0;
		// repeat until a correct number is entered  
		while(amount < 3 || amount > 6) {
			//player number input dialog
			String inputValue = (String)JOptionPane.showInputDialog(null, "Enter amount of players (3-6): ", "Cluedo", JOptionPane.QUESTION_MESSAGE, null, null, null);
			//number not entered, end game
			if(inputValue == null) {
				return null;
			}
			// check for invalid input (ie not and integer)
			try {
				amount = Integer.parseInt(inputValue);
			}catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter an integer.");
			}
		}
		List<Player> players = new ArrayList<Player>();
		for(int i = 1; i < amount+1; i++) {
			// get name for each player via dialog
			String name = JOptionPane.showInputDialog("Enter player "+i+"'s name: ");
			if(name == null){return null;}
			//shouldn't be able to enter the same name as another player
			for(Player p: players) {
				if(p.getName().equals(name)) {
					while(p.getName().equals(name)) {
						JOptionPane.showMessageDialog(null, "Name already in use!\nSelect a different name.");
						name = (String)JOptionPane.showInputDialog(null, "Enter new name: ", "Cluedo", JOptionPane.QUESTION_MESSAGE, null, null, null);
						if(name == null){return null;}
					}
				}
			}
			players.add(new Player(name, this, i+""));
		}
		return players;
	}	
	
	/**
	 * runs the Action 
	 * 
	 * @param act
	 * @return action that was ran or null if the action wasn't valid
	 */
	private Action doAction(Action act) {
		if(act.isValid()) {
			act.run();
			return act;
		}
		return null;

	}
	
	
	public void diceRoll() {
		int roll = currentPlayer.roll();
		frame.getOptions().getTextArea().append("You rolled a "+roll+"\n");
	}


	/**
	 * Set currentPlayer to be the next player to have a turn
	 *
	 * @return the next player
	 */
	public Player nextPlayer() {
		int index = players.indexOf(currentPlayer);
		if(index == players.size()-1) {
			return players.get(0);
		}
		else {
			return players.get(index+1);
		}
	}

	/**
	 * find and return the player to have a turn after the given player
	 * 
	 * @param player
	 * @return next player
	 */
	public Player nextPlayer(Player player) {
		int index = players.indexOf(player);
		if(index == players.size()-1) {
			return players.get(0);
		}
		else {
			return players.get(index+1);
		}
	}


    /**
     * Shuffles cards randomly and distributes to all players as well as deciding the winning 3 cards.
     *
     */
	public void distributeCards(){
		ArrayList<Card> allcards = new ArrayList<Card>();

		Collections.shuffle(rooms);
		Collections.shuffle(weapons);
		Collections.shuffle(characters);

		envelope.add(rooms.get(0));
		envelope.add(weapons.get(0));
		envelope.add(characters.get(0));

		allcards.addAll(rooms.subList(1, rooms.size()-1));
		allcards.addAll(weapons.subList(1, weapons.size()-1));
		allcards.addAll(characters.subList(1, characters.size()-1));
		Player p = currentPlayer;
		for(Card c: allcards){
			p.addCard(c);
			p = nextPlayer(p);
		}
	}
	
	public void doorClicked(Tile tile) {
		DoorTile door = (DoorTile) tile;
		if(currentPlayer.getRoom()!=null && currentPlayer.getRoom().equals(door.getRoom())){
			Leave leave = new Leave(this,currentPlayer, door);
			leave.run();
			//movePlayer(currentPlayer, currentPlayer.getCurrentPosition(), tile.getPosition());
		} else{Output.appendText("up2 g");}
		
	}
	
	
	/**
	 * Attempts to move player when a direction key is detected by making a
	 * new move object and checking validity of move.
	 * @param dir - direction detected by the keyListener in the Frame class
	 */
	public void moveDetected(String dir){
		if(currentPlayer.getRoll()>0){
			Move move = new Move(this, currentPlayer, board);
			move.setNewPosition(move.moveDirection(dir));
			if(move.isValidMove()){
				movePlayer(currentPlayer, move.getOldPosition(), move.getNewPosition());
				currentPlayer.setRoll(currentPlayer.getRoll()-1);
				if(board.getBoard()[currentPlayer.getCurrentPosition().row()][currentPlayer.getCurrentPosition().col()] instanceof DoorTile){
					Enter ent = new Enter(this, currentPlayer);
					ent.run();
				}				
			}
		} else{Output.appendText("You have no moves left!");}

	}
	
	public void movePlayer(Player player, Position oldPos, Position newPos){
		frame.movePlayer(player, oldPos, newPos);
		board.move(oldPos, newPos);
	}
	
	public void guess(String character, String weapon) {
		List<Card> guess = new ArrayList<Card>();
		for(Character c : characters) {
			if(c.getName().equals(character)) {
				guess.add(c);
			}
		}
		for(Weapon w : weapons) {
			if(w.getName().equals(weapon)) {
				guess.add(w);
			}
		}
		guess.add(rooms.get(0));
		boolean hasCards = false;
		for(Player p : players) {
			if(p.equals(currentPlayer)){continue;}
			for(Card c : p.getHand()) {
				for(Card card : guess) {
					if(card.equals(c)) {
						hasCards = true;
						OptionsPanel panel = frame.getOptions();
						Output.appendText("Player "+p.getName()+" has one (or more) of the cards\n");
					}
				}
			}
		}
		if(!hasCards){Output.appendText("No one has any of the cards");}
	}


	/**
	 * Check to see if a guess was correct.
	 *
	 * @param guess - the cards that the player guessed.
	 * @return
	 */
	public boolean checkGuess(List<Card> guess) {
		return envelope.containsAll(guess);
	}
	
	
	
	/*
	 * Getters and setters
	 */

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
	}

	public List<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Tile[][] getBoardArray() {
		return board.getBoard();
	}

	public Board getBoard() {
		return board;
	}

	public Player[][] getPlayerPositions() {
		return board.getPlayerPositions();
	}








}
