package comp_main;

public class Token {

	public String lexeme;
	public int token;
	public int attribute;

	public Token(String lex, int tok, int att) {
		lexeme = lex;
		token = tok;
		attribute = att;
	}

}
