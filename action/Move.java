package cluedo.action;

import java.util.Scanner;

import cluedo.Board;
import cluedo.Game;
import cluedo.Player;
import cluedo.Position;

public class Move extends Action {

	private Position newPosition;
	private Position oldPosition;
	
	public Move(Game game, Player player, Position oldPosition, Position newPosition) {
		super(game, player);
		this.newPosition = newPosition;
		this.oldPosition = oldPosition;
	}
	
	public Move(Game game, Player currentPlayer) {
		super(game, currentPlayer);
		setup();
	}


	/**
	 * Asks user for coordinates and tries to set new position to given coords if it
	 * is a valid move.
	 */
	public void setup(){
		Scanner sc = new Scanner(System.in);
		oldPosition = player.getCurrentPosition();
		System.out.println("Choose a direction to move. [N, S, W, E]");
		String direction = sc.next();
		newPosition = moveDirection(direction);
		System.out.println("new pos: "+newPosition);
		while(newPosition==null || !player.isValidMove(newPosition)){
				System.out.println("Invalid Move.");
				System.out.println("Choose a direction to move. [N, S, W, E]");
				direction = sc.next();
				newPosition = moveDirection(direction);
		}
	}
	
	public Position moveDirection(String direction){
		switch(direction){
			case  "N": 
				return new Position(player.getCurrentPosition().row()-1, player.getCurrentPosition().col());			
			case  "S": 
				return new Position(player.getCurrentPosition().row()+1, player.getCurrentPosition().col());	
			case  "W": 
				return new Position(player.getCurrentPosition().row(), player.getCurrentPosition().col()-1);		
			case  "E": 
				return new Position(player.getCurrentPosition().row(), player.getCurrentPosition().col()+1);	
			default: 
				System.out.println("Invalid direction.");
				newPosition = null;
				setup();
		}
		return null;
	}
	
	/**
	 * checks if moving to new position is valid. (Can't move on rooms).
	 * @return boolean. true if valid move, otherwise false.
	 */
	public boolean isValid(){
		System.out.println("NEW PLAYER POSITION: "+newPosition.toString());
		if(!player.isValidMove(newPosition)){return false;}
		if(game.getBoard()[newPosition.row()][newPosition.col()]=='X'){
			System.out.println("Cannot move on X positions.");
			return false;
		}
		if(game.getPlayerPositions()[newPosition.row()][newPosition.col()]!=null){
			System.out.println("There is already a player in this position!");
		}
		return true;
	}

	public Position getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(Position newPosition) {
		this.newPosition = newPosition;
	}

	public Position getOldPosition() {
		return oldPosition;
	}

	public void setOldPosition(Position oldPosition) {
		this.oldPosition = oldPosition;
	}
	

}
