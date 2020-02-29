import java.util.ArrayList;

public class Scanner {
	
	public static void main(String args[]) 
	{
		String code = "MAIN {} end";
		ArrayList<String> tokens = scan(code);
		for (int x = 0; x < code.length(); x++)
		{
			//if tokens.get(x).equals("ERROR")
			System.out.println(tokens.get(x));
		}
	}
	
	public ArrayList<String> scan(String code)
	{
		ArrayList<String> token = new ArrayList<String>;
		int state=0;
		char lexeme;
		public static int c=0;
		while (c < code.length())
		{
			lexeme = code.charAt(c);
			switch(state){
				case 0 :								//beggining of all DFA's and Reserve words
					if(lexeme == '(')
						token.add("[LPAREN]");
					else if(lexeme == ')')
						token.add("[RPAREN]");
					else if(lexeme == '{')
						token.add("[LCURLY]");
					else if(lexeme == '}')
						token.add("[RCURLY]");
					else if(lexeme == ';')
						token.add("[SCLON]");
					else if(lexeme == ',')
						token.add("[COMMA]");
					else if(lexeme == '+')
					{
						if nextChar('+')		//addsub, increment
							state = 1;
						else
							token.add("[ADDSUB]");
					}
					else if(lexeme == '-')		//addsub, decrement
					{
						if nextChar('-')
							state = 2;
						else
							token.add("[ADDSUB]");
					}
					else if(lexeme == '*' || lexeme == '/')
						token.add("[MULDIV]");
					else if(lexeme == '^')
						token.add("[EXPON]");
					else if(lexeme == '=')
					{
						if nextChar('=')
							state = 3;
						else
							token.add("[ASSIGN]");
					}
					else if(isDigit(lexeme))	//intlit, floatlit
						state = 4;
					else if(lexeme == '"')
						state = 5;
					else if(lexeme == '\'')
						state = 6;
					else if(lexeme == '>')
						state = 7;
					else if(lexeme == '<')
						state = 8;
					else if(isLetter(lexeme)	//id, boolconst, logical operators, reserved words
					{
						switch(lexeme)			//every option is a possible identifier
						{
							case 'A':			//AND
								state = 9;
								break;
							case 'F':			//FALSE
								state = 10;
								break;
							case 'I':			//INPUT
								state = 11;
								break;
							case 'M':			//MAIN
								state = 12;
								break;
							case 'N':			//NAND, NOR, NOT
								state = 13;
								break;
							case 'O':			//OUTPUT
								state = 14;
								break;
							case 'T':			//TRUE
								state = 15;
								break;
							case 'X':			//XOR
								state = 16;
								break;
							case 'b':			//bool
								state = 17;
								break;
							case 'c':			//case
								state = 18;
								break;
							case 'd':			//def
								state = 19;
								break;
							case 'e':			//else, elseif, end
								state = 20;
								break;
							case 'f':			//float, follow, for
								state = 21;
								break;
							case 'i':			//if, int
								state = 22;
								break;
							case 's':			//stop, str, switch
								state = 23;
								break;
							case 'w':			//while
								state = 24;
								break;
							default:
								state = 25 		//identifier state
								break;
						}
					}
					else
						token.add("ERROR");
					
					break;
				case 1:
					token.add("[INCREMENT]");
					state = 0;
					break;
				case 2:
					token.add("[DECREMENT]");
					state = 0;
					break;
				default:
					token.add("ErrorCaseNum");
			}
			c++;
		}
	}
	
	public bool nextChar(char letter)
	{
		lexeme = code.charAt(c+1);
		return (letter == lexeme);
	}
}

/*
Sample code:
MAIN {

	int num;			// this line shows declaration //
	num = 100;			// this line shows assignment //

	str word = "A string!";	// declaration and assignment //
	
} end
*/