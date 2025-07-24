package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService service;

    @MockBean
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveDevolverCodigo200ParaListagemDeAbrigos() throws Exception {
        var response = mvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveDevolverCodigo200ParaCadastroDeAbrigo() throws Exception {
        String json = """
                {
                    "nome": "Abrigo feliz",
                    "telefone": "(94)0000-9090",
                    "email": "email@example.com"
                }
                """;

        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveDevolverCodigo400ParaCadastroDeAbrigoComErros() throws Exception {
        String json = """
                {
                    "nome": "Abrigo feliz",
                    "telefone": "(94)0000-90900",
                    "email": "email@example.com"
                }
                """;

        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveDevolverCodigo200ParaListagemDePetsDoAbrigoPorNome() throws Exception {
        String nome = "Miau";

        var response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveDevolverCodigo200ParaListagemDePetsDoAbrigoPorId() throws Exception {
        String id = "1";

        var response = mvc.perform(
                get("/abrigos/{nome}/pets", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveDevolverCodigo404ParaListagemDePetsDoAbrigoPorIdInvalido() throws Exception {
        String id = "1";

        given(service.listarPets(id)).willThrow(ValidacaoException.class);

        var response = mvc.perform(
                get("/abrigos/{nome}/pets", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveDevolverCodigo404ParaListagemDePetsDoAbrigoPorNomeInvalido() throws Exception {
        String nome = "Miau";

        given(service.listarPets(nome)).willThrow(ValidacaoException.class);

        var response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveDevolverCodigo200ParaCadastroDePetPeloId() throws Exception {
        String abrigoId = "1";
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "padrao",
                    "idade": "5",
                    "cor": "Parda",
                    "peso": "6.4"
                }
                """;

        var response = mvc.perform(
                post("/abrigos/{abrigoId}/pets", abrigoId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveDevolverCodigo200ParaCadastroDePetPeloNome() throws Exception {
        String abrigoNome = "Abrigo feliz";
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "padrao",
                    "idade": "5",
                    "cor": "Parda",
                    "peso": "6.4"
                }
                """;

        var response = mvc.perform(
                post("/abrigos/{abrigoNome}/pets", abrigoNome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveDevolverCodigo404ParaCadastroDePetEmAbrigoNaoEncontradoPeloId() throws Exception {
        String abrigoId = "1";
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "padrao",
                    "idade": "5",
                    "cor": "Parda",
                    "peso": "6.4"
                }
                """;

        given(service.carregarAbrigo(abrigoId)).willThrow(ValidacaoException.class);

        var response = mvc.perform(
                post("/abrigos/{abrigoId}/pets", abrigoId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveDevolverCodigo404ParaCadastroDePetEmAbrigoNaoEncontradoPeloNome() throws Exception {
        String abrigoNome = "Abrigo legal";
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "padrao",
                    "idade": "5",
                    "cor": "Parda",
                    "peso": "6.4"
                }
                """;

        given(service.carregarAbrigo(abrigoNome)).willThrow(ValidacaoException.class);

        var response = mvc.perform(
                post("/abrigos/{abrigoNome}/pets", abrigoNome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveDevolverCodigo400ParaCadastroDePetInvalido() throws Exception {
        String id = "1";
        String json = """
                {
                    "tipo": "GAT",
                    "nome": "Miau",
                    "raca": "padrao",
                    "idade": "5",
                    "cor": "Parda",
                    "peso": "6.4"
                }
                """;

        var response = mvc.perform(
                post("/abrigos/{id}/pets", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
}
