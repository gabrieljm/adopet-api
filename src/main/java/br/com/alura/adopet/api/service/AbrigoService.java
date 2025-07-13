package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.validation.CadastroAbrigoValidation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private List<CadastroAbrigoValidation> validacoes;

    public void cadastrar(CadastroAbrigoDto dto) {
        validacoes.forEach(v -> v.validar(dto));

        Abrigo abrigo = new Abrigo(dto.nome(), dto.telefone(), dto.email());

        repository.save(abrigo);
    }

    public List<Pet> listarPets(String idOuNome) {
        try {
            Long id = Long.parseLong(idOuNome);

            return repository.getReferenceById(id).getPets();
        } catch (EntityNotFoundException enfe) {
            throw new EntityNotFoundException();
        } catch (NumberFormatException e) {
            try {
                return repository.findByNome(idOuNome).getPets();
            } catch (EntityNotFoundException enfe) {
                throw new EntityNotFoundException();
            }
        }
    }

    public void cadastrarPet(String idOuNome, Pet pet) {
        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = repository.getReferenceById(id);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);

            repository.save(abrigo);
        } catch (EntityNotFoundException enfe) {
            throw new EntityNotFoundException();
        } catch (NumberFormatException nfe) {
            try {
                Abrigo abrigo = repository.findByNome(idOuNome);
                pet.setAbrigo(abrigo);
                pet.setAdotado(false);
                abrigo.getPets().add(pet);

                repository.save(abrigo);
            } catch (EntityNotFoundException enfe) {
                throw new EntityNotFoundException();
            }
        }
    }
}
