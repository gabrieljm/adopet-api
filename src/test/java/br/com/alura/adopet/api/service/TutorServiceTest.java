package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService service;

    @Mock
    private TutorRepository repository;

    @Mock
    private CadastroTutorDto dto;

    @Mock
    private AtualizacaoTutorDto atualizacaoDto;

    @Mock
    private Tutor tutor;

    @Test
    void deveRecusarCadastroDeTutorComTelefoneOuEmailJaCadastrados() {
        given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email()))
                .willReturn(true);

        assertThrows(ValidacaoException.class, () -> service.cadastrar(dto));
    }

    @Test
    void deveCadastrarComSucesso() {
        given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email()))
                .willReturn(false);

        assertDoesNotThrow(() -> service.cadastrar(dto));

        then(repository).should().save(new Tutor(dto));
    }

    @Test
    void deveAtualizarTutor() {
        given(repository.getReferenceById(atualizacaoDto.id()))
                .willReturn(tutor);

        service.atualizar(atualizacaoDto);

        then(tutor).should().atualizarDados(atualizacaoDto);
    }
}
