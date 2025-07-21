package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validation.SolicitacaoAdocaoValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private EmailService emailService;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Spy
    private List<SolicitacaoAdocaoValidation> validacoes = new ArrayList<>();

    @Mock
    private SolicitacaoAdocaoValidation validador1;

    @Mock
    private SolicitacaoAdocaoValidation validador2;

    private SolicitacaoAdocaoDto dto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Test
    void deveSalvarAdocaoAoSolicitar() {
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");

        given(petRepository.getReferenceById(dto.petId())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.tutorId())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        service.solicitar(dto);

        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();

        assertEquals(pet, adocaoSalva.getPet());
        assertEquals(tutor, adocaoSalva.getTutor());
        assertEquals(dto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    void deveChamarValidadoresDeAdocaoAoSolicitar() {
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");

        given(petRepository.getReferenceById(dto.petId())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.tutorId())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        validacoes.add(validador1);
        validacoes.add(validador2);

        service.solicitar(dto);

        then(validador1).should().validar(dto);
        then(validador2).should().validar(dto);
    }
}
