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
    public static int close = 0;
    public static ArrayList<String> tokens = read("src\\Cocoa Test Files\\test1.txt");
    public static String nameofCurrMethod;
    //public static ArrayList<String> tokens = read("C:\\Users\\User\\Desktop\\parseTest.txt");
    
    public static void main(String args[]) {
        String path = "src\\Cocoa Test Files\\test1.txt";
        parse();
        //String output = parse(read(path));
    }
    
    public static String parse() {
       
        System.out.println();
        System.out.println("==================Parse Part==================");
        System.out.println();
        
        tokens.add("$");
        PrintArrayList(tokens);
        Prgm();
        System.out.println(tree);
        writeFile(tree);
        NPLviewer makeTree = new NPLviewer("treetest.txt");
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
        tree += "[";
        tree += nameofCurrMethod + " # ";
        Start();
        tree += "]";
    }
    public static void Start() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[[";
        tree += nameofCurrMethod + " # ";
            tree += "[";
            Match("START");
            tree += ",";
            StmntBlk();
            tree += ",";
            Match("FINISH");
            tree += "]";
        tree += "]]";
    }
    public static void StmntBlk() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
            tree += "[";
            Match("LCURLY");
            tree += ",";
            Stmnts();
            tree += ",";
            Match("RCURLY");
            tree += "]";
        tree += "]";
    }
    public static void Stmnts() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
        switch(getBase(tokens.get(x))){
            case "ARITH": 
            case "BOOL": 
            case "FLOAT": 
            case "FLT2INT": 
            case "FLT2STR": 
            case "FOR": 
            case "ID": 
            case "IF": 
            case "INT2FLT": 
            case "INT2STR":
            case "INTEGER": 
            case "INPUT": 
            case "LCURLY": 
            case "LOGIC": 
            case "OUTPUT": 
            case "STR2FLT": 
            case "STR2INT": 
            case "STRING": 
            case "SWITCH": 
            case "WHILE": 
                tree += "[";
                Stmnt();
                tree += ",";
                Stmnts();
                tree += "]";
                break;
            case "RCURLY":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void Stmnt() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
            case "LOGIC": 
            case "ARITH": 
                tree += "[";
                Exp();
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            case "LCURLY": 
                tree += "[";
                StmntBlk();
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
            case "SWITCH": 
                Switch();
                break;
            case "INPUT":      ///////Problem/////////
            case "OUTPUT": 
                tree += "[";
                InOut();
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            case "STR2INT":
            case "STR2FLT": 
            case "INT2STR": 
            case "FLT2INT": 
            case "FLT2STR": 
            case "INT2FLT": 
                tree += "[";
                Convert();
                tree += ",";
                Match("SCLON");
                tree += "]";
                break;
            default:
                System.out.println("Stmnt");
                Error();
                break;
        }
        tree += "]";
    }
    public static void Exp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LOGIC": 
                tree += "[";
                Match("LOGIC");
                tree += ",";
                LogicExp();
                tree += "]";
                break;
            case "ARITH":
                tree += "[";
                Match("ARITH");
                Arith();
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
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTEGER": 
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void Decln() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTEGER": 
            case "STRING": 
            case "FLOAT": 
            case "BOOL": 
                tree += "[";
                Dtype();
                tree += ",";
                Declr();
                tree += "]";
                break;
            case "ID": 
                Declr();
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Dtype() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
            case "ID": 
                tree += "[";
                Declr();
                tree += "]";
                break;
            default:
                tree += "[";
                Error();
                tree += "]";
                break;
        }
        tree += "]";
    }
    
    public static void Declr() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ID": 
                tree += "[";
                Match("ID");
                tree += ",";
                Assign();
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
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ASSIGN": 
                tree += "[";
                Match("ASSIGN");
                tree += ",";
                AssignPrime();
                tree += "]";
                break;
            case "INCREMENT": 
            case "DECREMENT": 
                tree += "[";
                UpDown();
                tree += "]";
                break;
            case "SCLON": 
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void AssignPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ARITH": 
            case "LOGIC": 
                tree += "[";
                Exp();
                tree += "]";
                break;
            case "STR2INT":
            case "STR2FLT": 
            case "INT2STR": 
            case "FLT2INT": 
            case "FLT2STR": 
            case "INT2FLT": 
                tree += "[";
                Convert();
                tree += "]";
                break;
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
            case "FLOATLIT": 
                tree += "[";
                Match("FLOATLIT");
                tree += "]";
                break;
            case "INTLIT": 
                tree += "[";
                Match("INTLIT");
                tree += "]";
                break;
            case "ID":
                tree += "[";
                Match("ID");
                tree += "]";
                break;
            case "STRINGLIT":
                tree += "[";
                Match("STRINGLIT");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void NumLit() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void BoolConst() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void LogicExp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void LogicExpPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "OR": 
            case "NOR": 
                tree += "[";
                OrOp();
                tree += ",";
                Conj();
                tree += ",";
                LogicExp();
                tree += "]";
                break;
            case "RPAREN":
            case "SCLON":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Conj() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void ConjPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
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
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Disj() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                tree += "[";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void DisjPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
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
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void ExOr() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void LogBase() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LPAREN": 
                tree += "[";
                Match("LPAREN");
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
                Match("ID");
                tree += ",";
                RelExp();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void RelExp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
        switch(getBase(tokens.get(x))){
            case "RELOP": 
                tree += "[";
                Match("RELOP");
                tree += ",";
                Arith();
                tree += "]";
                break;
            case "AND": 
            case "NAND":   
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void OrOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";; 
        switch(getBase(tokens.get(x))){
            case "OR": 
                tree += "[";
                Match("OR");
                tree += "]";
                break;
            case "NOR": 
                tree += "[";
                Match("NOR");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void XorOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "XOR": 
                tree += "[";
                Match("XOR");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void AndOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void NotOp() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "NOT": 
                tree += "[";
                Match("NOT");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    /* -------------------------------------------  */
    
    public static void Arith() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
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
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Term() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
                tree += "[";
                Factor();
                tree += ",";
                TermPrime();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void TermPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
            case "follow": 
            case "ADDSUB":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Factor() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTLIT": 
            case "FLOATLIT": 
            case "LPAREN": 
            case "ID": 
                tree += "[";
                Expow();
                tree += ",";
                FactorPrime();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void FactorPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "EXPON": 
                tree += "[";
        	Match("EXPON");
                tree += ",";
                FactorPrime();
                tree += "]";
                break;
            case "MULDIV":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Expow() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void CondIf() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void CondStmt() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ELSE": 
            case "ELSEIF": 
                tree += "[";
                CondElseIf();
                tree += ",";
                CondElse();
                tree += "]";
                break;
            case "follow": 
            //case "Var":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void CondElseIf() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void CondElse() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ELSE": 
                tree += "[";
            	Match("ELSE");
                tree += ",";
                StmntBlk();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    public static void Loop() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                StmntBlk();
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
                StmntBlk();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    
    
    public static void LoopCond() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "INTEGER": 
            case "STRING": 
            case "FLOAT": 
            case "BOOL": 
                tree += "[";
                Decln();
                tree += ",";
                Match("SCLON");
                tree += ",";
                LogicExp();
                tree += ",";
                Match("SCLON");
                tree += ",";
                UpDown();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void UpDown() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ID": 
                tree += "[";
                Match("ID");
                tree += ",";
                UpDownPrime();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void UpDownPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void InOut() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "ID": 
                tree += "[";
                In();
                tree += "]";
                break;
            case "OUTPUT": 
                tree += "[";
                Out();
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void In() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # "; 
        switch(getBase(tokens.get(x))){
            case "ID": 
                tree += "[";
                Match("ID");
                tree += ",";
                Match("ASSIGN");
                tree += ",";
                Match("INPUT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                InCond();
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void InCond() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "LQUOTE": 
                tree += "[";
                Match("LQUOTE");
                tree += ",";
                ConCat();
                tree += ",";
                Match("RQUOTE");
                tree += "]";
                break;
            case "RPAREN":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Out() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "OUTPUT": 
                tree += "[";
                Match("OUTPUT");
                tree += ", ";
                Match("LPAREN");
                tree += ", ";
                OutCond();
                tree += ", ";
                Match("RPAREN");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void OutCond() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STRINGLIT":
            case "IGNORE": 
            case "ID": 
                tree += "[";
                ConCat();
                tree += "]";
                break;
            case "RPAREN":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void ConCat() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STRINGLIT":
            case "IGNORE": 
            case "ID": 
                tree += "[";
                String();
                tree += ", ";
                ConCatPrime();
                tree += "]";
                break;    
            default:
                nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
                Error();
                break;
        }
        tree += "]";
    }
    public static void ConCatPrime() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "CONCAT": 
                tree += "[";
                Match("CONCAT");
                tree += ", ";
                String();
                tree += ", ";
                ConCatPrime();
                tree += "]";
                break;
            case "RPAREN":
            case "RQUOTE":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
                Error();
                break;
        }
        tree += "]";
    }
    public static void String() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Switch() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void Cases() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
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
                CasesS();
                tree += ",";
                Match("SCLON");	
                tree += ",";
                Cases();
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
                Error();
                break;
        }
        tree += "]";
    }
    public static void CasesS() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STOP": 
                tree += "[";
                Match("STOP");
                tree += "]";
                break;
            case "CASE":
            case "DEF:":
                tree += "[[ # []]]";
                //do nothing
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }
    public static void Convert() {
        nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName(); 
        tree += "[";
        tree += nameofCurrMethod + " # ";
        switch(getBase(tokens.get(x))){
            case "STR2INT":
                tree += "[";
                Match("STR2INT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("STRINGLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "STR2FLT": 
                tree += "[";
                Match("STR2FLT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("STRINGLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "INT2STR": 
                tree += "[";
                Match("INT2STR");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("INTLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "FLT2INT": 
                tree += "[";
                Match("FLT2INT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("FLOATLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "FLT2STR": 
                tree += "[";
                Match("FLT2STR");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("FLOATLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            case "INT2FLT": 
                tree += "[";
                Match("INT2FLT");
                tree += ",";
                Match("LPAREN");
                tree += ",";
                Match("INTLIT");
                tree += ",";
                Match("RPAREN");
                tree += "]";
                break;
            default:
                Error();
                break;
        }
        tree += "]";
    }

    public static void Match(String token){
        if (token.equals("$"))
        {
            System.out.println("Done!");
            System.out.println(tree);
            System.exit(0);
        }
        x++;
        tree += "["+ token + " # []]";
    }
    public static void Error(){
        System.out.println("Error at index " + x);
        System.out.println("token: " + getBase(tokens.get(x)));
        System.out.println("source function: " + nameofCurrMethod);
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
            File output = new File("treetest.txt");
            if (output.createNewFile()) {
                System.out.println("File created: " + output.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        try {
            FileWriter myWriter = new FileWriter("treetest.txt");
	    myWriter.write(input);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}
