package program.entities;

import lombok.Data;
import program.controllers.Controller;

import java.util.ArrayList;
import java.util.List;

enum LexemeType {
    LEFT_BRACKET,RIGHT_BRACKET,PLUS,MINUS,MUL,DIV,NUMBER,EOF
}
@Data
public class Lexeme {
    LexemeType type;
    String value;
    public Lexeme(LexemeType type, String value){
        this.type=type;
        this.value=value;
    }
    public Lexeme(LexemeType type, Character value){
        this.type=type;
        this.value= String.valueOf(value);
    }
    public List<Lexeme> analyzeOfLexeme(String expression) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int position = 0;
        while (position < expression.length()) {
        char c=expression.charAt(position);
        switch (c){
            case'(':
                lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET,c));
                position++;
                continue;
            case')':
                lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET,c));
                position++;
                continue;
            case'+':
                lexemes.add(new Lexeme(LexemeType.PLUS,c));
                position++;
                continue;
            case'-':
                lexemes.add(new Lexeme(LexemeType.MINUS,c));
                position++;
                continue;
            case'*':
                lexemes.add(new Lexeme(LexemeType.MUL,c));
                position++;
                continue;
            case'/':
                lexemes.add(new Lexeme(LexemeType.DIV,c));
                position++;
                continue;
            default:
                if(c<='9' && c>='0'){
                    StringBuilder sb=new StringBuilder();
                    do {
                        sb.append(c);
                        position++;
                        if(position>expression.length()){
                            break;
                        }
                        c=expression.charAt(position);
                    }while (c<='9' && c>='0');
            }
        }
        }
        return lexemes;
    }
}
