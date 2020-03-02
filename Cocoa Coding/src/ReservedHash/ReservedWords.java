package ReservedHash;

import java.util.HashMap;

public class ReservedWords {
    public HashMap<String, String> predefineReserves() {
        HashMap<String, String> reservedWords = new HashMap<String, String>();
        reservedWords.put("INPUT", "[INPUT]");
        reservedWords.put("MAIN", "[START]");
        reservedWords.put("OUTPUT", "[OUTPUT]");
        reservedWords.put("bool", "[bool]");
        reservedWords.put("case", "[case]");
        reservedWords.put("def", "[def]");
        reservedWords.put("else", "[else]");
        reservedWords.put("elseif", "[elseif]");
        reservedWords.put("end", "[end]");
        reservedWords.put("float", "[float]");
        reservedWords.put("follow", "[follow]");
        reservedWords.put("for", "[for]");
        reservedWords.put("if", "[if]");
        reservedWords.put("int", "[int]");
        reservedWords.put("stop", "[stop]");
        reservedWords.put("str", "[str]");
        reservedWords.put("switch", "[switch]");
        reservedWords.put("while", "[while]");
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
