package program.entities;

import lombok.Data;

import java.util.List;

@Data
public class LexemeBuffer {
    private int pos;
    public List<Lexeme> lexemes;

    public LexemeBuffer(List<Lexeme> lexemes){
        this.lexemes=lexemes;
    }
    public Lexeme next() {
        return lexemes.get(pos++);
    }
    public void back() {
        pos--;
    }
    public int getPos() {
        return pos;
    }
}
