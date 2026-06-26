package com.gabrielnfr.heracles_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "execucoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Execucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treino_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Treino treino;

    @OneToMany(mappedBy = "execucao", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ExercicioRealizado> exercicios = new ArrayList<>();
}
