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
class TutorComLimiteDeAdocoesValidationTest {

    @InjectMocks
    private TutorComLimiteDeAdocoesValidation validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    SolicitacaoAdocaoDto dto;

    @Test
    void devePermitirSolicitacaoAdocaoPet() {
        given(adocaoRepository.countByTutorIdAndStatus(dto.tutorId(), StatusAdocao.APROVADO))
                .willReturn(1);

        assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void deveRecusarSolicitacaoAdocaoPet() {
        given(adocaoRepository.countByTutorIdAndStatus(dto.tutorId(), StatusAdocao.APROVADO))
                .willReturn(5);

        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }
}
