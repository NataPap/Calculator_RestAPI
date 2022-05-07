package program.entities;

public class ExpressionNotFoundException extends RuntimeException {
    public ExpressionNotFoundException(int id) {
        super("Could not find expression " + id);
    }
}