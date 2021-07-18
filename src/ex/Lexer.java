package ex;

import java.util.ArrayList;

public class Lexer {

	static Integer transitions[][] = {
			//             espace  lettre chiffre   [     ]      autre
			/*  0 */    {      0,     1,    2,    106,  107,     null },
			/*  1 */    {    201,     1,    1,    201,  201,     null },
			/*  2 */    {    202,   202,    2,    202,  202,     null },


			// 106 accepte [                        (goBack : non)
			// 107 accepte ]                        (goBack : non)


			// 201 accepte identifiant ou mot clé   (goBack : oui)
			// 202 accepte entier                   (goBack : oui)


	};

	static final int ETAT_INITIAL = 0;

	private int indiceSymbole(Character c) {
		if (c == null) return 0;
		if (Character.isWhitespace(c)) return 0;
		if (Character.isLetter(c)) return 1;
		if (Character.isDigit(c)) return 2;
		if (c == '[') return 3;
		if (c == ']') return 4;

		return 5;
	}

	public ArrayList<Token> lexer(SourceReader sr) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		String buf="";
		int etat = ETAT_INITIAL;
		while (true) {
			Character c = sr.lectureSymbole();

			Integer e = transitions[etat][indiceSymbole(c)];
			if (e == null) {
				System.out.println(" pas de transition depuis état " + etat + " avec symbole " + c);
				return new ArrayList<Token>(); // renvoie une liste vide
			}
			if (e >= 100) {

				 if (e == 106) {
 					tokens.add(new Token(TokenClass.leftPar));
				} else if (e == 107) {
					tokens.add(new Token(TokenClass.rightPar));

				} else if (e == 201) {

					 if(buf.equals("forward")) {
						 tokens.add(new Token(TokenClass.nForward, buf));

					 } else if (buf.equals("left")) {
						 tokens.add(new Token(TokenClass.nLeft, buf));

					 }else if (buf.equals("right"))
					 {
						 tokens.add(new Token(TokenClass.nRight, buf));
					 }
					 else if (buf.equals("procedure")) {
						 tokens.add(new Token(TokenClass.nProc, buf));

					 }else if (buf.equals("color")) {
						 tokens.add(new Token(TokenClass.nColor, buf));

					 }else if (buf.equals("repeat")){
						 tokens.add(new Token(TokenClass.nRepeat, buf));

					 }else if (buf.equals("call")) {
						 tokens.add(new Token(TokenClass.nCall, buf));

					 }else {
						     tokens.add(new Token(TokenClass.ident, buf));
					 }
					sr.goBack();

				  } else if (e == 202) {
					tokens.add(new Token(TokenClass.intVal, buf));
					sr.goBack();
				}
				etat = 0;
				buf = "";
			} else {
				etat = e;
				if (etat>0) buf = buf + c;
			}
			if (c==null) break;
		}
		return tokens;
	}

}
