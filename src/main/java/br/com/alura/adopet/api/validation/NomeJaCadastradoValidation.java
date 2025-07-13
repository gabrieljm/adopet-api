package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class NomeJaCadastradoValidation implements CadastroAbrigoValidation {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Override
    public void validar(CadastroAbrigoDto dto) {
        boolean nomeJaCadastrado = abrigoRepository.existsByNome(dto.nome());

        if (nomeJaCadastrado) {
            throw new ValidacaoException("Nome já cadastrado para outro abrigo!");
        }
    }
}
