package cluedo.action;

import java.util.ArrayList;
import java.util.List;

import cluedo.Game;
import cluedo.Input;
import cluedo.Player;
import cluedo.cards.Card;
import cluedo.cards.Character;
import cluedo.cards.Weapon;

public class Guess extends Action {
	private List<Card> guess = new ArrayList<Card>();

	public Guess(Game game, Player player) {
		super(game, player);
		run();
	}

	public void run(){
		String weapon = Input.getLine("Guess a Weapon: [Dagger, Revolver, Candlestick, Rope, Spanner, Leadpipe] ");
		Weapon weaponGuess = validWeapon(weapon);
		while(weaponGuess==null){
			System.out.println("Invalid Weapon.");
			weapon = Input.getLine("Guess a Weapon: [Dagger, Revolver, Candlestick, Rope, Spanner, Leadpipe] ");
			weaponGuess = validWeapon(weapon);
		}

		String character = Input.getLine("Guess a Character: [Miss Scarlett, Colonel Mustard, Mrs. White, The Reverand Green, Mrs. Peacock, Professor Plum]");
		Character charGuess = validChar(character);
		while(charGuess==null){
			System.out.println("Invalid Character.");
			character = Input.getLine("Guess a Character: [Miss Scarlett, Colonel Mustard, Mrs. White, The Reverand Green, Mrs. Peacock, Professor Plum]");
			charGuess = validChar(character);
		}
		guess.add(player.getRoom());
		guess.add(weaponGuess);
		guess.add(charGuess);
		
		
		
		for(Player p : game.getPlayers()) {
			if(p.equals(game.getCurrentPlayer())){continue;}
			for(Card c : p.getHand()) {
				for(Card card : guess) {
					if(card.equals(c)) {
						System.out.println(p.getName()+" has one of the cards\n");
					}
				}
			}
		}
	}

	private Weapon validWeapon(String guess) {
		for(Weapon w: game.getWeapons()){
			if(w.getName().toLowerCase().equals(guess.toLowerCase())){return w;}
		}
		return null;
	}

	private Character validChar(String guess) {
		for(Character c: game.getCharacters()){
			if(c.getName().toLowerCase().equals(guess.toLowerCase())){return c;}
		}
		return null;
	}

	public List<Card> getCards() {
		return guess;
	}

	public boolean isValid() {
		return true;
	}

	public boolean endsTurn(){
		return true;
	}

}
