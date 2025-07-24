package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validation.SolicitacaoAdocaoValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Mock
    private AprovacaoAdocaoDto aprovacaoDto;

    @Mock
    private ReprovacaoAdocaoDto reprovacaoDto;

    @Spy
    private Adocao adocao;

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

    @Test
    void deveEnviarEmailAoSolicitar() {
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");

        given(petRepository.getReferenceById(dto.petId())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.tutorId())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        service.solicitar(dto);

        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();

        then(emailService).should().enviarEmail(
                adocaoSalva.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocaoSalva.getPet().getAbrigo().getNome() + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocaoSalva.getPet().getNome() + ". \n\nFavor avaliar para aprovação ou reprovação."
        );
    }

    @Test
    void deveAprovarAdocao() {
        given(repository.getReferenceById(aprovacaoDto.adocaoId())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        service.aprovar(aprovacaoDto);

        then(adocao).should().aprovar();
        assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
    }

    @Test
    void deveReprovarAdocao() {
        given(repository.getReferenceById(reprovacaoDto.adocaoId())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        service.reprovar(reprovacaoDto);

        then(adocao).should().reprovar(reprovacaoDto.justificativa());
        assertEquals(StatusAdocao.REPROVADO, adocao.getStatus());
    }
}
