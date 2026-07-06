package com.gabrielnfr.heracles_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.Exercicio;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.model.Treino;
import com.gabrielnfr.heracles_api.repository.ExecucaoRepository;
import com.gabrielnfr.heracles_api.repository.ExercicioRepository;
import com.gabrielnfr.heracles_api.repository.TreinoRepository;
import com.gabrielnfr.heracles_api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ExecucaoServiceTest {
    @Mock
    private ExecucaoRepository execucaoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TreinoRepository treinoRepository;
    @Mock
    private ExercicioRepository exercicioRepository;
    @InjectMocks
    private ExecucaoService execucaoService;

    private Usuario usuario;
    private Treino treino;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);

        treino = new Treino();
        treino.setId(10L);
        treino.setNome("Peito");
        treino.setUsuario(usuario);
        treino.setExecucoes(new ArrayList<>());
    }

    @Test
    void deveCriarExecucao() {
        Exercicio supino = new Exercicio();
        supino.setId(1L);
        supino.setNome("Supino");
        supino.setTreino(treino);

        ExercicioRealizado realizado = new ExercicioRealizado();
        realizado.setExercicio(supino);
        realizado.setSeries(3);
        realizado.setRepeticoes("10");
        realizado.setCarga(60.0);
        
        Execucao dados = new Execucao();
        dados.setDataHora(LocalDateTime.now());
        dados.setExercicios(List.of(realizado));

        Execucao execucaoSalva = new Execucao();
        execucaoSalva.setId(5L);
        execucaoSalva.setDataHora(dados.getDataHora());
        execucaoSalva.setTreino(treino);
        execucaoSalva.setExercicios(List.of(realizado));
        
        when(treinoRepository.findById(10L)).thenReturn(Optional.of(treino));
        when(exercicioRepository.findById(1L)).thenReturn(Optional.of(supino));
        when(execucaoRepository.save(any(Execucao.class))).thenReturn(execucaoSalva);

        Execucao resultado = execucaoService.criar(10L, dados, 1L);

        assertEquals(5L, resultado.getId());
        assertEquals(1, resultado.getExercicios().size());
        verify(treinoRepository).findById(10L);
        verify(exercicioRepository).findById(1L);
        verify(execucaoRepository).save(any(Execucao.class));
    }

    @Test
    void deveListarTodos() {
        List<Execucao> listaMock = List.of(new Execucao(), new Execucao());

        when(treinoRepository.findById(10L)).thenReturn(Optional.of(treino));
        when(execucaoRepository.findByTreinoId(10L)).thenReturn(listaMock);

        List<Execucao> resultado = execucaoService.listarPorTreino(10L, 1L);

        assertEquals(2, resultado.size());
        verify(treinoRepository).findById(10L);
        verify(execucaoRepository).findByTreinoId(10L);
    }

    @Test
    void deveBuscarExecucaoPorId() {
        Execucao execucao = new Execucao();
        execucao.setId(7L);
        execucao.setTreino(treino);

        when(execucaoRepository.findById(7L)).thenReturn(Optional.of(execucao));

        Execucao resultado = execucaoService.buscarPorId(7L, 1L);

        assertEquals(7L, resultado.getId());
        verify(execucaoRepository).findById(7L);
    }

    @Test
    void deveDeletarExecucaoExistente() {
        Execucao execucao = new Execucao();
        execucao.setId(7L);
        execucao.setTreino(treino);

        when(execucaoRepository.findById(7L)).thenReturn(Optional.of(execucao));

        execucaoService.deletar(7L, 1L);

        verify(execucaoRepository).delete(execucao);
    }

    @Test
    void deveLancarExcecaoQuandoAcharExecucaoDeOutroUsuario() {
        Usuario outro = new Usuario();
        outro.setId(99L);

        Treino treinoOutro = new Treino();
        treinoOutro.setId(20L);
        treinoOutro.setUsuario(outro);

        Execucao execucao = new Execucao();
        execucao.setId(5L);
        execucao.setTreino(treinoOutro);

        when(execucaoRepository.findById(5L)).thenReturn(Optional.of(execucao));

        assertThrows(EntityNotFoundException.class,
            () -> execucaoService.buscarPorId(5L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoDeletarExecucaoDeOutroUsuario() {
        Usuario outro = new Usuario(); outro.setId(99L);
        Treino treinoOutro = new Treino(); treinoOutro.setId(20L); treinoOutro.setUsuario(outro);
        Execucao execucao = new Execucao(); execucao.setId(5L); execucao.setTreino(treinoOutro);
        when(execucaoRepository.findById(5L)).thenReturn(Optional.of(execucao));

        assertThrows(EntityNotFoundException.class,
            () -> execucaoService.deletar(5L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoListarTreinoDeOutroUsuario() {
        Usuario outro = new Usuario(); outro.setId(99L);
        Treino treinoOutro = new Treino(); treinoOutro.setId(20L); treinoOutro.setUsuario(outro);
        when(treinoRepository.findById(20L)).thenReturn(Optional.of(treinoOutro));

        assertThrows(EntityNotFoundException.class,
            () -> execucaoService.listarPorTreino(20L, 1L));
    }
}
