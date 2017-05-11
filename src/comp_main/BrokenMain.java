//package comp_main;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Main {
//
//    public static File fileIn;
//    public static File fileOut;
//    public static File resIn;
//    public static File tokOut;
//    public static File symOut;
//    public static Scanner scan;
//    public static Scanner resScan;
//    public static PrintWriter pw;
//    public static PrintWriter tokpw;
//    public static PrintWriter sympw;
//    public static char[] c_buffer;
//    public static ArrayList<Token> reserved_words;
//    public static int lineNumber;
//    public static int f_p = 0;
//    public static int b_p = 0;
//    public static String[][] token_defs;
//    public static ArrayList<String> id_list;
//    public static String errorMessages = "";
//    public static Token tok;
//    public static boolean EOF_FOUND;
//    public static boolean EOF_REACHED;
//    public static boolean tok_reported;
//    public static ColoredNode forwardEye;
//    public static ColoredNode eye;
//    public static SymbolStack greenNodeStack;
//    public static int offset;
//    public static int counter;
//    public static int level;
//
//    public static void main(String[] args) {
//        int argsLength = args.length;
//        String inFile = args[0];
//        String outFile = "sample_output_new1.pas";
//        if (argsLength > 1) {
//            outFile = args[1];
//        }
//        String inRes = "reserved_words.txt";
//        if (argsLength > 2) {
//            inRes = args[2];
//        }
//        String outTok = "token_file_new1.txt";
//        if (argsLength > 3) {
//            outTok = args[3];
//        }
//        init(inFile, outFile, inRes, outTok);
//        // while(scan.hasNextLine()) {
//        parse();
//        pw.flush();
//        // getNextLine();
//        // }
//        // printToken(new Token("EOF", 49, 0));
//		/*
//		 * int numberofSpaces = 3 - (int) Math.log10(lineNumber); String
//		 * beforeLine = ""; for (int i = 0; i < numberofSpaces; i++) {
//		 * beforeLine = beforeLine + " "; } beforeLine = beforeLine + lineNumber
//		 * + ". "; pw.write(beforeLine + "EOF");
//		 */
//        // pw.flush();
//    }
//
//    public static void popScopeStack(){
//        eye = greenNodeStack.node;
//        forwardEye = eye.next;
//        greenNodeStack = greenNodeStack.previous;
//    }
//
//    public static void init(String inF, String outF, String inR, String outT) {
//        int a = 1;
//        int b = a;
//        greenNodeStack = new SymbolStack();
//        EOF_FOUND = false;
//        EOF_REACHED = false;
//        EOF_REACHED = false;
//        tok_reported = false;
//        setupTokenDefs();
//        fileIn = new File(inF);
//        fileOut = new File(outF);
//        resIn = new File(inR);
//        tokOut = new File(outT);
//        symOut = new File("SymbolTable.txt");
//        try {
//            scan = new Scanner(fileIn);
//            resScan = new Scanner(resIn);
//            pw = new PrintWriter(fileOut);
//            tokpw = new PrintWriter(tokOut);
//            sympw = new PrintWriter(symOut);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        c_buffer = new char[72];
//        lineNumber = 1;
//        reserved_words = new ArrayList<Token>();
//        id_list = new ArrayList<String>();
//        while (resScan.hasNextLine()) {
//            String line = resScan.nextLine();
//            String[] parts = line.split(",");
//            Token temptok = new Token(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
//            reserved_words.add(temptok);
//        }
//        tokpw.write("Line No.\t\tLexeme\t\t\t\t\t\tTOKEN-TYPE\t\t\t\tATTRIBUTE\n");
//
//        String temp = scan.nextLine();
//        temp = temp + "\n";
//        if (temp.length() > 72) {
//            temp = temp.substring(0, 72);
//        }
//        c_buffer = temp.toCharArray();
//        f_p = 0;
//        b_p = 0;
//    }
//
//
//
//    public static ColoredNode checkAddGreenNode(String lex, Type type){
//        ColoredNode current = eye;
//        if(current != null){
//            while(current.previous != null){
//                if(current.isGreen){
//                    if(lex.equals(current.id)){
//                        errorMessages += "SEMERR:\tReuse of scope id " + lex + "\n";
//                        return null;
//                    }
//                }
//                current = current.previous;
//            }
//        }
//        forwardEye = new ColoredNode(true, lex, type, 0, 0, eye);
//        if(eye != null){
//            eye.next = forwardEye;
//        }
//        eye = forwardEye;
//        forwardEye = eye.content;
//
//        SymbolStack push = new SymbolStack();
//        push.node = eye;
//        push.previous = greenNodeStack;
//        greenNodeStack = push;
//
//        return eye;
//
//    }
//
//    public static void checkAddBlueNode(String lex, Type type, int offset){
//        ColoredNode current = eye;
//        while(!current.isGreen){
//            if(lex.equals(current.id)){
//                errorMessages += "SEMERR:\tReuse of id: " + lex + "\n";
//                return;
//            } else {
//                current = current.previous;
//            }
//        }
//        forwardEye = new ColoredNode(false, lex, type, offset, 0, eye);
//        if(eye.isGreen){
//            eye.content = forwardEye;
//        } else {
//            eye.next = forwardEye;
//        }
//        eye = forwardEye;
//        forwardEye = eye.next;
//    }
//
//    public static void symbolWriter(){
//        ColoredNode current = eye;
//
//        while(current.previous != null){
//            System.out.println("Previous: " + current.previous.id);
//            current = current.previous;
//        }
//        int level = 0;
//        printLine(level, current);
//        sympw.flush();
//    }
//
//    public static void printLine(int level, ColoredNode node){
//        if(node.isGreen){
//            System.out.println("Level: " + level);
//            for(int i = 0; i < level; i++){
//                sympw.write("\t");
//            }
//            sympw.write("SCOPE: ");
//            sympw.write("[id: " + node.id + " type: " + node.type.type + " # params: " + node.numParams + "]\n");
//            //System.out.println("Content " + node.content);
//            if(node.content != null){
//                printLine(level + 1, node.content);
//            }
//            if(node.next != null){
//                printLine(level, node.next);
//            }
//        } else {
//            for(int i = 0; i < level; i++){
//                sympw.write("\t");
//            }
//            sympw.write("VAR: [id: " + node.id + " type: " + node.type.type + " offset: " + node.offset + "]\n");
//            if(node.next != null){
//                printLine(level, node.next);
//            }
//        }
//    }
//
//    public static void setNumParams(int count){
//        greenNodeStack.node.numParams = count;
//    }
//
//    public static Type getType(String lex){
//        ColoredNode current = eye;
//        while(current.previous != null){
//            if(lex.equals(current.id)){
//                return current.type;
//            } else {
//                current = current.previous;
//            }
//        }
//        errorMessages += "SEMERR:\tUse of undeclared identifier: " + lex + "\n";
//        return new Type("ERR");
//    }
//
//    public static ColoredNode getProcPointer(String lex){
//        ColoredNode current = eye;
//        while(current.previous != null){
//            if(current.isGreen && current.id.equals(lex)){
//                return current;
//            }
//            current = current.previous;
//        }
//        errorMessages += "SEMERR:\tCould not find procedure: " + lex + "\n";
//        pw.write(errorMessages);
//        pw.flush();
//        return null;
//    }
//
//    public static void printToken(Token tok) {
//        tokpw.write(lineNumber + "\t\t\t\t" + tok.lexeme);
//        int tabs2write = 7 - tok.lexeme.length() / 4;
//        if (tabs2write > 0) {
//            for (int i = 0; i < tabs2write; i++) {
//                tokpw.write("\t");
//            }
//        }
//        String token2write = tok.token + " (" + token_defs[tok.token][0] + ")";
//        tokpw.write(token2write);
//        tabs2write = 6 - token2write.length() / 4;
//        if (tabs2write > 0) {
//            for (int i = 0; i < tabs2write; i++) {
//                tokpw.write("\t");
//            }
//        }
//        if (tok.token == 10) {
//            tokpw.write(tok.attribute + " (location: " + tok.attribute + ")\n");
//        } else {
//            tokpw.write(tok.attribute + " (" + token_defs[tok.token][tok.attribute + 1] + ")\n");
//        }
//        tokpw.flush();
//    }
//
//    public static void program() {
//        System.out.println("Program was called.");
//        switch (tok.token) {
//            case PROGRAM:
//                System.out.println("Program's token is " + token_defs[tok.token][0]);
//
//                System.out.println("Program in case PROGRAM.");
//
//                match(PROGRAM);
//                Token temp = tok;
//                match(ID);
//                checkAddGreenNode(temp.lexeme, new Type("PG_NAME"));
//                match(L_PAREN);
//                identifier_list();
//                match(R_PAREN);
//                match(SEMI_C);
//                program_tail();
//                break;
//            default:
//                System.out.println("Program's token in default is " + token_defs[tok.token][0]);
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected " + token_defs[PROGRAM][0] + " received " + token_defs[tok.token][0]
//                            + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.program) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void program_tail() {
//        System.out.println("Start of program tail" + tok.token);
//        offset = 0;
//        counter = 0;
//        switch (tok.token) {
//            case VAR:
//                declarations();
//                program_tail_tail();
//                break;
//            case PROCEDURE:
//                subprogram_declarations();
//                compound_statement();
//                match(DOT);
//                break;
//            case BEGIN:
//                compound_statement();
//                match(DOT);
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected one of " + token_defs[VAR][0] + " " + token_defs[PROCEDURE][0] + " "
//                            + token_defs[BEGIN][0] + " received " + token_defs[tok.token][0] + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.program) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void program_tail_tail() {
//        switch (tok.token) {
//            case PROCEDURE:
//                subprogram_declarations();
//                compound_statement();
//                match(DOT);
//                break;
//            case BEGIN:
//                compound_statement();
//                match(DOT);
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected one of " + token_defs[PROCEDURE][0] + " " + token_defs[BEGIN][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.program) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void identifier_list() {
//        switch (tok.token) {
//            case ID:
//                Token temp = tok;
//                match(ID);
//                checkAddBlueNode(temp.lexeme, new Type("PG_PARAM"), 0);
//                identifier_list_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected " + token_defs[ID][0] + " received " + token_defs[tok.token][0]
//                            + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.id_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void identifier_list_tail() {
//        switch (tok.token) {
//            case COMMA:
//                match(COMMA);
//                Token temp = tok;
//                match(ID);
//                checkAddBlueNode(temp.lexeme, new Type("PG_PARAM"), 0);
//                identifier_list_tail();
//                break;
//            case R_PAREN:
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected one of " + token_defs[COMMA][0] + " " + token_defs[R_PAREN][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.id_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void declarations() {
//        switch (tok.token) {
//            case VAR:
//                match(VAR);
//                Token temp = tok;
//                match(ID);
//                match(COLON);
//                Decoration dec = type();
//                checkAddBlueNode(temp.lexeme, dec.type, offset);
//                offset += dec.width;
//                match(SEMI_C);
//                declarations_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected " + token_defs[VAR][0] + " received " + token_defs[tok.token][0]
//                            + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.declarations) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void declarations_tail() {
//        switch (tok.token) {
//            case VAR:
//                match(VAR);
//                Token temp = tok;
//                match(ID);
//                match(COLON);
//                Decoration dec = type();
//                checkAddBlueNode(temp.lexeme, dec.type, offset);
//                offset += dec.width;
//                match(SEMI_C);
//                declarations_tail();
//                break;
//            case PROCEDURE:
//                break;
//            case BEGIN:
//                break;
//
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected one of " + token_defs[VAR][0] + " " + token_defs[PROCEDURE][0] + " "
//                            + token_defs[BEGIN][0] + " received " + token_defs[tok.token][0] + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.declarations) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static Decoration type() {
//        int arrayLen = 0;
//        boolean validArray = false;
//        switch (tok.token) {
//            case STANDARD_TYPE:
//                return standard_type();
//            case ARRAY:
//                match(ARRAY);
//                match(L_BRACK);
//                Token temp1 = tok;
//                match(NUM);
//                match(TWO_DOT);
//                Token temp2 = tok;
//                match(NUM);
//                match(R_BRACK);
//                if(temp1.token == NUM && temp2.token == NUM){
//                    System.out.println("Lexeme: "+temp1.lexeme + " Attribute: " + temp1.attribute);
//                    System.out.println("Matches with Lexeme: "+temp2.lexeme + " Attribute: " + temp2.attribute);
//                    if(temp1.attribute == 1 && temp2.attribute == 1){
//                        arrayLen = Integer.parseInt(temp2.lexeme) - Integer.parseInt(temp1.lexeme) + 1;
//                        System.out.println("Array len: " + arrayLen);
//                        validArray = true;
//                    } else if(temp1.attribute == 2 || temp1.attribute == 3 || temp2.attribute == 2 || temp2.attribute == 3){
//                        errorMessages += "SEMERR:\tAttempt to use real number for array length.\n";
//                    } else {
//                        errorMessages += "SEMERR:\tUnrecognized input for array length.\n";
//                    }
//                } else if(temp1.token != LEXERR && temp2.token != LEXERR){
//                    errorMessages += "SEMERR:\tAttempt to use non-number for array length.\n";
//                }
//                match(OF);
//                Decoration std_type = standard_type();
//                if(validArray){
//                    int width = arrayLen * std_type.width;
//                    System.out.println("Array type: " + std_type.type.type);
//                    if(std_type.type.isEquivalent(new Type("INT"))){
//                        return new Decoration(new Type("AREAL"), width);
//                    } else if(std_type.type.isEquivalent(new Type("REAL"))){
//                        return new Decoration(new Type("AREAL"), width);
//                    } else {
//                        errorMessages += "SEMERR:\tUnexpected type for array.\n";
//                        return new Decoration(new Type("ERR"), 0);
//                    }
//                } else {
//                    return new Decoration(new Type("ERR"), 0);
//                }
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected one of " + token_defs[STANDARD_TYPE][0] + " " + token_defs[ARRAY][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.type) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration standard_type() {
//        int att;
//        switch (tok.token) {
//            case STANDARD_TYPE:
//                att = tok.attribute;
//                match(STANDARD_TYPE);
//                if(att == 1){
//                    return new Decoration(new Type("INT"), 4);
//                } else {
//                    return new Decoration(new Type("REAL"), 8);
//                }
//            default:
//                if (tok.token != LEXERR && !tok_reported){
//                    tok_reported = true;
//                    errorMessages += "SYNERR: Expected " + token_defs[STANDARD_TYPE][0] + " received "
//                            + token_defs[tok.token][0] + "\n";
//                }
//
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.standard_type) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//    public static void subprogram_declarations() {
//        switch (tok.token) {
//            case PROCEDURE:
//                subprogram_declaration();
//                popScopeStack();
//                match(SEMI_C);
//                subprogram_declarations_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected " + token_defs[PROCEDURE][0] + " received "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_declarations) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void subprogram_declarations_tail() {
//        switch (tok.token) {
//            case PROCEDURE:
//                subprogram_declaration();
//                popScopeStack();
//                match(SEMI_C);
//                subprogram_declarations_tail();
//                break;
//            case BEGIN:
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[PROCEDURE][0] + " " + token_defs[BEGIN][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_declarations)) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void subprogram_declaration() {
//        switch (tok.token) {
//            case PROCEDURE:
//                subprogram_head();
//                setNumParams(counter);
//                subprogram_declaration_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected " + token_defs[PROCEDURE][0] + " received "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_declaration)) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void subprogram_declaration_tail() {
//        switch (tok.token) {
//            case VAR:
//                declarations();
//                subprogram_declaration_tail_tail();
//                break;
//            case PROCEDURE:
//                subprogram_declarations();
//                compound_statement();
//                break;
//            case BEGIN:
//                compound_statement();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[VAR][0] + " " + token_defs[PROCEDURE][0] + " "
//                            + token_defs[BEGIN][0] + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_declaration)) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void subprogram_declaration_tail_tail() {
//        switch (tok.token) {
//            case PROCEDURE:
//                subprogram_declarations();
//                compound_statement();
//                break;
//            case BEGIN:
//                compound_statement();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[PROCEDURE][0] + " " + token_defs[BEGIN][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_declaration)) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void subprogram_head() {
//        switch (tok.token) {
//            case PROCEDURE:
//                offset = 0;
//                counter = 0;
//                match(PROCEDURE);
//                Token temp = tok;
//                match(ID);
//                checkAddGreenNode(temp.lexeme, new Type("PROC"));
//                subprogram_head_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected " + token_defs[PROCEDURE][0] + " received "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_head) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void subprogram_head_tail() {
//        switch (tok.token) {
//            case L_PAREN:
//                arguments();
//                match(SEMI_C);
//                break;
//            case SEMI_C:
//                match(SEMI_C);
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[L_PAREN][0] + " " + token_defs[COLON][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.subprogram_head) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void arguments() {
//        switch (tok.token) {
//            case L_PAREN:
//                match(L_PAREN);
//                parameter_list();
//                match(R_PAREN);
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected " + token_defs[L_PAREN][0] + " received " + token_defs[tok.token][0]
//                            + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.arguments) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void parameter_list() {
//        switch (tok.token) {
//            case ID:
//                Token temp = tok;
//                match(ID);
//                match(COLON);
//                Decoration dec = type();
//                if(!dec.type.type.equals("ERR")){
//                    checkAddBlueNode(temp.lexeme, new Type("PP_"+dec.type.type), offset);
//                }
//                offset = offset + dec.width;
//                counter++;
//                parameter_list_tail();
//                break;
//            default:
//                if (tok.token != LEXERR) {
//                    errorMessages += "SYNERR: Expected " + token_defs[ID][0] + " received " + token_defs[tok.token][0]
//                            + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.parameter_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void parameter_list_tail() {
//        switch (tok.token) {
//            case SEMI_C:
//                match(SEMI_C);
//                Token temp = tok;
//                match(ID);
//                match(COLON);
//                Decoration dec = type();
//                if(!dec.type.type.equals("ERR")){
//                    checkAddBlueNode(temp.lexeme, new Type("PP_"+dec.type.type), offset);
//                }
//                offset = offset + dec.width;
//                counter++;
//                parameter_list_tail();
//                break;
//            case R_PAREN:
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[SEMI_C][0] + " " + token_defs[R_PAREN][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.parameter_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void compound_statement() {
//        switch (tok.token) {
//            case BEGIN:
//                match(BEGIN);
//                compound_statement_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected " + token_defs[BEGIN][0] + " received " + token_defs[tok.token][0]
//                            + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.compound_statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void compound_statement_tail() {
//        switch (tok.token) {
//            case BEGIN:
//                optional_statements();
//                match(END);
//                break;
//            case ID:
//                optional_statements();
//                match(END);
//                break;
//            case CALL:
//                optional_statements();
//                match(END);
//                break;
//            case IF:
//                optional_statements();
//                match(END);
//                break;
//            case WHILE:
//                optional_statements();
//                match(END);
//                break;
//            case END:
//                match(END);
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[BEGIN][0] + " " + token_defs[ID][0] + " "
//                            + token_defs[CALL][0] + " " + token_defs[IF][0] + " " + token_defs[WHILE][0] + " "
//                            + token_defs[END][0] + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.compound_statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void optional_statements() {
//        switch (tok.token) {
//            case BEGIN:
//                statement_list();
//                break;
//            case ID:
//                statement_list();
//                break;
//            case CALL:
//                statement_list();
//                break;
//            case IF:
//                statement_list();
//                break;
//            case WHILE:
//                statement_list();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[BEGIN][0] + " " + token_defs[ID][0] + " "
//                            + token_defs[CALL][0] + " " + token_defs[IF][0] + " " + token_defs[WHILE][0] + " received "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.optional_statements) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void statement_list() {
//        switch (tok.token) {
//            case BEGIN:
//                statement();
//                statement_list_tail();
//                break;
//            case ID:
//                statement();
//                statement_list_tail();
//                break;
//            case CALL:
//                statement();
//                statement_list_tail();
//                break;
//            case IF:
//                statement();
//                statement_list_tail();
//                break;
//            case WHILE:
//                statement();
//                statement_list_tail();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[BEGIN][0] + " " + token_defs[ID][0] + " "
//                            + token_defs[CALL][0] + " " + token_defs[IF][0] + " " + token_defs[WHILE][0] + " received "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.statement_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void statement_list_tail() {
//        switch (tok.token) {
//            case SEMI_C:
//                match(SEMI_C);
//                statement();
//                statement_list_tail();
//                break;
//            case END:
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages += "SYNERR: Expected one of " + token_defs[SEMI_C][0] + " " + token_defs[END][0]
//                            + " received " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.statement_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//
//        }
//    }
//
//    public static void statement() {
//        Decoration dec1;
//        Decoration dec2;
//        switch (tok.token) {
//            case ID:
//                dec1 = variable();
//                match(ASSIGNOP);
//                dec2 = expression();
//                if(!dec1.type.isEquivalent(dec2.type) && !dec1.type.type.equals("ERR") && !dec2.type.type.equals("ERR")){
//                    errorMessages += "SEMERR:\tAttempt to assign variable to incorrect type. " + "Type1:" + dec1.type.type + " Type2:" + dec2.type.type + "\n";
//                }
//                break;
//            case CALL:
//                procedure_statement();
//                break;
//            case BEGIN:
//                compound_statement();
//                break;
//            case IF:
//                match(IF);
//                dec1 = expression();
//                if(!dec1.type.isEquivalent(new Type("BOOL")) && !dec1.type.isEquivalent(new Type("ERR"))){
//                    errorMessages += "SEMERR:\tAttempt to use non-boolean expression in if statement.\n";
//                }
//                match(THEN);
//                statement();
//                statement_tail();
//                break;
//            case WHILE:
//                match(WHILE);
//                dec1 = expression();
//                if(!dec1.type.isEquivalent(new Type("BOOL")) && !dec1.type.isEquivalent(new Type("ERR"))){
//                    errorMessages += "SEMERR:\tAttempt to use non-boolean expression in while loop.\n";
//                }
//                match(DO);
//                statement();
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID, CALL, BEGIN, IF, WHILE  received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void statement_tail() {
//        switch (tok.token) {
//            case ELSE:
//                match(ELSE);
//                statement();
//                break;
//            case END:
//            case SEMI_C:
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ELSE, END, SEMI_C received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static Decoration variable() {
//        switch (tok.token) {
//            case ID:
//                Token temp = tok;
//                match(ID);
//                Type type = getType(temp.lexeme);
//                System.out.println("Lexeme: " + temp.lexeme + " Variable: " + type.type);
//                return variable_tail(new Decoration(type, 0));
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID  received: " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.variable) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration variable_tail(Decoration inh) {
//        switch (tok.token) {
//            case L_BRACK:
//                match(L_BRACK);
//                Decoration dec = expression();
//                match(R_BRACK);
//                if(dec.type.type.equals("INT") && (inh.type.isEquivalent(new Type("AINT")))){
//                    return new Decoration(new Type("INT"), 0);
//                } else if(dec.type.type.equals("INT") && inh.type.isEquivalent(new Type("AREAL"))){
//                    return new Decoration(new Type("REAL"), 0);
//                } else if(dec.type.type.equals("ERR") && inh.type.isEquivalent(new Type("ERR"))) {
//                    return new Decoration(new Type("ERR"), 0);
//                } else {
//                    errorMessages +="SEMERR:\tIncorrect array access.\n";
//                    return new Decoration(new Type("ERR"), 0);
//                }
//            case ASSIGNOP:
//                return inh;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected L_BRACK, ASSIGNOP  received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.variable) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static void procedure_statement() {
//        switch (tok.token) {
//            case CALL:
//                match(CALL);
//                Token temp = tok;
//                match(ID);
//                Type type = getType(temp.lexeme);
//                if(type.type.equals("PROC")){
//                    procedure_statement_tail(getProcPointer(temp.lexeme));
//                } else if(type.type.equals("ERR")){
//                    procedure_statement_tail(null);
//
//                } else {
//                    errorMessages += "SEMERR:\t" + temp.lexeme + " is ot a procedure.\n";
//                    procedure_statement_tail(null);
//                }
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected CALL received: " + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.procedure_statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static void procedure_statement_tail(ColoredNode inh) {
//        switch (tok.token) {
//            case L_PAREN:
//                match(L_PAREN);
//                if(inh == null){
//                    expression_list(0, null);
//                } else {
//                    int net_params = expression_list(inh.numParams, inh.content);
//                    if(net_params != 0){
//                        errorMessages += "SEMERR:\tIncorrect number of parameters in procedure call.\n";
//                    }
//                }
//                match(R_PAREN);
//                break;
//            case SEMI_C:
//            case ELSE:
//            case END:
//                if(inh != null && inh.numParams != 0){
//                    errorMessages += "SEMERR:\tIncorrect number of parameters in procedure call.\n";
//                }
//                break;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected L_PAREN, SEMI_C, END, ELSE received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.procedure_statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                break;
//        }
//    }
//
//    public static int expression_list(int numParams, ColoredNode inh) {
//        Decoration dec;
//        switch (tok.token) {
//            case ID:
//            case L_PAREN:
//            case NUM:
//            case ADDOP:
//            case NOT:
//                dec = expression();
//                if(inh == null || numParams >= 0){
//                    return expression_list_tail(numParams - 1, inh);
//                } else if(!dec.type.isEquivalent(inh.type)){
//                    errorMessages += "SEMERR:\tIncorrect type for parameter. " + dec.type.type + ' ' + inh.type.type + "\n";
//                }
//                return expression_list_tail(numParams - 1, inh.next);
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID, NUM, L_PAREN, NOT, ADDOP received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.procedure_statement) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return 0;
//        }
//    }
//
//    public static int expression_list_tail(int numParams, ColoredNode inh) {
//        switch (tok.token) {
//            case COMMA:
//                match(COMMA);
//                Decoration dec = expression();
//                if(inh == null || numParams >= 0){
//                    return expression_list_tail(numParams - 1, inh);
//                } else if (!dec.type.isEquivalent(inh.type)){
//                    errorMessages += "SEMERR:\tIncorrect type for parameter." + dec.type.type + ' ' + inh.type.type +  "\n";
//                }
//                return expression_list_tail(numParams - 1, inh.next);
//            case R_PAREN:
//                return 0;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected COMMA, R_PAREN, received: " + token_defs[tok.token][0]
//                            + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.expression_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return 0;
//        }
//    }
//
//    public static Decoration expression() {
//        Decoration dec;
//        switch (tok.token) {
//            case ID:
//            case NUM:
//            case L_PAREN:
//            case NOT:
//            case ADDOP:
//                dec = simple_expression();
//                return expression_tail(dec);
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID, NUM, L_PAREN, NOT, ADDOP received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.expression) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration expression_tail(Decoration inh) {
//        switch (tok.token) {
//            case RELOP:
//                match(RELOP);
//                Decoration dec = simple_expression();
//                if(dec.type.isEquivalent(inh.type)){
//                    return new Decoration(new Type("ERR"), 0);
//                } else if (dec.type.type.equals("ERR") || inh.type.type.equals("ERR")){
//                    return new Decoration(new Type("ERR"), 0);
//                } else {
//                    errorMessages += "SEMERR:\tIncompatible types for relop operations.\n";
//                    return new Decoration(new Type("ERR"), 0);
//                }
//
//            case COMMA:
//            case SEMI_C:
//            case ELSE:
//            case END:
//            case R_PAREN:
//            case R_BRACK:
//            case THEN:
//            case DO:
//                return inh;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID, NUM, L_PAREN, NOT received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.expression_list) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration simple_expression() {
//        Decoration dec;
//        switch (tok.token) {
//            case ID:
//            case NUM:
//            case L_PAREN:
//            case NOT:
//                dec = term();
//                return simple_expression_tail(dec);
//            case ADDOP:
//                sign();
//                dec = term();
//                if(!dec.type.type.equals("INT") && !dec.type.type.equals("REAL")){
//                    errorMessages += "SEMERR:\tAttempt to add sign to an unsigned type.\n";
//                    dec = new Decoration(new Type("ERR"), 0);
//                }
//                return simple_expression_tail(dec);
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID, NUM, L_PAREN, NOT, ADDOP received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.simple_expression) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration simple_expression_tail(Decoration inh) {
//
//        switch (tok.token) {
//            case ADDOP:
//                int op = tok.attribute;
//                match(ADDOP);
//                Decoration dec = term();
//                if(op == 2 || op == 3){
//                    if(dec.type.isEquivalent(new Type("INT")) && inh.type.isEquivalent(new Type("INT")) ){
//                        return new Decoration(new Type("INT"),4);
//                    } else if(dec.type.isEquivalent(new Type("REAL")) && inh.type.isEquivalent(new Type("REAL")) ){
//                        return new Decoration(new Type("REAL"),8);
//                    } else if(dec.type.isEquivalent(new Type("ERR")) && inh.type.isEquivalent(new Type("ERR")) ){
//                        return new Decoration(new Type("ERR"),0);
//                    } else {
//                        errorMessages += "SEMERR:\tIncompatible types for addop operation.\n";
//                        return new Decoration(new Type("ERR"), 0);
//                    }
//                } else if(op == 1){
//                    if(dec.type.type.equals("BOOL") && inh.type.type.equals("BOOL")){
//                        return dec;
//                    } else if(dec.type.type.equals("ERR") && inh.type.type.equals("ERR")){
//                        return new Decoration(new Type("ERR"), 0);
//                    } else {
//                        errorMessages += "SEMERR:\tIncompatible types for or operation.\n";
//                        return new Decoration(new Type("ERR"), 0);
//                    }
//                } else {
//                    errorMessages += "SEMERR:\tUnrecognized addop.\n";
//                    return new Decoration(new Type("ERR"), 0);
//                }
//            case COMMA:
//            case SEMI_C:
//            case ELSE:
//            case END:
//            case RELOP:
//            case R_PAREN:
//            case R_BRACK:
//            case THEN:
//            case DO:
//                return inh;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages
//                            + "SYNERR: Expected COMMA, SEMI_C, or ELSE, END, RELOP, ADDOP, MULOP, R_PAREN, R_BRACK, THEN, DO, received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.simple_expression) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration term() {
//        switch (tok.token) {
//            case NUM:
//            case NOT:
//            case L_PAREN:
//            case ID:
//                Decoration dec = factor();
//                return term_tail(dec);
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected NUM, NOT, L_PAREN, ID, received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.term) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration term_tail(Decoration inh) {
//        switch (tok.token) {
//            case MULOP:
//                int op = tok.attribute;
//                match(MULOP);
//                Decoration dec = factor();
//                switch(op){
//                    case 1:
//                        if(dec.type.isEquivalent(new Type("INT")) && inh.type.isEquivalent(new Type("INT")) ){
//                            return new Decoration(new Type("INT"),4);
//                        } else if(dec.type.isEquivalent(new Type("REAL")) && inh.type.isEquivalent(new Type("REAL")) ){
//                            return new Decoration(new Type("REAL"),8);
//                        } else if(dec.type.isEquivalent(new Type("ERR")) && inh.type.isEquivalent(new Type("ERR")) ){
//                            return new Decoration(new Type("ERR"),0);
//                        } else {
//                            errorMessages += "SEMERR:\tIncompatible types for mult operation.\n";
//                            return new Decoration(new Type("ERR"), 0);
//                        }
//                    case 2:
//                    case 3:
//                        if(dec.type.isEquivalent(new Type("INT")) && inh.type.isEquivalent(new Type("INT")) ){
//                            return new Decoration(new Type("INT"),4);
//                        } else if(dec.type.isEquivalent(new Type("REAL")) && inh.type.isEquivalent(new Type("REAL")) ){
//                            return new Decoration(new Type("REAL"),8);
//                        } else if(dec.type.isEquivalent(new Type("ERR")) && inh.type.isEquivalent(new Type("ERR")) ){
//                            return new Decoration(new Type("ERR"),0);
//                        } else {
//                            errorMessages += "SEMERR:\tIncompatible types for div operation.\n";
//                            return new Decoration(new Type("ERR"), 0);
//                        }
//                    case 4:
//                        if(dec.type.isEquivalent(new Type("INT")) && inh.type.isEquivalent(new Type("INT")) ){
//                            return new Decoration(new Type("INT"),4);
//                        } else if(dec.type.isEquivalent(new Type("REAL")) && inh.type.isEquivalent(new Type("REAL")) ){
//                            return new Decoration(new Type("REAL"),8);
//                        } else if(dec.type.isEquivalent(new Type("ERR")) && inh.type.isEquivalent(new Type("ERR")) ){
//                            return new Decoration(new Type("ERR"),0);
//                        } else {
//                            errorMessages += "SEMERR:\tIncompatible types for mod operation.\n";
//                            return new Decoration(new Type("ERR"), 0);
//                        }
//                    case 5:
//                        if(dec.type.isEquivalent(new Type("BOOL")) && inh.type.isEquivalent(new Type("BOOL")) ){
//                            return dec;
//                        } else if(dec.type.isEquivalent(new Type("ERR")) && inh.type.isEquivalent(new Type("ERR")) ){
//                            return new Decoration(new Type("ERR"),0);
//                        } else {
//                            errorMessages += "SEMERR:\tIncompatible types for AND operation.\n";
//                            return new Decoration(new Type("ERR"), 0);
//                        }
//                    default:
//                        errorMessages += "SEMERR:\tInvalid MULOP.\n";
//                        return new Decoration(new Type("ERR"), 0);
//                }
//            case COMMA:
//            case SEMI_C:
//            case ELSE:
//            case END:
//            case RELOP:
//            case ADDOP:
//            case R_PAREN:
//            case R_BRACK:
//            case THEN:
//            case DO:
//                return inh;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages
//                            + "SYNERR: Expected COMMA, SEMI_C, or ELSE, END, RELOP, ADDOP, MULOP, R_PAREN, R_BRACK, THEN, DO, received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.term) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static Decoration factor() {
//
//        switch (tok.token) {
//            case ID:
//                Token temp =tok;
//                match(ID);
//                Type t = getType(temp.lexeme);
//                return factor_tail(new Decoration(t, 0));
//            case NUM:
//                Decoration dec;
//                if(tok.attribute == 1){
//                    dec = new Decoration(new Type("INT"), 0);
//                } else {
//                    dec = new Decoration(new Type("REAL"), 0);
//                }
//                match(NUM);
//                return dec;
//            case L_PAREN:
//                match(L_PAREN);
//                dec = expression();
//                match(R_PAREN);
//                return dec;
//            case NOT:
//                match(NOT);
//                dec = factor();
//                if(dec.type.type.equals("BOOL")){
//                    return dec;
//                } else if(dec.type.type.equals("ERR")){
//
//                } else{
//                    errorMessages += "SEMERR:\tNot used with non-boolean expression.\n";
//                    return new Decoration(new Type("ERR"), 0);
//                }
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages + "SYNERR: Expected ID, NUM, L_PAREN, R_PAREN, NOT, received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.factor) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//
//    }
//
//    public static Decoration factor_tail(Decoration inh) {
//        switch (tok.token) {
//            case L_BRACK:
//                match(L_BRACK);
//                Decoration dec = expression();
//                Type temp = dec.type;
//                match(R_BRACK);
//                if(temp.type.equals("INT") && inh.type.type.equals("AINT")){
//                    return new Decoration(new Type("INT"),0);
//                } else if(temp.type.equals("REAL") && inh.type.type.equals("AREAL")){
//                    return new Decoration(new Type("REAL"), 0);
//                } else if(!inh.type.type.equals("AINT") && inh.type.type.equals("AREAL") && !inh.type.type.equals("ERR")){
//                    errorMessages += "SEMERR:\tArray reference is not an integer.\n";
//                    return new Decoration(new Type("ERR"), 0);
//                } else {
//                    return new Decoration(new Type("ERR"), 0);
//                }
//            case COMMA:
//            case SEMI_C:
//            case ELSE:
//            case END:
//            case RELOP:
//            case ADDOP:
//            case MULOP:
//            case R_PAREN:
//            case R_BRACK:
//            case THEN:
//            case DO:
//                return inh;
//            default:
//                if (tok.token != LEXERR && !tok_reported) {
//                    errorMessages = errorMessages
//                            + "SYNERR: Expected L_BRACK, COMMA, SEMI_C, or ELSE, END, RELOP, ADDOP, MULOP, R_PAREN, R_BRACK, THEN, DO, received: "
//                            + token_defs[tok.token][0] + "\n";
//                    tok_reported = true;
//                }
//                while (!SynchSets.isInSynchSet(tok.token, SynchSets.factor) && !EOF_FOUND) {
//                    getNextToken();
//                    if (tok.token == EOF) {
//                        EOF_FOUND = true;
//                    }
//                }
//                return new Decoration(new Type("ERR"), 0);
//        }
//    }
//
//    public static void sign() {
//        if (tok.token == ADDOP && (tok.attribute == 3 || tok.attribute == 4)) {
//            match(ADDOP);
//        } else {
//            if (tok.token != LEXERR && !tok_reported) {
//                errorMessages = errorMessages + "SYNERR: ADDOPs: + or -, received: " + token_defs[tok.token][0] + "\n";
//                tok_reported = true;
//            }
//            while (!SynchSets.isInSynchSet(tok.token, SynchSets.sign) && !EOF_FOUND) {
//                getNextToken();
//                if (tok.token == EOF) {
//                    EOF_FOUND = true;
//                }
//            }
//        }
//    }
//
//
//    public static void match(int t) {
//        System.out.println("Match called with token " + token_defs[t][0]);
//        if (t == tok.token && t == EOF) {
//            System.out.println("Success!!");
//        } else if (t == tok.token && t != EOF) {
//            getNextToken();
//        } else if (t != tok.token) {
//            if (!tok_reported) {
//                System.out.println("SYNERR: Expected " + t + " received " + tok.token);
//                errorMessages = errorMessages + "SYNERR: Expected " + token_defs[t][0] + " received "
//                        + token_defs[tok.token][0] + "\n";
//                pw.write(errorMessages);
//                pw.flush();
//            }
//            getNextToken();
//        }
//    }
//
//    public static void parse() {
//        getNextToken();
//        program();
//        match(EOF);
//        pw.flush();
//        symbolWriter();
//    }
//
//    public static void getNextToken() {
//        boolean flag = false;
//
//        System.out.println("b_p = " + b_p + " c_buffer.length = " + c_buffer.length);
//        if (b_p < c_buffer.length && !EOF_REACHED) {
//            System.out.print("Line Number: " + lineNumber + ", Front Pointer: " + f_p + ", Back Pointer: " + b_p + ", and then: ");
//            tok = getToken();
//            tok_reported = false;
//            if (tok != null) {
//                printToken(tok);
//                if (tok.token == 99) {
//                    errorMessages = errorMessages + "LEXERR: " + tok.lexeme + " commits error: "
//                            + token_defs[99][tok.attribute + 1] + "\n";
//                }
//            } else {
//                flag = true;
//            }
//        } else {
//            flag = true;
//        }
//        if (flag) {
//            System.out.println("");
//            int numberofSpaces = 3 - (int) Math.log10(lineNumber);
//            String beforeLine = "";
//            for (int i = 0; i < numberofSpaces; i++) {
//                beforeLine = beforeLine + " ";
//            }
//            beforeLine = beforeLine + lineNumber + ". ";
//            String fromBuffer = new String(c_buffer);
//            if (!scan.hasNextLine()) {
//                fromBuffer = fromBuffer.substring(0, fromBuffer.length() - 1) + "EOF\n";
//                if (!EOF_REACHED) {
//                    pw.write(beforeLine + fromBuffer);
//                    pw.write(errorMessages);
//                    pw.flush();
//                }
//                EOF_REACHED = true;
//                lineNumber++;
//
//                f_p = 0;
//                b_p = 0;
//                errorMessages = "";
//                if (!EOF_FOUND && !EOF_REACHED) {
//                    getNextToken();
//                } else {
//                    System.out.println("Setting tok to EOF");
//                    tok = new Token("EOF", EOF, 0);
//                }
//
//            } else {
//                pw.write(beforeLine + fromBuffer);
//                pw.write(errorMessages);
//                pw.flush();
//                lineNumber++;
//                String temp = scan.nextLine();
//                temp = temp + "\n";
//                if (temp.length() > 72) {
//                    temp = temp.substring(0, 72);
//                }
//                c_buffer = temp.toCharArray();
//                f_p = 0;
//                b_p = 0;
//                errorMessages = "";
//
//                getNextToken();
//            }
//
//        }
//
//    }
//
//    // H I S T O R I C  F U N C T I O N
//    public static void getNextLine() {
//        String temp = scan.nextLine();
//        temp = temp + "\n";
//        if (temp.length() > 72) {
//            temp = temp.substring(0, 72);
//        }
//        c_buffer = temp.toCharArray();
//        f_p = 0;
//        b_p = 0;
//        while (b_p < c_buffer.length) {
//            System.out.print(lineNumber + "," + f_p + ", " + b_p + ", and then: ");
//            tok = getToken();
//            if (tok != null) {
//                printToken(tok);
//                if (tok.token == 99) {
//                    errorMessages = errorMessages + "LEXERR: " + tok.lexeme + " commits error: "
//                            + token_defs[99][tok.attribute + 1] + "\n";
//                }
//
//            }
//        }
//        System.out.println("");
//        int numberofSpaces = 3 - (int) Math.log10(lineNumber);
//        String beforeLine = "";
//        for (int i = 0; i < numberofSpaces; i++) {
//            beforeLine = beforeLine + " ";
//        }
//        beforeLine = beforeLine + lineNumber + ". ";
//        String fromBuffer = new String(c_buffer);
//        if (!scan.hasNextLine()) {
//            fromBuffer = fromBuffer.substring(0, fromBuffer.length() - 1) + "EOF";
//        }
//        pw.write(beforeLine + fromBuffer);
//        pw.write(errorMessages);
//        pw.flush();
//        lineNumber++;
//    }
//
//    public static Token getToken() {
//        // Whitespace
//        while (f_p < c_buffer.length && (c_buffer[f_p] == ' ' || c_buffer[f_p] == '\t' || c_buffer[f_p] == '\n')) {
//            f_p++;
//        }
//        b_p = f_p;
//        // ID/RES
//        if (f_p < c_buffer.length && Character.isLetter(c_buffer[f_p])) {
//            f_p++;
//            while (f_p < c_buffer.length && (Character.isLetter(c_buffer[f_p]) || Character.isDigit(c_buffer[f_p]))) {
//                f_p++;
//            }
//            if (f_p - b_p > 10) {
//                String lex = new String(c_buffer).substring(b_p, f_p);
//                b_p = f_p;
//                return new Token(lex, 99, 2); // LEXICAL ERROR, LONG ID
//            }
//            String lex = new String(c_buffer).substring(b_p, f_p);
//            b_p = f_p;
//            Token retMe = new Token(lex, 10, 1);
//            for (int i = 0; i < reserved_words.size(); i++) {
//                if (lex.equals(reserved_words.get(i).lexeme)) {
//                    retMe = reserved_words.get(i);
//                    return retMe;
//                }
//            }
//            int inlist = -1;
//            for (int i = 0; i < id_list.size(); i++) {
//                if (lex.equals(id_list.get(i))) {
//                    inlist = i;
//                }
//            }
//            if (inlist != -1) {
//                return new Token(lex, 10, inlist);
//            } else {
//                id_list.add(lex);
//            }
//            return new Token(lex, 10, id_list.size() - 1);
//            // CHECK RESERVED WORD LIST, RETURN ID/RES!!
//        }
//        // LONGREAL
//        if (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//            f_p++;
//            while (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                f_p++;
//            }
//            if (f_p < c_buffer.length && c_buffer[f_p] == '.') {
//                f_p++;
//                if (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                    f_p++;
//                    while (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                        f_p++;
//                    }
//                    if (f_p < c_buffer.length && c_buffer[f_p] == 'E') {
//                        f_p++;
//                        if (f_p < c_buffer.length && (c_buffer[f_p] == '+' || c_buffer[f_p] == '-')) {
//                            f_p++;
//                        }
//                        if (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                            f_p++;
//                            while (f_p < c_buffer.length
//                                    && (Character.isLetter(c_buffer[f_p]) || Character.isDigit(c_buffer[f_p]))) {
//                                f_p++;
//                            }
//                            String lex = new String(c_buffer).substring(b_p, f_p);
//                            String[] parts = lex.split("\\.");
//                            String[] parts2 = parts[1].split("E");
//                            if (parts2[1].charAt(0) == '+' || parts2[1].charAt(0) == '-') {
//                                parts2[1] = parts2[1].substring(1);
//                            }
//                            if (parts[0].length() > 5 || parts2[0].length() > 5 || parts2[1].length() > 2) {
//                                b_p = f_p;
//                                return new Token(lex, 99, 5);
//                                // RETURN LEXERR, LONGREAL TOO LONG
//                            }
//                            if (c_buffer[b_p] == '0' && !parts[0].equals("0")) {
//                                b_p = f_p;
//                                return new Token(lex, 99, 6);
//                                // RETURN LEXERR, LEADING 0s
//                            }
//                            if (parts2[0].length() > 1 && parts2[0].charAt(parts2[0].length() - 1) == '0'
//                                    && parts2[0].charAt(parts2[0].length() - 2) == '0') {
//                                b_p = f_p;
//                                return new Token(lex, 99, 7);
//                                // RETURN LEXERR, TRAILING 0s
//                            }
//                            b_p = f_p;
//                            System.out.print("longreal");
//                            return new Token(lex, 50, 3);
//                            // RETURN LONGREAL
//                        }
//                    }
//                }
//
//            }
//        }
//        f_p = b_p;
//        // REAL
//        if (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//            f_p++;
//            while (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                f_p++;
//            }
//            if (f_p < c_buffer.length && c_buffer[f_p] == '.') {
//                f_p++;
//                if (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                    f_p++;
//                    while (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                        f_p++;
//                    }
//                    String lex = new String(c_buffer).substring(b_p, f_p);
//                    String[] parts = lex.split("\\.");
//                    if (parts[0].length() > 5 || parts[1].length() > 5) {
//                        b_p = f_p;
//                        return new Token(lex, 99, 4);
//                        // RETURN LEXERR, REAL TOO LONG
//                    }
//                    if (c_buffer[b_p] == '0' && !parts[0].equals("0")) {
//                        b_p = f_p;
//                        return new Token(lex, 99, 6);
//                        // RETURN LEXERR, LEADING 0s
//                    }
//                    if (parts[1].length() > 1 && parts[1].charAt(parts[1].length() - 1) == '0'
//                            && parts[1].charAt(parts[1].length() - 2) == '0') {
//                        b_p = f_p;
//                        return new Token(lex, 99, 7);
//                        // RETURN LEXERR, TRAILING 0s
//                    }
//                    b_p = f_p;
//                    System.out.print("real");
//                    return new Token(lex, 50, 2);
//                    // RETURN REAL
//                }
//            }
//        }
//        f_p = b_p;
//        // INT
//        if (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//            f_p++;
//            while (f_p < c_buffer.length && Character.isDigit(c_buffer[f_p])) {
//                f_p++;
//            }
//            if (f_p - b_p > 10) {
//                String lex = new String(c_buffer).substring(b_p, f_p);
//                b_p = f_p;
//                return new Token(lex, 99, 3);
//                // RETURN LEXERR, INT TOO LONG
//            }
//            String lex = new String(c_buffer).substring(b_p, f_p);
//            if (c_buffer[b_p] == '0' && !lex.equals("0")) {
//                b_p = f_p;
//                return new Token(lex, 99, 6);
//                // RETURN LEXERR, LEADING 0s
//            }
//            b_p = f_p;
//            System.out.print("int ");
//            return new Token(lex, 50, 1);
//            // RETURN INT
//        }
//        f_p = b_p;
//        // RELOP
//        if (f_p < c_buffer.length && c_buffer[f_p] == '=') {
//            // =
//            f_p++;
//            b_p = f_p;
//            return new Token("=", 38, 1);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '<') {
//            f_p++;
//            if (f_p < c_buffer.length && c_buffer[f_p] == '>') {
//                f_p++;
//                b_p = f_p;
//                // <>, NEQ
//                return new Token("<>", 38, 2);
//            }
//            if (f_p < c_buffer.length && c_buffer[f_p] == '=') {
//                f_p++;
//                b_p = f_p;
//                // <=, LTEQ
//                return new Token("<=", 38, 4);
//            }
//            // <, LT
//            b_p = f_p;
//            return new Token("<", 38, 3);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '>') {
//            f_p++;
//            if (f_p < c_buffer.length && c_buffer[f_p] == '=') {
//                f_p++;
//                b_p = f_p;
//                // >=, GREQ
//                return new Token(">=", 38, 6);
//            }
//            // >, GT
//            b_p = f_p;
//            return new Token(">", 38, 5);
//        }
//        // ASSIGNOP
//        if (f_p < c_buffer.length && c_buffer[f_p] == ':') {
//            f_p++;
//            if (f_p < c_buffer.length && c_buffer[f_p] == '=') {
//                f_p++;
//                b_p = f_p;
//                // :=, assignop
//                return new Token(":=", 39, 0);
//            }
//            b_p = f_p;
//            // :, COLON
//            return new Token(":", 40, 0);
//        }
//        // CATCHALL
//        if (f_p < c_buffer.length && c_buffer[f_p] == '+') {
//            f_p++;
//            b_p = f_p;
//            // ADDOP, +
//            return new Token("+", 36, 2);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '-') {
//            f_p++;
//            b_p = f_p;
//            // ADDOP, -
//            return new Token("-", 36, 3);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '*') {
//            f_p++;
//            b_p = f_p;
//            // MULOP, *
//            return new Token("*", 37, 1);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '/') {
//            f_p++;
//            b_p = f_p;
//            // MULOP, /
//            return new Token("/", 37, 2);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == ';') {
//            f_p++;
//            b_p = f_p;
//            // SEMI, ;
//            return new Token(";", 41, 0);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == ',') {
//            f_p++;
//            b_p = f_p;
//            // COMMA, ,
//            return new Token(",", 42, 0);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '(') {
//            f_p++;
//            b_p = f_p;
//            // L_PAREN
//            return new Token("(", 43, 0);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == ')') {
//            f_p++;
//            b_p = f_p;
//            // R_PAREN
//            return new Token(")", 44, 0);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '[') {
//            f_p++;
//            b_p = f_p;
//            // L_BRACKET
//            return new Token("[", 45, 0);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == ']') {
//            f_p++;
//            b_p = f_p;
//            // R_BRACKET
//            return new Token("]", 46, 0);
//        }
//        if (f_p < c_buffer.length && c_buffer[f_p] == '.') {
//            f_p++;
//            if (f_p < c_buffer.length && c_buffer[f_p] == '.') {
//                f_p++;
//                b_p = f_p;
//                // TWODOT
//                return new Token("..", 48, 0);
//            }
//            b_p = f_p;
//            // LDOT
//            return new Token(".", 47, 0);
//        }
//        if (f_p >= c_buffer.length) {
//            System.out.println("Token gets nullified.");
//            return null;
//        }
//        b_p = f_p;
//        f_p++;
//        // UNRECOG
//        return new Token(String.valueOf(c_buffer[f_p - 1]), 99, 1);
//    }
//
//    public static void setupTokenDefs() {
//        token_defs = new String[100][];
//        token_defs[10] = new String[] { "ID", "NULL", "location" };
//        token_defs[20] = new String[] { "PROG", "NULL" };
//        token_defs[21] = new String[] { "OF", "NULL" };
//        token_defs[22] = new String[] { "ARRAY", "NULL" };
//        token_defs[23] = new String[] { "VAR", "NULL" };
//        token_defs[24] = new String[] { "STND_TYPE", "NULL", "INT", "REAL" };
//        token_defs[26] = new String[] { "FUNC", "NULL" };
//        token_defs[27] = new String[] { "PROC", "NULL" };
//        token_defs[28] = new String[] { "BEGIN", "NULL" };
//        token_defs[29] = new String[] { "END", "NULL" };
//        token_defs[30] = new String[] { "IF", "NULL" };
//        token_defs[31] = new String[] { "THEN", "NULL" };
//        token_defs[32] = new String[] { "ELSE", "NULL" };
//        token_defs[33] = new String[] { "WHILE", "NULL" };
//        token_defs[34] = new String[] { "DO", "NULL" };
//        token_defs[35] = new String[] { "NOT", "NULL" };
//        token_defs[36] = new String[] { "ADDOP", "NULL", "OR", "PLUS_SIGN", "MINUS_SIGN" };
//        token_defs[37] = new String[] { "MULOP", "NULL", "MUL_SIGN", "DIV_SIGN", "DIV", "MOD", "AND" };
//        token_defs[38] = new String[] { "RELOP", "NULL", "EQUAL_SIGN", "NOT_EQUAL", "LS_THAN", "LS_EQUAL", "GR_THAN",
//                "GR_EQUAL" };
//        token_defs[39] = new String[] { "ASSIGNOP", "NULL" };
//        token_defs[40] = new String[] { "COLON", "NULL" };
//        token_defs[41] = new String[] { "SEMI_C", "NULL" };
//        token_defs[42] = new String[] { "COMMA", "NULL" };
//        token_defs[43] = new String[] { "L_PAREN", "NULL" };
//        token_defs[44] = new String[] { "R_PAREN", "NULL" };
//        token_defs[45] = new String[] { "L_BRACK", "NULL" };
//        token_defs[46] = new String[] { "R_BRACK", "NULL" };
//        token_defs[47] = new String[] { "DOT", "NULL" };
//        token_defs[48] = new String[] { "TWO_DOT", "NULL" };
//        token_defs[49] = new String[] { "EOF", "NULL" };
//        token_defs[50] = new String[] { "NUM", "NULL", "INT", "REAL", "LONGREAL" };
//        token_defs[72] = new String[] { "CALL", "NULL" };
//        token_defs[99] = new String[] { "LEXERR", "NULL", "Unrecog Symbol", "Extra Long ID", "Extra Long Int",
//                "Extra Long Real", "Extra Long Longreal", "Leading Zeroes", "Trailing zeroes" };
//    }
//
//    public static final int ID = 10;
//    public static final int PROGRAM = 20;
//    public static final int OF = 21;
//    public static final int ARRAY = 22;
//    public static final int VAR = 23;
//    public static final int STANDARD_TYPE = 24;
//    public static final int FUNCTION = 26;
//    public static final int PROCEDURE = 27;
//    public static final int BEGIN = 28;
//    public static final int END = 29;
//    public static final int IF = 30;
//    public static final int THEN = 31;
//    public static final int ELSE = 32;
//    public static final int WHILE = 33;
//    public static final int DO = 34;
//    public static final int NOT = 35;
//    public static final int ADDOP = 36;
//    public static final int MULOP = 37;
//    public static final int RELOP = 38;
//    public static final int ASSIGNOP = 39;
//    public static final int COLON = 40;
//    public static final int SEMI_C = 41;
//    public static final int COMMA = 42;
//    public static final int L_PAREN = 43;
//    public static final int R_PAREN = 44;
//    public static final int L_BRACK = 45;
//    public static final int R_BRACK = 46;
//    public static final int DOT = 47;
//    public static final int TWO_DOT = 48;
//    public static final int EOF = 49;
//    public static final int NUM = 50;
//    public static final int CALL = 72;
//    public static final int LEXERR = 99;
//
//}
