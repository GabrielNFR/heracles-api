package com.gabrielnfr.heracles_api.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "treino", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Execucao> execucoes = new ArrayList<>();

    @OneToMany(mappedBy = "treino", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Exercicio> exercicios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
