package br.com.alura.adopet.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "adocoes")
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    private Pet pet;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private StatusAdocao status;

    private String justificativaStatus;

    public Adocao() {}

    public Adocao(Pet pet, Tutor tutor, String motivo) {
        this.pet = pet;
        this.tutor = tutor;
        this.motivo = motivo;
        this.data = LocalDateTime.now();
        this.status = StatusAdocao.AGUARDANDO_AVALIACAO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adocao adocao = (Adocao) o;
        return Objects.equals(id, adocao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Pet getPet() {
        return pet;
    }

    public String getMotivo() {
        return motivo;
    }

    public StatusAdocao getStatus() {
        return status;
    }

    public String getJustificativaStatus() {
        return justificativaStatus;
    }

    public void aprovar() {
        this.status = StatusAdocao.APROVADO;
    }

    public void reprovar(String justificativa) {
        this.status = StatusAdocao.REPROVADO;
        this.justificativaStatus = justificativa;
    }
}
