package token;

import java.io.Serializable;

public enum Colour {
	GREEN, BLUE, RED, PURPLE, BLACK, WHITE;
	
	public String toString(Colour input) {
		if(input == GREEN) {
			return "green";
		} else if (input == BLUE) {
			return "blue";
		} else if (input == RED) {
			return "red";
		} else if (input == PURPLE) {
			return "purple";
		} else if (input == BLACK) {
			return "black";
		} else if (input == WHITE) {
			return "white";
		} else {
			return "yellow";
		}
	}
}
