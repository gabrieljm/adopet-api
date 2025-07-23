package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TutorComAdocaoEmAndamentoValidationTest {

    @InjectMocks
    TutorComAdocaoEmAndamentoValidation validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void devePermitirSolicitacaoAdocaoPet() {
        given(adocaoRepository.existsByTutorIdAndStatus(dto.tutorId(), StatusAdocao.AGUARDANDO_AVALIACAO))
                .willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void deveRecusarSolicitacaoAdocaoPet() {
        given(adocaoRepository.existsByTutorIdAndStatus(dto.tutorId(), StatusAdocao.AGUARDANDO_AVALIACAO))
                .willReturn(true);

        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }
}
