
package comp_main;

public class SynchSets {

    public static final int ID = 10;
    public static final int PROGRAM = 20;
    public static final int OF = 21;
    public static final int ARRAY = 22;
    public static final int VAR = 23;
    public static final int STANDARD_TYPE = 24;
    public static final int FUNCTION = 26;
    public static final int PROCEDURE = 27;
    public static final int BEGIN = 28;
    public static final int END = 29;
    public static final int IF = 30;
    public static final int THEN = 31;
    public static final int ELSE = 32;
    public static final int WHILE = 33;
    public static final int DO = 34;
    public static final int NOT = 35;
    public static final int ADDOP = 36;
    public static final int MULOP = 37;
    public static final int RELOP = 38;
    public static final int ASSIGNOP = 39;
    public static final int COLON = 40;
    public static final int SEMI_C = 41;
    public static final int COMMA = 42;
    public static final int L_PAREN = 43;
    public static final int R_PAREN = 44;
    public static final int L_BRACK = 45;
    public static final int R_BRACK = 46;
    public static final int DOT = 47;
    public static final int TWO_DOT = 48;
    public static final int EOF = 49;
    public static final int NUM = 50;
    public static final int CALL = 72;
    public static final int LEXERR = 99;

    public static int[] program = {};
    public static int[] id_list = {R_PAREN};
    public static int[] declarations = {BEGIN, PROCEDURE};
    public static int[] type = {SEMI_C, R_PAREN};
    public static int[] standard_type = {SEMI_C, R_PAREN};
    public static int[] subprogram_declarations = {BEGIN};
    public static int[] subprogram_declaration = {SEMI_C};
    public static int[] subprogram_head = {PROCEDURE, BEGIN, VAR};
    public static int[] arguments = {SEMI_C};
    public static int[] parameter_list = {R_PAREN};
    public static int[] compound_statement = {DOT, SEMI_C, ELSE, END};
    public static int[] optional_statements = {END};
    public static int[] statement_list = {END};
    public static int[] statement = {END, ELSE, SEMI_C};
    public static int[] variable = {ASSIGNOP};
    public static int[] procedure_statement = {END, ELSE, SEMI_C};
    public static int[] expression_list = {R_PAREN};
    public static int[] expression = {R_PAREN, THEN, DO, R_BRACK, END, ELSE, SEMI_C, COMMA};
    public static int[] sign = {ID, NUM, L_PAREN, NOT};
    public static int[] factor = {MULOP, ADDOP, R_PAREN, THEN, DO, R_BRACK, END, ELSE, SEMI_C, RELOP, COMMA};
    public static int[] term = {RELOP, ADDOP, R_PAREN, THEN, DO, R_BRACK, END, ELSE, SEMI_C, COMMA};
    public static int[] simple_expression = { RELOP, R_PAREN, THEN, DO, R_BRACK, END, ELSE, SEMI_C, COMMA};

    public static boolean isInSynchSet(int tok, int[] synchset) {
        if(tok == EOF) {
            return true;
        }
        for(int i = 0; i < synchset.length; i++) {
            if (tok == synchset[i]) {
                return true;
            }
        }

        return false;
    }
}
