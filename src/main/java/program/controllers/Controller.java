package program.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import program.entities.Expression;
import program.entities.ExpressionNotFoundException;
import program.entities.Lexeme;
import program.entities.LexemeBuffer;
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
    public Expression newExpression(@RequestBody  String newExp) {
        List<Lexeme> lexemes = analyzeOfLexeme(newExp);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        Double res=expr(lexemeBuffer);
        System.out.println(res);
        Expression newExpression=new Expression();
        newExpression.setExpression(newExp);
        newExpression.setResult(res);
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
                            if(position>=expression.length()){
                                break;
                            }
                            c=expression.charAt(position);
                        }while (c<='9' && c>='0'|| c=='.');
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

    public Double expr (LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if(lexeme.type==LexemeType.EOF) {
            return 0.0;
        } else {
            lexemes.back();
            return plusMinus(lexemes);
        }
    }

    public Double plusMinus (LexemeBuffer lexemes) {
        Double value = mulDiv(lexemes);
        while (true) {
            Lexeme lexeme=lexemes.next();
            switch (lexeme.type){
                case PLUS:
                    value+=mulDiv (lexemes);
                    break;
                case MINUS:
                    value-=mulDiv(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public Double mulDiv (LexemeBuffer lexemes) {
        Double value = factor(lexemes);
        while (true) {
            Lexeme lexeme=lexemes.next();
            switch (lexeme.type){
                case MUL:
                    value*=factor (lexemes);
                    break;
                case DIV:
                    value/=factor(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public Double factor (LexemeBuffer lexemes) {
    Lexeme lexeme=lexemes.next();
    switch (lexeme.type) {
        case MINUS:
            Double val = factor(lexemes);
            return -val;
        case NUMBER:
            return Double.parseDouble(lexeme.value);
        case LEFT_BRACKET:
            Double value = expr (lexemes);
            lexeme=lexemes.next();
            if(lexeme.type!=LexemeType.RIGHT_BRACKET) {
                throw new RuntimeException("Unexpected token:" + lexeme.value
                +" at position"+ lexemes.getPos());
            }
            return value;
        default:
            throw new RuntimeException("Unexpected token:" + lexeme.value
                    +" at position"+ lexemes.getPos());
    }
    }

//    @GetMapping("/{id}")
//    public Expression singleExpression(@PathVariable int id) {
//        return CalcRepository.findById(id)
//                .orElseThrow(() -> new ExpressionNotFoundException(id));
//    }
//
//    @PutMapping("/{id}")
//    Expression replaceExpression(@RequestBody Expression newExpression, @PathVariable int id) {
//
//
//        return CalcRepository.findById(id)
//                .map(exp -> {
//                    exp.setExpression(newExpression.getExpression());
//                    exp.setResult(newExpression.getResult());
//                    return CalcRepository.save(exp);
//                })
//                .orElseGet(() -> {
//                    newExpression.setId(id);
//                    return CalcRepository.save(newExpression);
//                });
//    }
}
