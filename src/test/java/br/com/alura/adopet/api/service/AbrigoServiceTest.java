package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService service;

    @Mock
    private AbrigoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveListarTodosOsAbrigos() {
        service.listar();

        then(repository).should().findAll();
    }

    @Test
    void deveListarTodosOsPetsDoAbrigoPorNome() {
        String nome = "Miau";

        given(repository.findByNome(nome)).willReturn(Optional.of(abrigo));

        service.listarPets(nome);

        then(petRepository).should().findByAbrigo(abrigo);
    }
}
