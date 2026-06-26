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
        log.info("🌱 Iniciando seed data e testes manuais...");

        try {
            log.info("--- PASSO 1: Criando treinos ---");
            Treino peito = treinoService.criar("Peito");
            Treino pernas = treinoService.criar("Pernas");
            log.info("Treino criado: {} (ID={})", peito.getNome(), peito.getId());
            log.info("Treino criado: {} (ID={})", pernas.getNome(), pernas.getId());

            log.info("--- PASSO 2: Listando todos os treinos ---");
            List<Treino> todos = treinoService.listarTodos();
            todos.forEach(t -> log.info("  Treino: {} (ID={})", t.getNome(), t.getId()));

            log.info("--- PASSO 3: Criando execucao para 'Peito' ---");
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
            execPeito = execucaoService.criar(peito.getId(), execPeito);
            log.info("Execucao criada: ID={}, dataHora={}, treino={}", 
                execPeito.getId(), execPeito.getDataHora(), execPeito.getTreino().getNome());
            execPeito.getExercicios().forEach(e -> 
                log.info("  Exercicio: {}, series={}, reps={}, carga={}kg, obs={}",
                    e.getNomeExercicio(), e.getSeries(), e.getRepeticoes(), e.getCarga(), e.getObs()));

            log.info("--- PASSO 4: Buscando execucao por ID={} ---", execPeito.getId());
            Execucao buscada = execucaoService.buscarPorId(execPeito.getId());
            log.info("Execucao encontrada: ID={}, treino={}, exercicios={}", 
                buscada.getId(), buscada.getTreino().getNome(), buscada.getExercicios().size());

            log.info("--- PASSO 5: Listando execucoes do treino 'Peito' ---");
            List<Execucao> execsPeito = execucaoService.listarPorTreino(peito.getId());
            execsPeito.forEach(e -> {
                log.info("  Execucao ID={}, dataHora={}", e.getId(), e.getDataHora());
                e.getExercicios().forEach(ex ->
                    log.info("    -> {} | {}x{} | {}kg", 
                        ex.getNomeExercicio(), ex.getSeries(), ex.getRepeticoes(), ex.getCarga()));
            });

            log.info("--- PASSO 6: Excluindo execucao ID={} ---", execPeito.getId());
            execucaoService.deletar(execPeito.getId());
            log.info("Execucao excluida. Listando execucoes do 'Peito' novamente:");
            List<Execucao> aposExclusao = execucaoService.listarPorTreino(peito.getId());
            log.info("  Total de execucoes restantes: {}", aposExclusao.size());

            log.info("--- PASSO 7: Excluindo treino 'Pernas' (sem execucoes) ---");
            treinoService.deletar(pernas.getId());
            log.info("Treino 'Pernas' excluido com sucesso!");
            todos = treinoService.listarTodos();
            log.info("Treinos restantes no banco: {}", todos.size());

            log.info("--- PASSO 8: Excluindo treino 'Peito' (ja sem execucoes) ---");
            treinoService.deletar(peito.getId());
            log.info("Treino 'Peito' excluido com sucesso!");
            todos = treinoService.listarTodos();
            log.info("Treinos restantes no banco: {}", todos.size());

            log.info("--- PASSO 9: Buscando ID inexistente 999 ---");
            try {
                treinoService.buscarPorId(999L);
            } catch (Exception e) {
                log.info("Excecao capturada (esperado): {}", e.getClass().getSimpleName() + " - " + e.getMessage());
            }

        } catch (Exception e) {
            log.error("Erro no DataLoader: {}", e.getMessage(), e);
        }

        log.info("Seed data e testes concluidos!");
    }
}
