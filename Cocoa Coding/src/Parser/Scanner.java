package Parser;

import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import ReservedHash.ReservedWords;

public class Scanner {

    public static int state = 0,
            c = 0, tokenStart = 0, tokenEnd = 0,
            point = 0, errorCount = 0;
    public static final ReservedWords rwTable = new ReservedWords();
    public static HashMap<String, String> reservedWords = rwTable.predefineReserves();
    public static HashMap<String, String> identifiers = new HashMap<>();
    ArrayList<String> error = new ArrayList<>();//Creating arraylist    
    public static char lexeme;
    public static String code;

    public static String id, stringlit, intlit, floatlit;

    public static void main(String args[]) {
        try {
            File file = new File("src\\Cocoa Test Files\\test1.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            int lineNumber = 0;
            int counter = 0;
            String error[] = new String[20];
            System.out.print("File Name:" + file + "\n");
            System.out.println("===============================================");
            while ((st = br.readLine()) != null) {
                code = st + " ";            //per line
                ArrayList< String> tokens = scan(code);
                lineNumber++;
                for (int x = 0; x < tokens.size(); x++) {
                    System.out.print("[" + tokens.get(x) + "] ");
                    if (tokens.get(x).contains("ErrorCaseNum") || tokens.get(x).contains("Lexeme not in language")) {

                        error[counter] = "Error at Line " + lineNumber + ", Column " + (counter + 1) + ":  " + tokens.get(x);
                        counter++;
                    }
                }
                System.out.println();
            }

            System.out.println("===============================================");
            System.out.println("\nErrors found:" + errorCount);
            System.out.println("===============================================");
            System.out.println("\nIdentifiers Table:");
            for (String key : identifiers.keySet()) {
                System.out.println("VarName: " + key + "\tvalue: " + identifiers.get(key));
            }

            if (errorCount > 0) {
                for (int x = 0; x < counter; x++) {
                    System.out.println(error[x]);
                }
            }
            br.close();
            //necessary for error handling
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList< String> scan(String code) {
        ArrayList< String> token = new ArrayList<>();
        c = 0;
        while (c < code.length()) {
            lexeme = code.charAt(c);
            //System.out.println("c = " + c + ": " + lexeme);
            switch (state) {
                case 0:
                    if (lexeme == '{') {
                        token.add("LCURLY");
                    } else if (lexeme == '}') {
                        token.add("RCURLY");
                    } else if (lexeme == '(') {
                        token.add("LPAREN");
                    } else if (lexeme == ')') {
                        token.add("RPAREN");
                    } else if (lexeme == '#') {
                        if (nextChar('+')) {
                            state = 1;
                        } else {
                            token.add("Lexeme not in language: " + lexeme);
                        }
                    } else if (lexeme == '*') {
                        token.add("MULDIV");
                    } else if (lexeme == '+') {
                        if (nextChar('+')) {
                            state = 2;
                        } else {
                            token.add("ADDSUB");
                        }
                    } else if (lexeme == ',') {
                        token.add("COMMA");
                    } else if (lexeme == '-') {
                        state = 3;
                        tokenStart = c;
                    } else if (lexeme == '^') {
                        token.add("EXPON");
                    } else if (lexeme == '%') {
                        token.add("MULDIV");
                    } else if (lexeme == ':') {
                        token.add("COLON");
                    } else if (lexeme == ';') {
                        token.add("SCLON");
                    } else if (lexeme == '<') {
                        if (nextChar('=')) {
                            state = 5;
                        } else {
                            token.add("RELOP");
                        }
                    } else if (lexeme == '=') {
                        if (nextChar('=')) {
                            state = 6;
                        } else {
                            token.add("ASSIGN");
                        }
                    } else if (lexeme == '!') {
                        if (nextChar('=')) {
                            state = 7;
                        } else {
                            token.add("Lexeme not in language: " + lexeme);
                        }
                    } else if (lexeme == '>') {
                        if (nextChar('=')) {
                            state = 8;
                        } else {
                            token.add("RELOP");
                        }
                    } else if (lexeme == '/') {
                        if (nextChar('/')) {
                            state = 9;
                        } else {
                            token.add("MULDIV");
                        }
                    } else if (lexeme == '"') {
                        state = 10;
                        tokenStart = c;
                    } else if (lexeme == '\'') {
                        state = 11;
                        tokenStart = c;
                    } else if (Character.isDigit(lexeme)) {
                        state = 4;
                        tokenStart = c;
                    } else if (Character.isLetter(lexeme)) {
                        switch (lexeme) {
                            case 'A':
                                state = 13;
                                break;
                            case 'B':
                                state = 14;
                                break;
                            case 'C':
                                state = 15;
                                break;
                            case 'D':
                                state = 16;
                                break;
                            case 'E':
                                state = 17;
                                break;
                            case 'F':
                                state = 18;
                                break;
                            case 'I':
                                state = 19;
                                break;
                            case 'M':
                                state = 20;
                                break;
                            case 'N':
                                state = 21;
                                break;
                            case 'O':
                                state = 22;
                                break;
                            case 'S':
                                state = 23;
                                break;
                            case 'T':
                                state = 24;
                                break;
                            case 'W':
                                state = 25;
                                break;
                            case 'X':
                                state = 26;
                                break;
                            default:
                                state = 27; //identifier state
                                break;
                        }
                        tokenStart = c;
                    } else if (Character.isWhitespace(lexeme)) {
                    } //might need to add token [IGNORE]
                    else {
                        token.add("Lexeme not in language: " + lexeme);
                    }
                    // ============================ generally c = 1;
                    break;
                case 1:                     //guaranteed #+
                    token.add("CONCAT");
                    state = 0;
                    break;
                case 2:                     //guaranteed ++ 
                    token.add("INCREMENT");
                    state = 0;
                    break;
                case 3:                     //-_
                    if (lexeme == '-') {
                        token.add("DECREMENT");
                        state = 0;
                    } else if (Character.isDigit(lexeme)) {
                        state = 4;
                    } else {
                        token.add("ADDSUB");
                        pushback();
                    }
                    break;
                case 4:                     //1_ or -1_
                    if (lexeme == '.') {
                        state = 12;
                    } else if (!Character.isDigit(lexeme)) {
                        pushback();
                        token.add("INTLIT = " + getWord());
                        identifiers.replace(id, getWord());
                    }                       //else, stay in intlit state
                    break;
                case 5:                     //guaranteed <=
                    token.add("RELOP");
                    break;
                case 6:
                    token.add("RELOP");     //guaranteed ==
                    break;
                case 7:
                    token.add("RELOP");     //guaranteed != 
                    break;
                case 8:
                    token.add("RELOP");     //guaranteed >=
                    break;
                case 9:                     //guaranteed //
                    state = 28;
                    break;
                case 10:                    // "_
                    if (lexeme == '"') {
                        tokenEnd = c;
                        state = 0;
                        token.add("STRINGLIT = " + getWord());
                        identifiers.replace(id, getWord());
                    }                       //else, stay in stringlit state. must error if EOL reached
                    break;
                case 11:                    // '_
                    if (lexeme == '\'') {
                        tokenEnd = c;
                        state = 0;
                        token.add("STRINGLIT = " + getWord());
                        identifiers.replace(id, getWord());
                    }                       //else, stay in stringlit state. must error if EOL reached
                    break;
                case 12:                    //1._ or -1._
                    if (Character.isDigit(lexeme)) {
                        state = 29;
                    } else {
                        token.add("Incorrect floatlit"); //think about it later
                        state = 0;
                    }
                    break;
                case 13:                    //A_
                    if (lexeme == 'N') {
                        state = 30;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 14:                    //B_
                    if (lexeme == 'O') {
                        state = 31;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 15:                    //C_
                    if (lexeme == 'A') {
                        state = 32;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 16:                    //D_
                    if (lexeme == 'E') {
                        state = 33;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 17:                    //E_
                    if (lexeme == 'L') {
                        state = 34;
                    } else if (lexeme == 'N') {
                        state = 35;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 18:                    //F_
                    if (lexeme == 'A') {
                        state = 36;
                    } else if (lexeme == 'L') {
                        state = 37;
                    } else if (lexeme == 'O') {
                        state = 38;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 19:                    //I_
                    if (lexeme == 'F') {
                        state = 39;
                    }
                    if (lexeme == 'N') {
                        state = 40;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 20:                    //M_
                    if (lexeme == 'A') {
                        state = 41;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 21:                    //N_
                    if (lexeme == 'A') {
                        state = 42;
                    } else if (lexeme == 'O') {
                        state = 43;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 22:                    //O_
                    if (lexeme == 'R') {
                        state = 44;
                    } else if (lexeme == 'U') {
                        state = 45;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 23:                    //S_
                    if (lexeme == 'T') {
                        state = 46;
                    } else if (lexeme == 'W') {
                        state = 47;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 24:                    //T_
                    if (lexeme == 'R') {
                        state = 48;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 25:                    //W_
                    if (lexeme == 'H') {
                        state = 49;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 26:                    //X_
                    if (lexeme == 'O') {
                        state = 50;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 27:                    //identifier state
                    if (!(Character.isLetter(lexeme) || Character.isDigit(lexeme))) {
                        anIdentifier(token);
                    }
                    //else, stay in identifier state. must error if EOF reached?
                    break;
                // ============================ generally c = 2;
                case 28:                    // currently //_//
                    if (lexeme == '/') {
                        state = 51;
                    }
                    break;
                case 29:                    //floatlit state
                    if (!Character.isDigit(lexeme)) {
                        pushback();
                        token.add("FLOATLIT = " + getWord());
                        identifiers.replace(id, getWord());
                    }                       //else, stay in identifier state. must error if EOF reached?
                    break;
                case 30:                    //AN_
                    if (lexeme == 'D') {
                        state = 52;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 31:                    //BO_
                    if (lexeme == 'O') {
                        state = 53;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 32:                    //CA_
                    if (lexeme == 'S') {
                        state = 54;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 33:                    //DE_
                    if (lexeme == 'F') {
                        state = 55;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 34:                    //EL_
                    if (lexeme == 'S') {
                        state = 56;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 35:                    //EN_
                    if (lexeme == 'D') {
                        state = 57;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 36:                    //FA_
                    if (lexeme == 'L') {
                        state = 58;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 37:                    //FL_
                    if (lexeme == 'O') {
                        state = 59;
                    } else if (lexeme == 'T') {
                        state = 60;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 38:                    //FO_
                    if (lexeme == 'L') {
                        state = 61;
                    } else if (lexeme == 'R') {
                        state = 62;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 39:                    //IF_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("IF"));
                    }
                    break;
                case 40:                    //IN_
                    if (lexeme == 'P') {
                        state = 63;
                    } else if (lexeme == 'T') {
                        state = 64;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 41:                    //MA_
                    if (lexeme == 'I') {
                        state = 65;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 42:                    //NA_
                    if (lexeme == 'N') {
                        state = 66;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 43:                    //NO_
                    if (lexeme == 'R') {
                        state = 67;
                    } else if (lexeme == 'T') {
                        state = 68;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 44:                    //OR_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("OR");
                    }
                    break;
                case 45:                    //OU_
                    if (lexeme == 'T') {
                        state = 69;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 46:                    //ST_
                    if (lexeme == 'O') {
                        state = 70;
                    } else if (lexeme == 'R') {
                        state = 71;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 47:                    //SW_
                    if (lexeme == 'I') {
                        state = 72;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 48:                    //TR_
                    if (lexeme == 'U') {
                        state = 73;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 49:                    //WH_
                    if (lexeme == 'I') {
                        state = 74;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 50:                    //XO_
                    if (lexeme == 'R') {
                        state = 75;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                //  ============================ generally c = 3;
                case 51:                    // currently at ///_/
                    if (lexeme == '/') {
                        state = 0;
                    } else {
                        state = 28;         //sample: //abc/a  (resets to //_ ?) might need to rework DFA
                    }
                    break;
                case 52:                    //AND_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("AND");
                    }
                    break;
                case 53:                    //BOO_
                    if (lexeme == 'L') {
                        state = 76;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 54:                    //CAS_
                    if (lexeme == 'E') {
                        state = 77;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 55:                    //DEF_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("DEF"));
                    }
                    break;
                case 56:                    //ELS_
                    if (lexeme == 'E') {
                        state = 78;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 57:                    //END_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("END"));
                    }
                case 58:                    //FAL_
                    if (lexeme == 'S') {
                        state = 79;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 59:                    //FLO_
                    if (lexeme == 'A') {
                        state = 80;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 60:                    //FLT_
                    if (lexeme == '2') {
                        state = 81;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 61:                    //FOL_
                    if (lexeme == 'L') {
                        state = 82;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 62:                    //FOR_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("FOR"));
                    }
                case 63:                    //INP_
                    if (lexeme == 'U') {
                        state = 83;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 64:                    //INT_
                    if (lexeme == '2') {
                        state = 84;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("INT");
                    }
                    break;
                case 65:                    //MAI_
                    if (lexeme == 'N') {
                        state = 85;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 66:                    //NAN_
                    if (lexeme == 'D') {
                        state = 86;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 67:                    //NOR_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("NOR");
                    }
                    break;
                case 68:                    //NOT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("NOT");
                    }
                    break;
                case 69:                    //OUT_
                    if (lexeme == 'P') {
                        state = 87;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 70:                    //STO_
                    if (lexeme == 'P') {
                        state = 88;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 71:                    //STR_
                    if (lexeme == '2') {
                        state = 89;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("STR"));
                    }
                    break;
                case 72:                    //SWI_
                    if (lexeme == 'T') {
                        state = 90;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 73:                    //TRU_
                    if (lexeme == 'E') {
                        state = 91;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 74:                    //WHI_
                    if (lexeme == 'L') {
                        state = 92;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 75:                    //XOR_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 75;
                    } else {
                        pushback();
                        token.add("XOR");
                    }
                    break;
                //  ============================ generally c = 4;
                case 76:                    //BOOL_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("BOOL"));
                    }
                    break;
                case 77:                    //CASE_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("CASE"));
                    }
                    break;
                case 78:                    //ELSE_
                    if (lexeme == 'I') {
                        state = 93;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("ELSE"));
                    }
                    break;
                case 79:                    //FALS_
                    if (lexeme == 'E') {
                        state = 94;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 80:                    //FLOA_
                    if (lexeme == 'T') {
                        state = 95;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 81:                    //FLT2_
                    if (lexeme == 'I') {
                        state = 96;
                    } else if (lexeme == 'S') {
                        state = 97;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 82:                    //FOLL_
                    if (lexeme == 'O') {
                        state = 98;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 83:                    //INPU_
                    if (lexeme == 'T') {
                        state = 99;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 84:                    //INT2_
                    if (lexeme == 'F') {
                        state = 100;
                    } else if (lexeme == 'S') {
                        state = 101;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 85:                    //MAIN_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("MAIN"));
                    }
                    break;
                case 86:                    //NAND_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("NAND");
                    }
                    break;
                case 87:                    //OUTP_
                    if (lexeme == 'U') {
                        state = 102;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 88:                    //STOP_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("STOP"));
                    }
                    break;
                case 89:                    //STR2_
                    if (lexeme == 'F') {
                        state = 103;
                    } else if (lexeme == 'I') {
                        state = 104;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 90:                    //SWIT_
                    if (lexeme == 'C') {
                        state = 105;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 91:                    //TRUE_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("TRUE");
                    }
                    break;
                case 92:                    //WHIL_
                    if (lexeme == 'E') {
                        state = 106;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                //  ============================ generally c = 5;
                case 93:                    //ELSEI_
                    if (lexeme == 'F') {
                        state = 107;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 94:                    //FALSE_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add("FALSE");
                    }
                    break;
                case 95:                    //FLOAT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("FLOAT"));
                    }
                    break;
                case 96:                    //FLT2I_
                    if (lexeme == 'N') {
                        state = 108;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 97:                    //FLT2S_
                    if (lexeme == 'T') {
                        state = 109;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 98:                    //FOLLO_
                    if (lexeme == 'W') {
                        state = 110;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 99:                    //INPUT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("INPUT"));
                    }
                    break;
                case 100:                    //INT2F_
                    if (lexeme == 'L') {
                        state = 111;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 101:                    //INT2S_
                    if (lexeme == 'T') {
                        state = 111;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 102:                    //OUTPU_
                    if (lexeme == 'T') {
                        state = 112;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 103:                    //STR2F_
                    if (lexeme == 'T') {
                        state = 113;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 104:                    //STR2I_
                    if (lexeme == 'N') {
                        state = 114;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 105:                    //SWITC_
                    if (lexeme == 'H') {
                        state = 116;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 106:                    //WHILE_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("WHILE"));
                    }
                    break;
                //  ============================ generally c = 6;
                case 107:                    //ELSEIF_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("ELSEIF"));
                    }
                    break;
                case 108:                    //FLT2IN_
                    if (lexeme == 'T') {
                        state = 117;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 109:                    //FLT2ST_
                    if (lexeme == 'R') {
                        state = 118;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 110:                    //FOLLOW_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("FOLLOW"));
                    }
                    break;
                case 111:                    //INT2FL_
                    if (lexeme == 'T') {
                        state = 119;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 112:                    //INT2ST_
                    if (lexeme == 'R') {
                        state = 120;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 113:                    //OUTPUT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("OUTPUT"));
                    }
                    break;
                case 114:                    //STR2FL_
                    if (lexeme == 'T') {
                        state = 121;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 115:                    //STR2IN_
                    if (lexeme == 'T') {
                        state = 122;
                    } else if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        anIdentifier(token);
                    }
                    break;
                case 116:                    //SWITCH_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("SWITCH"));
                    }
                    break;
                //  ============================ generally c = 6;
                case 117:                    //FLT2INT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("FLT2INT"));
                    }
                    break;
                case 118:                    //FLT2STR_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("FLT2STR"));
                    }
                    break;
                case 119:                    //INT2FLT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("INT2FLT"));
                    }
                    break;
                case 120:                    //INT2STR_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("INT2STR"));
                    }
                    break;
                case 121:                    //STR2FLT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("STR2FLT"));
                    }
                    break;
                case 122:                    //STR2INT_
                    if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) {
                        state = 27;
                    } else {
                        pushback();
                        token.add(reservedWords.get("STR2INT"));
                    }
                    break;
                default:
                    token.add("ErrorCaseNum"); //should not happen once code is complete
                    state = 0;
                    errorCount += 1;
            }
            c++;
        }
        return token;
    }

    public static boolean nextChar(char letter) //good for single look ahead
    {
        try {
            char test = code.charAt(c + 1);
            //System.out.println(test);
            return (letter == test);
        } catch (Exception e) {
            System.out.println("nextChar error?");
            return false;
        }
    }

    public static void anIdentifier(ArrayList<String> token) {
        pushback();
        id = getWord();
        token.add("ID = " + id);
        identifiers.put(id, null);       //add identifier to hashMap
    }

    public static String getWord() { //identifier, stringlit, intlit, floatlit. Code similar to substring()
        String substring = "";
        for (int x = tokenStart; x <= tokenEnd; x++) {
            substring = substring + code.charAt(x);
        }
        return substring;
    }

    public static void pushback() {  //return state to 0 and move back 1 space
        state = 0;
        c--;
        tokenEnd = c;
    }
}
//note: add seperate state for error ?
