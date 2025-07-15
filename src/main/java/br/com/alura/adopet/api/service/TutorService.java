package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validation.CadastroTutorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    @Autowired
    private List<CadastroTutorValidation> validacoes;

    public void cadastrar(CadastroTutorDto dto) {
        validacoes.forEach(v -> v.validar(dto));

        Tutor tutor = new Tutor(dto.nome(), dto.telefone(), dto.email());

        repository.save(tutor);
    }
}
