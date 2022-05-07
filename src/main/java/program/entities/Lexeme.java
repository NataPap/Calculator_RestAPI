package program.entities;

import lombok.Data;
import program.controllers.Controller;

import java.util.ArrayList;
import java.util.List;

@Data
public class Lexeme {
    Controller.LexemeType type;
    String value;
    public Lexeme(Controller.LexemeType type, String value){
        this.type=type;
        this.value=value;
    }
    public Lexeme(Controller.LexemeType type, Character value){
        this.type=type;
        this.value= value.toString();
    }
    @Override
    public String toString(){
        return "Lexeme {"+
                "type="+type+
                ", value='"+value+'\''+
                '}';
    }
}
