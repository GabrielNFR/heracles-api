package com.gabrielnfr.heracles_api.runner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.Exercicio;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.model.Treino;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.UsuarioRepository;
import com.gabrielnfr.heracles_api.service.ExecucaoService;
import com.gabrielnfr.heracles_api.service.TreinoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("!prod")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final TreinoService treinoService;
    private final ExecucaoService execucaoService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Iniciando seed data...");

        try {
            Usuario usuario;
            if (usuarioRepository.findByEmail("teste@email.com").isEmpty()) {
                usuario = new Usuario();
                usuario.setEmail("teste@email.com");
                usuario.setUsername("Teste");
                usuario.setPasswordHash(passwordEncoder.encode("123456"));
                usuario.setDataCriacao(LocalDateTime.now());
                usuario = usuarioRepository.save(usuario);
            } else {
                usuario = usuarioRepository.findByEmail("teste@email.com").get();
            }

            Treino peito = treinoService.criar(usuario, "Peito", List.of("Supino reto", "Crucifixo", "Cross-over"));
            Treino pernas = treinoService.criar(usuario, "Pernas", List.of("Agachamento", "Leg press", "Cadeira extensora"));
            Treino costas = treinoService.criar(usuario, "Costas", List.of("Puxada frente", "Remada curvada"));
            log.info("Treinos criados com exercicios: Peito, Pernas, Costas");

            Execucao execPeito = new Execucao();
            execPeito.setDataHora(LocalDateTime.now());

            Exercicio supinoRef = new Exercicio();
            supinoRef.setId(peito.getExercicios().get(0).getId());

            ExercicioRealizado ex1 = new ExercicioRealizado();
            ex1.setExercicio(supinoRef);
            ex1.setSeries(3);
            ex1.setRepeticoes("10");
            ex1.setCarga(60.0);
            ex1.setObs("aquecimento leve");

            Exercicio crucifixoRef = new Exercicio();
            crucifixoRef.setId(peito.getExercicios().get(1).getId());

            ExercicioRealizado ex2 = new ExercicioRealizado();
            ex2.setExercicio(crucifixoRef);
            ex2.setSeries(3);
            ex2.setRepeticoes("12");
            ex2.setCarga(20.0);

            execPeito.setExercicios(new ArrayList<>(List.of(ex1, ex2)));
            execucaoService.criar(peito.getId(), execPeito, usuario.getId());

            Execucao execCostas = new Execucao();
            execCostas.setDataHora(LocalDateTime.now().minusDays(1));

            Exercicio puxadaRef = new Exercicio();
            puxadaRef.setId(costas.getExercicios().get(0).getId());

            ExercicioRealizado ex3 = new ExercicioRealizado();
            ex3.setExercicio(puxadaRef);
            ex3.setSeries(4);
            ex3.setRepeticoes("8-10");
            ex3.setCarga(50.0);

            execCostas.setExercicios(new ArrayList<>(List.of(ex3)));
            execucaoService.criar(costas.getId(), execCostas, usuario.getId());

            log.info("Seed data concluido: 3 treinos, 8 exercicios, 2 execucoes");
        } catch (Exception e) {
            log.error("Erro no DataLoader: {}", e.getMessage(), e);
        }
    }
}
