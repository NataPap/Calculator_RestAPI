package program.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import program.entities.Expression;
import program.entities.Lexeme;
import program.entities.LexemeBuffer;
import program.repositories.CalcRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class Controller {

    private final CalcRepository calcRepository;

    public enum LexemeType {
        LEFT_BRACKET, RIGHT_BRACKET, PLUS, MINUS, MUL, DIV, NUMBER, EOF
    }

    @GetMapping("/")
    public List<Expression> index() {
        return calcRepository.findAll();
    }

    @PostMapping("/add")
    public Expression newExpression(@RequestBody String newExp) {
        List<Lexeme> lexemes = analyzeOfLexeme(newExp);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        Double res = expr(lexemeBuffer);
        System.out.println(res);
        Expression newExpression = new Expression();
        newExpression.setExpression(newExp);
        newExpression.setResult(res);
        return calcRepository.save(newExpression);
    }

    public List<Lexeme> analyzeOfLexeme(String expression) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int position = 0;
        while (position < expression.length()) {
            char c = expression.charAt(position);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
                    position++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
                    position++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.PLUS, c));
                    position++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.MINUS, c));
                    position++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.MUL, c));
                    position++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.DIV, c));
                    position++;
                    continue;
                default:
                    if (c <= '9' && c >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            position++;
                            if (position >= expression.length()) {
                                break;
                            }
                            c = expression.charAt(position);
                        } while (c <= '9' && c >= '0' || c == '.');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Erroneous indicator in the expression:" + c);
                        }
                        position++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        /* for (int i = 0; i < lexemes.size(); i++) {
            for (int i2 = 1; i2 < lexemes.size(); i2++) {
                if ((lexemes.get(i).type == LexemeType.PLUS
                        || lexemes.get(i).type == LexemeType.MINUS
                        || lexemes.get(i).type == LexemeType.MUL
                        || lexemes.get(i).type == LexemeType.DIV)
                        && (lexemes.get(i2).type == LexemeType.MUL
                        || lexemes.get(i2).type == LexemeType.DIV)) {
                    throw new RuntimeException("Incorrect syntax of the expression:"
                            + lexemes.get(i).value + lexemes.get(i2).value);
                }
            }
        }*/
        return lexemes;
    }


    public Double expr(LexemeBuffer lexemeBuffer) {
        Lexeme lexeme = lexemeBuffer.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0.0;
        } else {
            lexemeBuffer.back();
            return plusMinus(lexemeBuffer);
        }
    }

    public Double plusMinus(LexemeBuffer lexemeBuffer) {
        Double value = mulDiv(lexemeBuffer);
        while (true) {
            Lexeme lexeme = lexemeBuffer.next();
            switch (lexeme.type) {
                case PLUS:
                    value += mulDiv(lexemeBuffer);
                    break;
                case MINUS:
                    value -= mulDiv(lexemeBuffer);
                    break;
                default:
                    lexemeBuffer.back();
                    return value;
            }
        }
    }

    public Double mulDiv(LexemeBuffer lexemeBuffer) {
        Double value = factor(lexemeBuffer);
        while (true) {
            Lexeme lexeme = lexemeBuffer.next();
            switch (lexeme.type) {
                case MUL:
                    value *= factor(lexemeBuffer);
                    break;
                case DIV:
                    value /= factor(lexemeBuffer);
                    break;
                default:
                    lexemeBuffer.back();
                    return value;
            }
        }
    }

    public Double factor(LexemeBuffer lexemeBuffer) {
        Lexeme lexeme = lexemeBuffer.next();
        switch (lexeme.type) {
            case MINUS:
                Double val = factor(lexemeBuffer);
                return -val;
            case NUMBER:
                return Double.parseDouble(lexeme.value);
            case LEFT_BRACKET:
                Double value = expr(lexemeBuffer);
                lexeme = lexemeBuffer.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException(":" + lexeme.value
                            + " at position" + lexemeBuffer.getPos());
                }
                return value;
            default:
                throw new RuntimeException("No right bracket:" + lexeme.value
                        + " at position" + lexemeBuffer.getPos());
        }
    }

    @RequestMapping("/search")


//    public List<Expression> search(@RequestParam Double key) {
//        List<Expression> result = CalcRepository.search(key );
//        ModelAndView mav = new ModelAndView("search");
//        mav.addObject("result", result);
//
//        return mav;
//    }
//    public List<Expression> search(Double keyword) {
//        return repo.search(keyword);
//  }

    @GetMapping("/{id}")
    public Expression singleExpression(@PathVariable int id) {
        return calcRepository.findById(id).get();
    }

    @PutMapping("/{id}")
    Expression replaceExpression(@RequestBody Expression newExpression, @PathVariable int id) {
        Expression item = calcRepository.findById(id).get();
        try {
            //analyzeOfLexeme("2+2");
            List<Lexeme> newLex = analyzeOfLexeme(newExpression.getExpression());
            LexemeBuffer buffer = new LexemeBuffer(newLex);
            item.setResult(expr(buffer));
            item.setExpression(newExpression.getExpression());
            //item.setResult(newExpression.getResult());
            calcRepository.save(item);
            return item;
        } catch (Exception ex) {
            return null;
        }
    }

}
