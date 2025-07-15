package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbrigoTelefoneJaCadastradoValidation implements CadastroAbrigoValidation {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Override
    public void validar(CadastroAbrigoDto dto) {
        boolean telefoneJaCadastrado = abrigoRepository.existsByTelefone(dto.telefone());

        if (telefoneJaCadastrado) {
            throw new ValidacaoException("Telefone já cadastrado para outro abrigo!");
        }
    }
}
