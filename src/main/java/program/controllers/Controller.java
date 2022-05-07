package program.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import program.entities.Expression;
import program.repositories.CalcRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final CalcRepository calcRepository;


    @GetMapping("/")
        public List<Expression> index() {
            return calcRepository.findAll();
    }
    @PostMapping("/add")
    Expression newExpression(@RequestBody  String newExp) {
Expression newExpression=new Expression();


                return calcRepository.save(newExpression);
    }


}
