package com.gabrielnfr.heracles_api.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.dialect.function.IntegralTimestampaddFunction;

@Entity
@Table(name = "treinos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Treino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;
}
