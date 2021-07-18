package ex;

import java.util.List;


public class Parser {

    int pos = 0;
    List<Token> tokens;

    public Parser() {	}


    public Node analyse(List<Token> tokens) throws Exception{
        this.tokens = tokens;
        pos = 0;
        Node root = new Node(TokenClass.nBlock);

        while(!isEOF()) {
            Token t = getNextToken();
            switch(t.getCl()) {

                case nProc:
                    root.appendNode(kProc(t));
                    break;
                case nCall:
                    root.appendNode(kCall(t));
                    break;
                case nColor:
                    root.appendNode(kColor(t));
                    break;
                case nForward:
                    root.appendNode(kForward(t));
                    break;
                case nLeft:
                    root.appendNode(kLeft(t));
                    break;
                case nRight:
                    root.appendNode(kRight(t));
                    break;
                case nRepeat:
                    root.appendNode(kRepeat(t));
                    break;
                default: throw UnexpextedToken();

            }

        }
        return root;
    }

    private Node blockStart(Token t) throws Exception {
        Node n = new Node(TokenClass.nBlock);
        while(!isEOF()) {
            Token to = getNextToken();
            switch(to.getCl()) {

                case nProc:
                    n.appendNode(kProc(t));
                    break;
                case nCall:
                    n.appendNode(kCall(to));
                    break;
                case nColor:
                    n.appendNode(kColor(to));
                    break;
                case nForward:
                    n.appendNode(kForward(to));
                    break;
                case nLeft:
                    n.appendNode(kLeft(to));
                    break;
                case nRight:
                    n.appendNode(kRight(to));
                    break;

                case nRepeat:
                    n.appendNode(kRepeat(t));
                    break;

                case rightPar: return n;

                default: throw UnexpextedToken();

            }

        }
        throw new Exception("Missing block end!");
    }


                 //S' → nProc ident [A]
    private Node kProc(Token t) throws Exception {
        if(viewNextToken() == TokenClass.ident) {
            Node n = new Node(TokenClass.nProc, ident(getNextToken()));
            if(viewNextToken() == TokenClass.leftPar) {
                n.appendNode(blockStart(getNextToken()));
            }
            return n;
        }
        else {
            throw UnexpextedToken();
        }
    }

                // A → nRepeat intval [A]
    private Node kRepeat(Token t) throws Exception {
        if(viewNextToken() == TokenClass.intVal) {
            Node n = new Node(TokenClass.nRepeat, intVal(getNextToken()));
            if(viewNextToken() == TokenClass.leftPar)
                n.appendNode(blockStart(getNextToken()));
            return n;
        }
        else {
            throw UnexpextedToken();
        }
    }

    private String ident(Token t) {
        return t.getValue();
    }

            //S' → nCall ident
    private Node kCall(Token t) throws Exception {
        if(viewNextToken() == TokenClass.ident) {
            return new Node(TokenClass.nCall, ident(getNextToken()));
        }
        else {
            throw UnexpextedToken();
        }
    }


        private String intVal(Token t) {
        return t.getValue();
    }

            // S'→ nColor intval
    private Node kColor(Token t) throws Exception {
        if(viewNextToken() == TokenClass.intVal) {
            return new Node(TokenClass.nColor, intVal(getNextToken()));
        }
        else {
            throw UnexpextedToken();
        }
    }

            // A→ nForward intval
    private Node kForward(Token t) throws Exception {
        if(viewNextToken() == TokenClass.intVal) {
            return new Node(TokenClass.nForward, intVal(getNextToken()));
        }
        else {
            throw UnexpextedToken();
        }
    }

            // A→ nLeft intval
    private Node kLeft(Token t) throws Exception {
        if(viewNextToken() == TokenClass.intVal) {
            return new Node(TokenClass.nLeft, intVal(getNextToken()));
        }
        else {
            throw UnexpextedToken();
        }
    }

            // A→ nRight intval
        private Node kRight(Token t) throws Exception {
        if(viewNextToken() == TokenClass.intVal) {
            return new Node(TokenClass.nRight, intVal(getNextToken()));
        }
        else {
            throw UnexpextedToken();
        }
    }




    /*Other Methods*/
    private boolean isEOF() {
        return pos >= tokens.size();
    }

    private Token getNextToken() {
        return (!isEOF()) ? tokens.get(pos++) : null;
    }

    private TokenClass viewNextToken() {
        return (!isEOF()) ? tokens.get(pos).getCl() : null;
    }

    private Exception UnexpextedToken(){
        return new Exception("Unexpected Token: "+viewNextToken()+ " at " + pos );
    }






}