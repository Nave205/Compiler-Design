package ReservedHash;

import java.util.HashMap;

public class ReservedWords {
    public HashMap<String, String> predefineReserves() {
        HashMap<String, String> reservedWords = new HashMap<String, String>();
        reservedWords.put("BOOL", "BOOL");
        reservedWords.put("CASE", "CASE");
        reservedWords.put("DEF", "DEF");
        reservedWords.put("ELSE", "ELSE");
        reservedWords.put("END", "FINISH");
        reservedWords.put("FLOAT", "FLOAT");
        reservedWords.put("FLT2STR", "CONVERT");
        reservedWords.put("FLT2STR", "CONVERT");
        reservedWords.put("FOLLOW", "FOLLOW");
        reservedWords.put("FOR", "FOR");
        reservedWords.put("IF", "IF");
        reservedWords.put("INPUT", "INPUT");
        reservedWords.put("INT", "INTEGER");
        reservedWords.put("INT2FLT", "CONVERT");
        reservedWords.put("INT2STR", "CONVERT");
        reservedWords.put("MAIN", "START");
        reservedWords.put("OUTPUT", "OUTPUT");
        reservedWords.put("STOP", "STOP");
        reservedWords.put("STR", "STRING");
        reservedWords.put("STR2FLT", "CONVERT");
        reservedWords.put("STR2INT", "CONVERT");
        reservedWords.put("SWITCH", "SWITCH");
        reservedWords.put("WHILE", "WHILE");
        return reservedWords;
    }
}

/*
INPUT
MAIN
OUTPUT
bool
case
def
else
elseif
end
float
follow
for
if
int
stop
str
switch
while
 */
