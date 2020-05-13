package Parser;

import static Parser.Scanner.*;
import java.util.ArrayList;
import grtree.NPLviewer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Parser {
    public static String tree = "";
    public static int x = 0;
    public static int alter = 0;
    public static int close = 0;
    //public static ArrayList<String> tokens = read("src\\Cocoa Test Files\\parseErrorHandled2.txt");
    public static String nameofCurrMethod;
    public static ArrayList<String> tokens = read("C:\\Users\\User\\Desktop\\parseTest.txt");
    
    public static void main(String args[]) {
        String path = "src\\Cocoa Test Files\\test5.txt";
        parse();
        //String output = parse(read(path));
    }
    
    public static String parse() {
       
        System.out.println();
        System.out.println("==================Parse Part==================");
        System.out.println();
        
        tokens.add("$");
        System.out.println("Row of tokens: ");
        PrintArrayList(tokens);
        System.out.println();
        Prgm();
        
        System.out.println("Tree Syntax: ");
        System.out.println(tree);
        System.out.println();
        
        System.out.println("Final row of tokens: ");
        PrintArrayList(tokens);
        
        writeFile(tree);
        NPLviewer makeTree = new NPLviewer("src\\Cocoa Test Files\\generatedTree.txt");
        
        return tree;
    }
    
    public static String parse(ArrayList <String> code) {
        tokens.add("$");
        PrintArrayList(tokens);
        Prgm();
        return tree;
    }
    
    public static void PrintArrayList(ArrayList<String> content){
        for (int x = 0; x < content.size(); x++) {
            System.out.print("[" + content.get(x) + "] ");
        }
        System.out.println();
    }
    public static void Prgm() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        Start();
        tree += "]";
    }
    public static void Start() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){ 
            case "START": 
                tree += "[";
                Match("START");
                tree += ",";
                StmntBlk();
                tree += ",";
                Match("FINISH");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 5);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LCURLY": 
                        tokens.add(x, "START");
                        break;
                    default:
                        Error();
                        break;
                }
                Start();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]]";
        else
            alter--;
    }
    public static void StmntBlk() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){ 
            case "LCURLY": 
                tree += "[";
                Match("LCURLY");
                tree += ",";
                Stmnts();
                tree += ",";
                Match("RCURLY");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "LCURLY");
                System.out.println();
                StmntBlk();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void Stmnts() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){ 
            case "BOOL": 
            case "DECREMENT": 
            case "FLOAT": 
            case "FOR": 
            case "ID": 
            case "IF": 
            case "INCREMENT": 
            case "INTEGER": 
            case "LCURLY": 
            case "OUTPUT": 
            case "STRING": 
            case "SWITCH": 
            case "WHILE": 
                tree += "[";
                Stmnt();
                tree += ",";
                Stmnts();
                tree += "]";
                break;
            case "CASE": 
            case "DEF": 
            case "STOP": 
            case "RCURLY":
            case "$":     
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "(": 
                        tokens.add(x, "CASE");
                        break;
                    case ":": 
                        tokens.add(x, "DEF");
                        break;
                    case ";": 
                        tokens.add(x, "STOP");
                        break;
                    case "FINISH":
                        tokens.add(x, "RCURLY");
                        break;
                    default:
                        Error();
                        break;
                }
                Stmnts();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void Stmnt() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){ 
            case "INTEGER": 
            case "STRING": 
            case "FLOAT": 
            case "BOOL":
            case "ID":
                tree += "[";
                Decln();
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            case "IF":
                tree += "[";
                CondIf();
                tree += "]";
                break;
            case "FOR": 
            case "WHILE":
                tree += "[";
                Loop();
                tree += "]";
                break;
            case "LCURLY": 
                tree += "[";
                StmntBlk();
                tree += "]";
                break;    
            case "SWITCH": 
                tree += "[";
                Switch();
                tree += "]";
                break;
            case "OUTPUT": 
                tree += "[";
                Out();
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }

    public static void LitExp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT":
                tree += "[";
                NumLit();
                tree += "]";
                break;
            case "STRINGLIT":
                tree += "[";
                Match("STRINGLIT");
                tree += "]";
                break;
            case "TRUE":
            case "FALSE":  
                tree += "[";
                BoolConst();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "RPAREN": 
                        tokens.add(x, "STRINGLIT");
                    default:
                        Error();
                        break;
                }
                LitExp();
                break;
                //Error();
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void Decln() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTEGER": 
            case "STRING": 
            case "FLOAT": 
            case "BOOL":
                tree += "[";
                Dtype();
                tree += ",";
                Assign();
                tree += "]";
                break;
            case "ID":
                tree += "[";
                Assign();
                tree += "]";
                break;
            default:
                Error();
                break;  
        }
        tree += "]";
    }
    public static void Dtype() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTEGER":
                tree += "[";
                Match("INTEGER");
                tree += "]";
                break;
            case "STRING":
                tree += "[";
                Match("STRING");
                tree += "]";
                break;
            case "FLOAT": 
                tree += "[";
                Match("FLOAT");
                tree += "]";
                break;
            case "BOOL": 
                tree += "[";
                Match("BOOL");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void Assign() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ID":
                tree += "[";
                Match("ID");
                tree += ",";
                AssignPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "=": 
                        tokens.add(x, "ID");
                        break;
                    default:
                        Error();
                        break;
                }
                Assign();
                //Error();
                break; 
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void AssignPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INCREMENT": 
            case "DECREMENT": 
                tree += "[";
                UpDown();
                tree += "]";
                break;
            case "ASSIGN": 
                tree += "[";
                Match("ASSIGN");
                tree += ",";
                AssignPrimePrime();
                tree += "]";
                break;
            case "COMMA": 
                tree += "[";
                Match("COMMA");
                tree += ",";
                Assign();
                tree += "]";
                break;
            case "SCLON": 
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing ,/= or ; in Assignment");
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void AssignPrimePrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LPAREN": 
            case "INTLIT":
            case "FLOATLIT": 
            case "ID": 
            case "CONVERT":
                tree += "[";
                Arith();
                tree += "]";
                break;
            case "INPUT": 
                tree += "[";
                In();
                tree += "]";
                break;
            case "TRUE": 
            case "FALSE": 
                tree += "[";
                BoolConst();
                tree += "]";
                break;
            case "STRINGLIT": 
                tree += "[";
                Match("STRINGLIT");
                tree += "]";
                break;
            case "INCREMENT": 
            case "DECREMENT": 
                tree += "[";
                UpDown();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case ";": 
                        tokens.add(x, "STRINGLIT");
                        break;
                    default:
                        Error();
                        break;
                }
                AssignPrimePrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void NumLit() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
                tree += "[";
                Match("INTLIT");
                tree += "]";
                break;
            case "FLOATLIT": 
                tree += "[";
                Match("FLOATLIT");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "FLOATLIT");
                NumLit();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void BoolConst() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "TRUE": 
                tree += "[";
                Match("TRUE");
                tree += "]";
                break;
            case "FALSE": 
                tree += "[";
                Match("FALSE");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "TRUE");
                BoolConst();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void LogicExp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "NOT": 
            case "LPAREN": 
            case "TRUE": 
            case "FALSE": 
            case "ID": 
                tree += "[";
                Conj();
                tree += ",";
                LogicExpPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Logical Expression");
                Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void LogicExpPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "OR": 
            case "NOR": 
                tree += "[";
                OrOp();
                tree += ",";
                Conj();
                tree += ",";
                LogicExpPrime();
                tree += "]";
                break;
            case "RPAREN":
            case "SCLON":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "SCLON": 
                        tokens.add(x, "ID");
                        break;
                    case "RPAREN": 
                        tokens.add(x, "LCURLY");
                        break;    
                    default:
                        Error();
                        break;
                }
                LogicExpPrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void Conj() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "NOT": 
            case "LPAREN": 
            case "TRUE": 
            case "FALSE": 
            case "ID": 
                tree += "[";
                Disj();
                tree += ",";
                ConjPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Logical Expression");
                Error();
                break;
        }
        tree += "]";
    }
    public static void ConjPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "XOR": 
                tree += "[";
                XorOp();
                tree += ",";
                Disj();
                tree += ",";
                ConjPrime();
                tree += "]";
                break;
            case "RPAREN":
            case "SCLON":
            case "OR": 
            case "NOR":     
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "SCLON": 
                        tokens.add(x, "ID");
                        break;
                    case "RPAREN": 
                        tokens.add(x, "LCURLY");
                        break;  
                    default:
                        tokens.add(x, "OR");
                        break;
                }
                ConjPrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void Disj() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "NOT": 
            case "LPAREN": 
            case "TRUE": 
            case "FALSE": 
            case "ID": 
                tree += "[";
                ExOr();
                tree += ",";
                DisjPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Logical Expression");
                Error();
                break;
        }
        tree += "]";
    }
    public static void DisjPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "AND": 
            case "NAND": 
                tree += "[";
                AndOp();
                tree += ",";
                Disj();
                tree += "]";
                break;
            case "XOR":
            case "RPAREN":
            case "SCLON":
            case "OR": 
            case "NOR":     
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "SCLON": 
                        tokens.add(x, "ID");
                        break;
                    case "RPAREN": 
                        tokens.add(x, "LCURLY");
                        break;  
                    default:
                        tokens.add(x, "OR");
                        break;
                }
                DisjPrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void ExOr() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "NOT": 
                tree += "[";
                NotOp();
                tree += ",";
                LogBase();
                tree += "]";
                break;
            case "LPAREN": 
            case "TRUE": 
            case "FALSE": 
            case "ID": 
                tree += "[";
                LogBase();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Logical Expression");
                Error();
                break;
        }
        tree += "]";
    }
    public static void LogBase() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LPAREN": 
                tree += "[";
                Match("LPAREN");
                tree += ",";
                LogicExp();
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "TRUE": 
            case "FALSE": 
                tree += "[";
                BoolConst();
                tree += "]";
                break;
            case "ID": 
                tree += "[";
                RelExp();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "TRUE": 
                    case "FALSE": 
                    case "ID":
                    case "NOT":    
                        tokens.add(x, "LPAREN");
                        break;
                    default:
                        Error();
                        break;
                }
                LogBase();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void RelExp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ID": 
                tree += "[";
                Match("ID");
                tree += ",";
                Match("RELOP");
                tree += ",";
                Arith();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "RELOP":
                        tokens.add(x, "ID");
                        break;
                    default:
                        Error();
                        break;
                }
                RelExp();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void OrOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "OR": 
                Match("OR");
                break;
            case "NOR": 
                Match("NOR");
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "OR");
                OrOp();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void XorOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "XOR": 
                tree += "[";
                Match("XOR");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "XOR");
                XorOp();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void AndOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "AND": 
                tree += "[";
                Match("AND");
                tree += "]";
                break;
            case "NAND": 
                tree += "[";
                Match("NAND");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "AND");
                AndOp();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void NotOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "NOT": 
                tree += "[";
                Match("NOT");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "NOT");
                NotOp();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    /* -------------------------------------------  */
    
    public static void Arith() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
            case "CONVERT": 
                tree += "[";
                Term();
                tree += ",";
                ArithPrime();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void ArithPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ADDSUB": 
                tree += "[";
                Match("ADDSUB");
                tree += ",";
                Term();
                tree += ",";
                ArithPrime();
                tree += "]";
                break;
            case "RPAREN":
            case "AND":
            case "NAND":
            case "OR":
            case "NOR":
            case "XOR":
            //case "NOT":
            case "SCLON":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: +");
                //ArithPrime();
                Error();
                break;
        }
        tree += "]";
    }
    public static void Term() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
            case "CONVERT": 
                tree += "[";
                Factor();
                tree += ",";
                TermPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Arithmetic Expression");
                Error();
                break;
        }
        tree += "]";
    }
    public static void TermPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "MULDIV": 
                tree += "[";
                Match("MULDIV");
                tree += ",";
                Factor();
                tree += ",";
                TermPrime();
                tree += "]";
                break;
            case "ADDSUB": 
            case "RPAREN":
            case "AND":
            case "NAND":
            case "OR":
            case "NOR":
            case "XOR":
            case "NOT":
            case "SCLON":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: * or /");
                //TermPrime();
                Error();
                break;
        }
        tree += "]";
    }
    public static void Factor() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
            case "CONVERT": 
                tree += "[";
                Expow();
                tree += ",";
                FactorPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Arithmetic Expression");
                Error();
                break;
        }
        tree += "]";
    }
    public static void FactorPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "EXPON": 
                tree += "[";
        	Match("EXPON");
                tree += ",";
                Expow();
                tree += ",";
                FactorPrime();
                tree += "]";
                break;
            case "MULDIV":
            case "ADDSUB": 
            case "RPAREN":
            case "AND":
            case "NAND":
            case "OR":
            case "NOR":
            case "XOR":
            case "NOT":
            case "SCLON":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing tokens in Arithmetic Expression");

                Error();
                break;
        }
        tree += "]";
    }
    public static void Expow() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LPAREN": 
                tree += "[";
                Match("LPAREN");
                tree += ",";
                Arith();
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "INTLIT": 
            case "FLOATLIT": 
                tree += "[";
                NumLit();
                tree += "]";
                break;
            case "ID": 
                tree += "[";
                Match("ID");
                tree += "]";
                break;
            case "CONVERT": 
                tree += "[";
                Convert();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "INTLIT": 
                    case "FLOATLIT": 
                    case "ID":
                    case "CONVERT":    
                        tokens.add(x, "LPAREN");
                        break;
                    default:
                        tokens.add(x, "ID");;
                        break;
                }
                Expow();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void CondIf() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "IF": 
                tree += "[";
                Match("IF");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                LogicExp();
                tree += ",";
                Match("RPAREN");
                tree += ",";
                StmntBlk();
                tree += ",";
                CondStmt();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "IF");;
                        break;
                    default:
                        Error();
                        break;
                }
                CondIf();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void CondStmt() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ELSE": 
                tree += "[";
                CondElse();
                tree += "]";
                break;
            case "ELSEIF": 
                tree += "[";
                CondElseIf();
                tree += "]";
                break;
            case "BOOL": 
            case "DECREMENT": 
            case "FOR": 
            case "ID": 
            case "IF": 
            case "INCREMENT": 
            case "INTEGER": 
            case "LCURLY": 
            case "OUTPUT": 
            case "STRING": 
            case "SWITCH": 
            case "WHILE": 
            case "CASE": 
            case "DEF": 
            case "STOP": 
            case "FOLLOW": 
            case "RCURLY":
            case "$":         
            /*    
            case "STOP":  
            case "FOLLOW": 
            */
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "$": 
                        tokens.add(x, "RCURLY");
                        break;
                    case "LCURLY": 
                        tokens.add(x, "ELSEIF");
                        break;    
                    default:
                        Error();
                        break;
                }
                CondStmt();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void CondElseIf() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ELSEIF": 
                tree += "[";
                Match("ELSEIF");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                LogicExp();
                tree += ",";
                Match("RPAREN");
                tree += ",";
                StmntBlk();
                tree += ",";
                CondStmt();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "ELSEIF");
                        break;    
                    default:
                        Error();
                        break;
                }
                CondElseIf();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void CondElse() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ELSE": 
                tree += "[";
            	Match("ELSE");
                tree += ",";
                StmntBlk();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "ELSE");
                        break;    
                    default:
                        Error();
                        break;
                }
                CondElse();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void Loop() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "FOR":
                tree += "[";
                Match("FOR");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                LoopCond();
                tree += ",";
                Match("RPAREN");
                tree += ",";
                LoopBlk();
                tree += "]";
                break;
            case "WHILE": 
                tree += "[";
                Match("WHILE");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                LogicExp();
                tree += ",";
                Match("RPAREN");
                tree += ",";
                LoopBlk();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing FOR/WHILE token in Loop");
                Error();
                break;
        }
        tree += "]";
    }
    
    
    public static void LoopCond() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTEGER": 
            case "STRING": 
            case "FLOAT": 
            case "BOOL": 
            case "ID": 
                tree += "[";
                Decln();
                tree += ",";
                Match("SCLON");
                tree += ",";
                LogicExp();
                tree += ",";
                Match("SCLON");
                tree += ",";
                Match("ID");
                tree += ",";
                UpDown();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "ASSIGN": 
                        tokens.add(x, "ID");
                        break;    
                    default:
                        Error();
                        break;
                }
                LoopCond();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void UpDown() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INCREMENT": 
                tree += "[";
                Match("INCREMENT");
                tree += "]";
                break;
            case "DECREMENT": 
                tree += "[";
                Match("DECREMENT");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "SCLON": 
                        tokens.add(x, "INCREMENT");
                        break;    
                    default:
                        Error();
                        break;
                }
                UpDown();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void In() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INPUT": 
                tree += "[";
                Match("INPUT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                InOutCond();
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "INPUT");
                        break;    
                    default:
                        Error();
                        break;
                }
                In();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void InOutCond() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STRINGLIT":
            case "IGNORE": 
            case "ID": 
            case "CONVERT":
                tree += "[";
                ConCat();
                tree += "]";
                break;
            case "RPAREN":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing STRINGLIT/ID token inside INPUT/OUTPUT");
                Error();
                break;
        }
        tree += "]";
    }
    public static void Out() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "OUTPUT": 
                tree += "[";
                Match("OUTPUT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                InOutCond();
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "OUTPUT");;
                        break;
                    default:
                        Error();
                        break;
                }
                Out();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }

    public static void ConCat() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STRINGLIT":
            case "IGNORE": 
            case "ID": 
            case "CONVERT":
                tree += "[";
                String();
                tree += ",";
                ConCatPrime();
                tree += "]";
                break;    
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Recommendation: ");
                System.out.println("Missing STRINGLIT/ID token inside ConCat statement");
                Error();
                break;
        }
        tree += "]";
    }
    public static void ConCatPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "CONCAT": 
                tree += "[";
                Match("CONCAT");
                tree += ",";
                String();
                tree += ",";
                ConCatPrime();
                tree += "]";
                break;
            case "RPAREN":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "RPAREN":  
                        tokens.add(x, "CONCAT");
                        break;
                    default:
                        Error();
                        break;
                }
                ConCatPrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void String() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STRINGLIT":
                tree += "[";
                Match("STRINGLIT");
                tree += "]";
                break;
            case "IGNORE": 
                tree += "[";
                Match("IGNORE");
                tree += "]";
                break;
            case "ID": 
                tree += "[";
                Match("ID");
                tree += "]";
                break;
            case "CONVERT": 
                tree += "[";
                Match("CONVERT");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add("STRINGLIT");
                String();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void Switch() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "SWITCH": 
                tree += "[";
                Match("SWITCH");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("ID");
                tree += ",";
                Match("RPAREN");
                tree += ",";
                Match("LCURLY");
                tree += ",";
                Cases();
                tree += ",";
                Match("RCURLY");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "SWITCH");
                        break;
                    default:
                        Error();
                        break;
                }
                Switch();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void Cases() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "CASE": 
                tree += "[";
                Match("CASE");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                LitExp();
                tree += ",";
                Match("RPAREN");
                tree += ",";
                Match("COLON");	
                tree += ",";
                Stmnts();
                tree += ",";
                Stop();
                tree += ",";
                CasesPrime();
                tree += "]";
                break;
            case "RCURLY": 
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "CASE");
                        break;
                    default:
                        Error();
                        break;
                }
                Cases();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    public static void CasesPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "CASE": 
                tree += "[";
                Match("CASE");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                LitExp();
                tree += ",";
                Match("RPAREN");
                tree += ",";
                Match("COLON");
                tree += ",";
                Stmnts();
                tree += ",";
                Stop();
                tree += ",";
                CasesPrime();
                tree += "]";
                break;
            case "DEF": 
                tree += "[";
                Match("DEF");
                tree += ",";
                Match("COLON");	
                tree += ",";
                Stmnts();
                tree += ",";
                Match("STOP");
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            case "RCURLY": 
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "CASE");
                        break;
                    case "COLON": 
                        tokens.add(x, "DEF");
                        break;
                    default:
                        Error();
                        break;
                }
                CasesPrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void Stop() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STOP": 
                tree += "[";
                Match("STOP");
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            case "CASE": 
            case "DEF": 
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "SCLON": 
                        tokens.add(x, "STOP");
                        break;
                    default:
                        Error();
                        break;
                }
                Stop();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void Convert() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "CONVERT":
                tree += "[";
                Match("CONVERT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                ConvertPrime();
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "LPAREN": 
                        tokens.add(x, "CONVERT");
                        break;
                    default:
                        Error();
                        break;
                }
                Convert();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void ConvertPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
            case "CONVERT": 
                tree += "[";
                Arith();
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "STRINGLIT": 
                tree += "[";
                Match("STRINGLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "RPAREN": 
                        tokens.add(x, "STRINGLIT");
                        break;
                    default:
                        Error();
                        break;
                }
                ConvertPrime();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }

    public static void LoopBlk() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LCURLY": 
                tree += "[";
                Match("LCURLY");
                tree += ",";
                LoopStmnts();
                tree += ",";
                Match("RCURLY");
                tree += "]";
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                tokens.add(x, "LCURLY");
                LoopBlk();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void LoopStmnts() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[" + nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STOP": 
                tree += "[";
                Match("STOP");
                tree += ",";
                Match("SCLON");
                tree += ",";
                LoopStmnts();
                tree += "]";
                break;
            case "FOLLOW": 
                tree += "[";
                Match("FOLLOW");
                tree += ",";
                Match("SCLON");
                tree += ",";
                LoopStmnts();
                tree += "]";
                break;
            case "INTEGER": 
            case "STRING": 
            case "FLOAT": 
            case "BOOL":
            case "ID":
            case "IF":
            case "FOR": 
            case "WHILE":
            case "LCURLY": 
            case "SWITCH": 
            case "OUTPUT": 
                tree += "[";
                Stmnt();
                tree += ",";
                LoopStmnts();
                tree += "]";
                break;
            case "RCURLY": 
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                System.out.println("CFG Mismatch");
                System.out.println("Attempting to fix");
                tree = tree.substring(0, tree.length() - nameofCurrMethod.length() - 4);
                alter++;
                switch(getBase(tokens.get(x+1))){ 
                    case "SCLON": 
                        tokens.add(x, "STOP");
                        break;
                    default:
                        Error();
                        break;
                }
                LoopStmnts();
                //Error();
                break;
        }
        if (alter == 0)
            tree += "]";
        else
            alter--;
    }
    
    public static void Match(String token){
        if (getBase(tokens.get(x)).equals(token) || token.equals("$"))
        {
            System.out.println(nameofCurrMethod);
            System.out.println("To match: " + token + ", token: " + getBase(tokens.get(x)));
            System.out.println();
            x++;
            tree += "["+ token + " # []]";
        }
        else
        {  
            System.out.println("Method Match Error");
            System.out.println("Required match: " + token + ", current token: " + getBase(tokens.get(x)));
            System.out.println("Fixing");
            tokens.add(x,token);
            System.out.println("Added: " + token);
            System.out.println();
            x++;
            tree += "["+ token + " # []]";
            //Error();
        }
        
    }
    public static void Error(){
        System.out.println("Cannot fix CFG Mismatch");
        System.out.println("Error at index " + x);
        System.out.println("token: " + getBase(tokens.get(x)));
        System.out.println("Source function: " + nameofCurrMethod);
        System.exit(0);
    }
    public static String getBase(String token){
        String baseWord = "";
        for (int i = 0; i < token.length(); i++)
        {
            if (token.charAt(i) == ' ')
                break;
            baseWord += (token.charAt(i));
        }
        return baseWord;
    }
    public static void writeFile(String input){
        try {
            File output = new File("src\\Cocoa Test Files\\generatedTree.txt");
            if (output.createNewFile()) {
                System.out.println("File created: " + output.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        try {
            FileWriter myWriter = new FileWriter("src\\Cocoa Test Files\\generatedTree.txt");
	    myWriter.write(input);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}
