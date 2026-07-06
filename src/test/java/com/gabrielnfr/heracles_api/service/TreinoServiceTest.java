package com.gabrielnfr.heracles_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielnfr.heracles_api.model.Treino;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.TreinoRepository;
import com.gabrielnfr.heracles_api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TreinoServiceTest {
    @Mock
    private TreinoRepository treinoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private TreinoService treinoService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
    }

    @Test
    void deveCriarTreinoComExercicios() {
        Treino treinoSalvo = new Treino();
        treinoSalvo.setId(10L);
        treinoSalvo.setNome("Peito");
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuario);
        when(treinoRepository.save(any())).thenReturn(treinoSalvo);

        Treino resultado = treinoService.criar(usuario, "Peito", List.of("Supino", "Crucifixo"));

        assertEquals("Peito", resultado.getNome());
        assertEquals(10L, resultado.getId());
        verify(usuarioRepository).getReferenceById(1L);
        verify(treinoRepository).save(any(Treino.class));
    }

    @Test
    void deveListarTodos() {
        List<Treino> listaMock = List.of(new Treino(), new Treino());
        when(treinoRepository.findByUsuarioId(1L)).thenReturn(listaMock);

        List<Treino> resultado = treinoService.listarTodos(1L);

        assertEquals(2, resultado.size());
        verify(treinoRepository).findByUsuarioId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoTreinoNaoEncontrado() {
        when(treinoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> treinoService.buscarPorId(99L, 1L));
    }

    @Test
    void deveDeletarTreinoExistente() {
        Treino treino = new Treino();
        treino.setId(10L);
        treino.setUsuario(usuario);
        when(treinoRepository.findById(10L)).thenReturn(Optional.of(treino));

        treinoService.deletar(10L, 1L);

        verify(treinoRepository).delete(treino);
    }

    @Test
    void deveLancarExcecaoQuandoAcharTreinoDeOutroUsuario() {
        Usuario outro = new Usuario();
        outro.setId(99L);

        Treino treino = new Treino();
        treino.setId(10L);
        treino.setUsuario(outro);
        when(treinoRepository.findById(10L)).thenReturn(Optional.of(treino));

        assertThrows(EntityNotFoundException.class,
                () -> treinoService.buscarPorId(10L, 1L));
        
    }

    @Test
    void deveLancarExcecaoQuandoDeletarTreinoDeOutroUsuario() {
        Usuario outro = new Usuario();
        outro.setId(99L);

        Treino treino = new Treino();
        treino.setId(10L);
        treino.setUsuario(outro);
        when(treinoRepository.findById(10L)).thenReturn(Optional.of(treino));

        assertThrows(EntityNotFoundException.class,
                () -> treinoService.deletar(10L, 1L));
        
    }

    @Test
    void deveBuscarTreinoPorId() {
        Treino treino = new Treino();
        treino.setId(10L);
        treino.setNome("Peito");
        treino.setUsuario(usuario);
        when(treinoRepository.findById(10L)).thenReturn(Optional.of(treino));

        Treino resultado = treinoService.buscarPorId(10L, 1L);

        assertEquals("Peito", resultado.getNome());
    }
}
