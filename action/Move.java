package cluedo.action;

import java.util.List;
import java.util.Scanner;

import cluedo.Board;
import cluedo.Game;
import cluedo.Player;
import cluedo.Position;
import cluedo.tile.*;

public class Move extends Action {

	private Position newPosition;
	private Position oldPosition;
	private Board board;
	
	public Move(Game game, Player player, Board board, Position oldPosition) {
		super(game, player);
		this.oldPosition = oldPosition;
		this.board = board;
	}

	/**
	 * Asks user for coordinates and tries to set new position to given coords if it
	 * is a valid move.
	 */
	public void run(){
		Scanner sc = new Scanner(System.in);
		while(player.getRoll()!=0){
			System.out.println("Choose a direction to move [N, S, W, E] or X to stop moving.");
			String direction = sc.next();
			if(direction.equalsIgnoreCase("x")){
				return;
			}
			newPosition = moveDirection(direction);
			while(newPosition==null || !isValid()){
					System.out.println("Invalid Move.");
					System.out.println("Choose a direction to move. [N, S, W, E]");
					direction = sc.next();
					newPosition = moveDirection(direction);
			}
			board.move(oldPosition, newPosition);
			player.setRoll(player.getRoll()-1);
			oldPosition = newPosition;
			board.redraw();
		}
	}


	public Position moveDirection(String direction){
		direction = direction.toUpperCase();
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
		}
		return null;
	}

	/**
	 * checks if moving to new position is valid. (Can't move on rooms).
	 * @return boolean. true if valid move, otherwise false.
	 */
	public boolean isValid(){
		if(newPosition.row()>=game.getBoardArray().length || newPosition.row()<0){
			System.out.println("Please choose a valid position.");
			return false;
		}
		if(newPosition.col()>=game.getBoardArray()[0].length || newPosition.col()<0){
			System.out.println("Please choose a valid position.");
			return false;
		}
		
		if(!(game.getBoardArray()[newPosition.row()][newPosition.col()] instanceof FloorTile)) {
			return false;
		}
		
		if(game.getPlayerPositions()[newPosition.row()][newPosition.col()]!=null){
			System.out.println("There is already a player in this position!");
			return false;
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
