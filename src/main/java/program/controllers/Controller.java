package program.controllers;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import program.entities.Expression;
import program.entities.Lexeme;
import program.repositories.CalcRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final CalcRepository calcRepository;
    public enum LexemeType {
        LEFT_BRACKET,RIGHT_BRACKET,PLUS,MINUS,MUL,DIV,NUMBER,EOF
    }

    @GetMapping("/")
        public List<Expression> index() {
            return calcRepository.findAll();
    }
    @PostMapping("/add")
    Expression newExpression(@RequestBody  String newExp) {
        List<Lexeme> lexemes = analyzeOfLexeme(newExp);



Expression newExpression=new Expression();
                return calcRepository.save(newExpression);
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
                        lexemes.add(new Lexeme(LexemeType.NUMBER,sb.toString()));
                    }else {
                        if(c!=' '){
                            throw new RuntimeException("Unexpected character:"+c);
                        }
                        position++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF,""));
        return lexemes;
    }


}
