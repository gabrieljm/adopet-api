package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PetDisponivelValidationTest {

    @InjectMocks
    private PetDisponivelValidation validacao;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Pet pet;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void devePermitirSolicitacaoAdocaoPet() {
        given(petRepository.getReferenceById(dto.petId())).willReturn(pet);
        given(pet.getAdotado()).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void deveRecusarSolicitacaoAdocaoPet() {
        given(petRepository.getReferenceById(dto.petId())).willReturn(pet);
        given(pet.getAdotado()).willReturn(true);

        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }
}
