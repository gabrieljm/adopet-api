package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TutorTelefoneJaCadastradoValidation implements CadastroTutorValidation {

    @Autowired
    private TutorRepository repository;

    @Override
    public void validar(CadastroTutorDto dto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());

        if (telefoneJaCadastrado) {
            throw new ValidacaoException("Telefone já cadastrado para outro tutor!");
        }
    }
}
