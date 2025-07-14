package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailJaCadastradoValidation implements CadastroAbrigoValidation {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Override
    public void validar(CadastroAbrigoDto dto) {
        boolean emailJaCadastrado = abrigoRepository.existsByEmail(dto.email());

        if (emailJaCadastrado) {
            throw new ValidacaoException("Email já cadastrado para outro abrigo!");
        }
    }
}
