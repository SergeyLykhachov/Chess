package com.yahoo.slykhachov.chess.util;
import com.yahoo.slykhachov.chess.Move;
public class PositionTranslator {
	public static String translate(String s) {
		char c1 = s.charAt(0);
		char c2 = s.charAt(1);
		if (c1 < 'a' || c1 > 'h' || c2 < '1' || c2 > '8' || s.length() != 2) {
			throw new IllegalArgumentException("Chars out of range");
		}
		int column;
		switch (c1) {
			case 'a':
				column = 0;
				break;
			case 'b':
				column = 1;
				break;
			case 'c':
				column = 2;
				break;
			case 'd':
				column = 3;
				break;
			case 'e':
				column = 4;
				break;
			case 'f':
				column = 5;
				break;
			case 'g':
				column = 6;
				break;
			case 'h':
				column = 7;
				break;
			default:
				throw new IllegalArgumentException();
		}
		int row;
		switch (c2) {
			case '1':
				row = 7;
				break;
			case '2':
				row = 6;
				break;
			case '3':
				row = 5;
				break;
			case '4':
				row = 4;
				break;
			case '5':
				row = 3;
				break;
			case '6':
				row = 2;
				break;
			case '7':
				row = 1;
				break;
			case '8':
				row = 0;
				break;
			default:
				throw new IllegalArgumentException();
		}
		return String.valueOf(row) + String.valueOf(column);
	}
	static String translateBack(Move m) {
		String s = m.toString();
		StringBuilder sb = new StringBuilder();;
		sb.append(s.substring(0, 4));
		sb.append(translateLetter(s.charAt(6)));
		sb.append(translateNumber(s.charAt(4)));
		sb.append(" -> (");
		sb.append(translateLetter(s.charAt(13)));
		sb.append(translateNumber(s.charAt(11)));
		return sb.toString();		
	}
	private static String translateLetter(char c) {
		String s;
		switch (c) {
			case '0':
				s = "a ";
				break;
			case '1':
				s = "b ";
				break;
			case '2':
				s = "c ";
				break;
			case '3':
				s = "d ";
				break;
			case '4':
				s = "e ";
				break;
			case '5':
				s = "f ";
				break;
			case '6':
				s = "g ";
				break;
			case '7':
				s = "h ";
				break;
			default:
				throw new IllegalArgumentException();
		}
		return s;
	}
	private static String translateNumber(char c) {
		String s;
		switch(c) {
			case '0':
				s = "8)";
				break;
			case '1':
				s = "7)";
				break;
			case '2':
				s = "6)";
				break;
			case '3':
				s = "5)";
				break;
			case '4':
				s = "4)";
				break;
			case '5':
				s = "3)";
				break;
			case '6':
				s = "2)";
				break;
			case '7':
				s = "1)";
				break;
			default:
				throw new IllegalArgumentException();
		}
		return s;
	}
}
