package com.deluca.fantamarvel.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lega.
 */
@Entity
@Table(name = "lega")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "codice", nullable = false)
    private String codice;

    @Lob
    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lega id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return this.codice;
    }

    public Lega codice(String codice) {
        this.setCodice(codice);
        return this;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public Lega descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    public Lega isPrivate(Boolean isPrivate) {
        this.setIsPrivate(isPrivate);
        return this;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getNome() {
        return this.nome;
    }

    public Lega nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lega)) {
            return false;
        }
        return id != null && id.equals(((Lega) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lega{" +
            "id=" + getId() +
            ", codice='" + getCodice() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", isPrivate='" + getIsPrivate() + "'" +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
