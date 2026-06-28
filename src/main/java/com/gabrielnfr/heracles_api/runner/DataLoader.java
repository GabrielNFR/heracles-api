package com.gabrielnfr.heracles_api.runner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.model.Treino;
import com.gabrielnfr.heracles_api.service.ExecucaoService;
import com.gabrielnfr.heracles_api.service.TreinoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final TreinoService treinoService;
    private final ExecucaoService execucaoService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Iniciando seed data...");

        try {
            Treino peito = treinoService.criar("Peito");
            Treino pernas = treinoService.criar("Pernas");
            Treino costas = treinoService.criar("Costas");
            log.info("Treinos criados: Peito (ID={}), Pernas (ID={}), Costas (ID={})",
                peito.getId(), pernas.getId(), costas.getId());

            Execucao execPeito = new Execucao();
            execPeito.setDataHora(LocalDateTime.now());

            ExercicioRealizado ex1 = new ExercicioRealizado();
            ex1.setNomeExercicio("Supino reto");
            ex1.setSeries(3);
            ex1.setRepeticoes("10");
            ex1.setCarga(60.0);
            ex1.setObs("aquecimento leve");

            ExercicioRealizado ex2 = new ExercicioRealizado();
            ex2.setNomeExercicio("Crucifixo");
            ex2.setSeries(3);
            ex2.setRepeticoes("12");
            ex2.setCarga(20.0);

            execPeito.setExercicios(new ArrayList<>(List.of(ex1, ex2)));
            execucaoService.criar(peito.getId(), execPeito);

            Execucao execCostas = new Execucao();
            execCostas.setDataHora(LocalDateTime.now().minusDays(1));

            ExercicioRealizado ex3 = new ExercicioRealizado();
            ex3.setNomeExercicio("Puxada frente");
            ex3.setSeries(4);
            ex3.setRepeticoes("8-10");
            ex3.setCarga(50.0);

            execCostas.setExercicios(new ArrayList<>(List.of(ex3)));
            execucaoService.criar(costas.getId(), execCostas);

            log.info("Seed data concluido: 3 treinos, 2 execucoes, 3 exercicios");
        } catch (Exception e) {
            log.error("Erro no DataLoader: {}", e.getMessage(), e);
        }
    }
}
