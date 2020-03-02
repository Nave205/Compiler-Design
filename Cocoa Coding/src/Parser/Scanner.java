package Parser;
import java.util.ArrayList;
import java.io.*; 
import java.util.HashMap;
import ReservedHash.ReservedWords;

public class Scanner {
    public static int state = 0,
    c = 0, tokenStart=0, tokenEnd=0,
    point = 0;
    public static ReservedWords rwTable = new ReservedWords();
    public static HashMap<String, String> reservedWords = rwTable.predefineReserves();
    public static HashMap<String, String> identifiers = new HashMap<>();
    public static char lexeme;
    public static String code;
    
    public static String id,stringlit,intlit,floatlit;
    
    public static void main(String args[]) {
		try{
			StringBuffer sb = new StringBuffer();	//required for null handling instead of concat
			File file = new File("C:\\Users\\User\\Desktop\\Cocoa Test Files\\test1.txt"); 
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st; 
			while ((st = br.readLine()) != null) {
				sb.append(st); 
				code = sb + " ";
				ArrayList < String > tokens = scan(code);
				for (int x = 0; x < tokens.size(); x++) {
					System.out.print(tokens.get(x) + " ");
				}
				System.out.println();
			} 
			br.close();
			
			code = sb + " ";
			//necessary for error handling
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static ArrayList < String > scan(String code) {
        ArrayList < String > token = new ArrayList <> ();
        while (c < code.length()) {
            lexeme = code.charAt(c);
            switch (state) {
            case 0: //beggining of all DFA's and Reserve words
                if (lexeme == '(')
                    token.add("[LPAREN]");
                else if (lexeme == ')')
                    token.add("[RPAREN]");
                else if (lexeme == '{')
                    token.add("[LCURLY]");
                else if (lexeme == '}')
                    token.add("[RCURLY]");
                else if (lexeme == ';')
                    token.add("[SCLON]");
                else if (lexeme == ',')
                    token.add("[COMMA]");
                else if (lexeme == '+' || lexeme == '-') {
                    if (nextChar('+')) //increment
                        state = 1;
                    else if (nextChar('-')) //decrement
                        state = 2;
                    else
                        token.add("[ADDSUB]");
                } else if (lexeme == '*')
                    token.add("[MULDIV]");
                else if (lexeme == '^')
                    token.add("[EXPON]");
                else if (lexeme == '=') {
                    if (nextChar('='))
                        state = 3;
                    else
                        token.add("[ASSIGN]");
                } else if (Character.isDigit(lexeme)) //intlit, floatlit
				{
                    state = 4;
					tokenStart=c;
				}
                else if (lexeme == '"')
				{
                    state = 5;
					tokenStart=c;
				}
                else if (lexeme == '\'')
				{
                    state = 6;
					tokenStart=c;
				}
                else if (lexeme == '>' || lexeme == '<') {
                    if (nextChar('='))
                        state = 7;
                    else
                        token.add("[RELOP]");
                } else if (Character.isLetter(lexeme)) //id, boolconst, logical operators, reserved words
                {
                    switch (lexeme) //every option is a possible identifier
                    {
                    case 'A': //AND
                        state = 8;
                        break;
                    case 'F': //FALSE
                        state = 9;
                        break;
                    case 'I': //INPUT
                        state = 10;
                        break;
                    case 'M': //MAIN
                        state = 11;
                        break;
                    case 'N': //NAND, NOR, NOT
                        state = 12;
                        break;
                    case 'O': //OUTPUT
                        state = 13;
                        break;
                    case 'T': //TRUE
                        state = 14;
                        break;
                    case 'X': //XOR
                        state = 15;
                        break;
                    case 'b': //bool
                        state = 16;
                        break;
                    case 'c': //case
                        state = 17;
                        break;
                    case 'd': //def
                        state = 18;
                        break;
                    case 'e': //else, elseif, end
                        state = 19;
                        break;
                    case 'f': //float, follow, for
                        state = 20;
                        break;
                    case 'i': //if, int
                        state = 21;
                        break;
                    case 's': //stop, str, switch
                        state = 22;
                        break;
                    case 'w': //while
                        state = 23;
                        break;
                    default:
                        state = 24; //identifier state
                        break;
                    }
					tokenStart = c;
                } 
                else if (lexeme == '/') //either comment or divide
                {
                    if (nextChar('/'))
                        state = 96; //state of comment DFA
                    else
                        token.add("[MULDIV]");
                }
				else if (Character.isWhitespace(lexeme)) // \n, \t, \r, ' ', EOL
				{}
				else
				{
                    token.add("ERR = Lexeme not in language : '" + lexeme + "'");
				}
                break;
            case 1:
                token.add("[INCREMENT]");
                state = 0;
                break;
            case 2:
                token.add("[DECREMENT]");
                state = 0;
                break;
            case 3:
                token.add("[RELOP]"); //==
                state = 0;
                break;
            case 4:
                if (lexeme == '.')
                    state = 25; //float
                else if (Character.isDigit(lexeme)) {}
                else {
                    pushback();
                    token.add("[INTLIT = " + getWord() + "]");
                    identifiers.replace(id, getWord());
                }
                break;
            case 5:
                if (lexeme == '"') {
                    state=0;
		    tokenEnd=c;
                    token.add("[STRINGLIT = " + getWord() + "]");
                    identifiers.replace(id, getWord());
                }
                break;
            case 6:
                if (lexeme == '\'') {
                    state=0;
                    tokenEnd=c;
                    token.add("[STRINGLIT = " + getWord() + "]");
                    identifiers.replace(id, getWord());
                }
                break;
            case 7: //>=, <=
                token.add("[RELOP]");
                state = 0;
            case 8:
                if (lexeme == 'N') //AN-D
                    state = 26;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 9:
                if (lexeme == 'A') //FA-LSE
                    state = 27;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 10:
                if (lexeme == 'N')
                    state = 28; //IN-PUT
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 11:
                if (lexeme == 'A')
                    state = 29; //MA-IN
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 12:
                if (lexeme == 'A') //NA-ND
                    state = 30;
                else if (lexeme == 'O') //NO-R, NO-T
                    state = 31;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 13:
                if (lexeme == 'U') //OU-TPUT
                    state = 32;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 14:
                if (lexeme == 'R') //TR-UE
                    state = 33;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 15:
                if (lexeme == 'O') //XO-R
                    state = 34;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 16:
                if (lexeme == 'o') //bo-ol
                    state = 35;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 17:
                if (lexeme == 'a') //ca-se
                    state = 36;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 18:
                if (lexeme == 'e') //de-f
                    state = 37;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 19:
                if (lexeme == 'l') //el-se, el-seif
                    state = 38;
                else if (lexeme == 'n') //en-d
                    state = 39;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 20:
                if (lexeme == 'l') //fl-oat
                    state = 40;
                else if (lexeme == 'o') //fo-llow, fo-r
                    state = 41;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 21:
                if (lexeme == 'f') //if
                    state = 42;
                else if (lexeme == 'n') //in-t
                    state = 43;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    state = 0;
                    c--;
                }
                break;
            case 22:
                if (lexeme == 't') //st-op, st-r
                    state = 44;
                else if (lexeme == 'w') //sw-itch
                    state = 45;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 23:
                if (lexeme == 'h') //wh-ile
                    state = 46;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 24:
                if (!(Character.isLetter(lexeme) || Character.isDigit(lexeme))) {
                    pushback();
                    token.add("[ID = " + getWord() + "]");
                    identifiers.put(id, null);
                }
                break;
            case 25:
                if (!Character.isDigit(lexeme)) {
                    token.add("[FLOAT]");
                    pushback();
                }
                break;
            case 26:
                if (lexeme == 'D') //AND
                    state = 47;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 27:
                if (lexeme == 'L') //FAL-SE
                    state = 48;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 28:
                if (lexeme == 'P') //INP-UT
                    state = 49;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 29:
                if (lexeme == 'I') //MAI-N
                    state = 50;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 30:
                if (lexeme == 'N') //NAN-D
                    state = 51;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 31:
                if (lexeme == 'R') //NOR
                    state = 52;
                else if (lexeme == 'T') //NOT
                    state = 53;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 32:
                if (lexeme == 'T') //OUT-PUT
                    state = 54;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 33:
                if (lexeme == 'U') //TRU-E
                    state = 55;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 34:
                if (lexeme == 'R') //XOR
                    state = 56;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 35:
                if (lexeme == 'o') //boo-l
                    state = 57;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 36:
                if (lexeme == 's') //cas-e
                    state = 58;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 37:
                if (lexeme == 'f') //def
                    state = 59;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 38:
                if (lexeme == 's') //els-e
                    state = 60;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 39:
                if (lexeme == 'd') //end
                    state = 61;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 40:
                if (lexeme == 'o') //flo-at
                    state = 62;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 41:
                if (lexeme == 'l') //fol-low
                    state = 63;
                else if (lexeme == 'r') //for
                    state = 64;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 42: //if_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("if"));
                    pushback();
                }
                break;
            case 43:
                if (lexeme == 't') //int
                    state = 65;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 44:
                if (lexeme == 'o') //sto-p
                    state = 66;
                else if (lexeme == 'r') //str
                    state = 67;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 45:
                if (lexeme == 'i') //swi-tch
                    state = 68;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 46:
                if (lexeme == 'i') //whi-le
                    state = 69;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 47:

                if (Character.isLetter(lexeme) || Character.isDigit(lexeme)) //AND_
                    state = 24;
                else {
                    token.add("[AND]");
                    pushback();
                }
                break;
            case 48:
                if (lexeme == 'S') //FALS-E
                    state = 70;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 49:
                if (lexeme == 'U') //INPU-T
                    state = 71;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 50:
                if (lexeme == 'N') //MAIN
                    state = 72;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 51:
                if (lexeme == 'D') //NAND
                    state = 73;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    state = 0;
                    c--;
                }
                break;
            case 52:
                //NOR_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[NOR]");
                    pushback();
                }
                break;
            case 53:
                //NOT_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[NOT]");
                    pushback();
                }
                break;
            case 54:
                if (lexeme == 'P') //OUTP-UT
                    state = 74;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 55:
                if (lexeme == 'E') //TRUE
                    state = 75;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 56:
                //XOR_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[XOR]");
                    pushback();
                }
                break;
            case 57:
                if (lexeme == 'l') //bool
                    state = 76;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 58:
                if (lexeme == 'e') //case
                    state = 77;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 59:
                //def_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("def"));
                    pushback();
                }
                break;
            case 60:
                if (lexeme == 'e') //else, else-if
                    state = 78;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 61:
                //end_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("end"));
                    pushback();
                }
                break;
            case 62:
                if (lexeme == 'a') //floa-t
                    state = 79;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 63:
                if (lexeme == 'l') //foll-ow
                    state = 80;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 64:
                //for_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("for"));
                    pushback();
                }
                break;
            case 65:
                //int_

                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("int"));
                    pushback();
                }
                break;
            case 66:
                if (lexeme == 'p') //stop
                    state = 81;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 67:
                //str_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("str"));
                    pushback();
                }
                break;
            case 68:
                if (lexeme == 't') //swit-ch
                    state = 82;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 69:
                if (lexeme == 'l') //whil-e
                    state = 83;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 70:
                if (lexeme == 'E') //FALSE
                    state = 84;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 71:
                if (lexeme == 'T') //INPUT
                    state = 85;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 72:
                //MAIN_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("MAIN"));
                    pushback();
                }
                break;
            case 73:
                //NAND_

                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[NAND]");
                    pushback();
                }
                break;
            case 74:
                if (lexeme == 'U') //OUTPU-T
                    state = 86;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 75:
                //TRUE_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[TRUE]");
                    pushback();
                }
                break;
            case 76:
                //bool_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("bool"));
                    pushback();
                }
                break;
            case 77:
                //case_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("case"));
                    pushback();
                }
                break;
            case 78:
                if (lexeme == 'i') //else_, elsei-f
                    state = 87;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("else"));
                    pushback();
                }
                break;
            case 79:
                if (lexeme == 't') //float
                    state = 88;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 80:
                if (lexeme == 'o') //follo-w
                    state = 89;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 81:
                //stop_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("stop"));
                    pushback();
                }
                break;
            case 82:
                if (lexeme == 'c') //switc-h
                    state = 90;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 83:
                if (lexeme == 'e') //while
                    state = 91;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
                //----------c=4
            case 84:
                //FALSE_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[FALSE]");
                    pushback();
                }
                break;
            case 85:
                //INPUT_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("INPUT"));
                    pushback();
                }
                break;
            case 86:
                if (lexeme == 'T') //OUTPUT
                    state = 92;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 87:
                if (lexeme == 'f') //elseif
                    state = 93;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 88:
                //float_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("float"));
                    pushback();
                }
                break;
            case 89:
                if (lexeme == 'w') //follow
                    state = 94;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 90:
                if (lexeme == 'h') //switch
                    state = 95;
                else if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add("[ID]");
                    pushback();
                }
                break;
            case 91:
                //while_

                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("while"));
                    pushback();
                }
                break;
                //-------------c=5
            case 92:
                //OUTPUT_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("OUTPUT"));
                    pushback();
                }
                break;
            case 93:
                //elseif_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("elseif"));
                    pushback();
                }
                break;
            case 94:
                //follow_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("follow"));
                    pushback();
                }
                break;
            case 95:
                //switch_
                if (Character.isLetter(lexeme) || Character.isDigit(lexeme))
                    state = 24;
                else {
                    token.add(reservedWords.get("switch"));
                    pushback();
                }
                break;
            case 96:
                if (lexeme == '/')
                    state = 97;
                else {
                    token.add("Error: /");
                    pushback();
                }
                break;
            case 97:
                if (lexeme == '/')
                    state = 98;
                break;
            case 98:
                if (lexeme == '/') // multi-line comment ended
                    state = 0;
                break;
            default:
                token.add("ErrorCaseNum"); //should not happen once code is complete
                state = 0;
            }
            c++;
        }
        return token;
    }

    public static boolean nextChar(char letter) //good for single look ahead
    {
        try {
            lexeme = code.charAt(c + 1);
            return (letter == lexeme);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getWord() 			//identifier, stringlit, intlit, floatlit
    {
		String substring = "";				//code similar to substring
        for(int x = tokenStart; x <= tokenEnd; x++)
		{
			substring = substring + code.charAt(x);
		}
        return substring;
    }

    public static void pushback() //return state to 0 and move back 1 space
    {
        state = 0;
        c--;
		tokenEnd = c;
    }
}

//note: add seperate state for error ?
//		unary plus/minus problem
//

/*
Sample code:
MAIN {

int num;			// this line shows declaration //
num = 100;			// this line shows assignment //

str word = "A string!";	// declaration and assignment //

} end
*/
