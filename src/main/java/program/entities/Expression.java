package program.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
//@NamedQuery(name = "Expressions.findByResult", query = "SELECT exp FROM tbl_expressions" +
//        " WHERE exp.result = key")
@Table(name = "tbl_expressions")
public class Expression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String expression;
    @Column(nullable = false)
    private Double result;

}

