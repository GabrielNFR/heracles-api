package com.gabrielnfr.heracles_api.model;

import java.util.concurrent.ExecutionException;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercicios_realizados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercicioRealizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String nomeExercicio;

    @Column
    private Integer series;

    @Column
    private String repeticoes;

    @Column(nullable = false)
    private Double carga;

    @Column(length = 200)
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execucao_id", nullable = false)
    private Execucao execucao;
}
