package com.deluca.fantamarvel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserExtended.
 */
@Entity
@Table(name = "user_extended")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserExtended implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "note_1")
    private String note1;

    @Column(name = "note_2")
    private String note2;

    @Column(name = "note_3")
    private String note3;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "userExtendeds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userExtendeds" }, allowSetters = true)
    private Set<Film> films = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserExtended id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public UserExtended username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public UserExtended provincia(String provincia) {
        this.setProvincia(provincia);
        return this;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNote1() {
        return this.note1;
    }

    public UserExtended note1(String note1) {
        this.setNote1(note1);
        return this;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNote2() {
        return this.note2;
    }

    public UserExtended note2(String note2) {
        this.setNote2(note2);
        return this;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public String getNote3() {
        return this.note3;
    }

    public UserExtended note3(String note3) {
        this.setNote3(note3);
        return this;
    }

    public void setNote3(String note3) {
        this.note3 = note3;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserExtended user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Film> getFilms() {
        return this.films;
    }

    public void setFilms(Set<Film> films) {
        if (this.films != null) {
            this.films.forEach(i -> i.removeUserExtended(this));
        }
        if (films != null) {
            films.forEach(i -> i.addUserExtended(this));
        }
        this.films = films;
    }

    public UserExtended films(Set<Film> films) {
        this.setFilms(films);
        return this;
    }

    public UserExtended addFilm(Film film) {
        this.films.add(film);
        film.getUserExtendeds().add(this);
        return this;
    }

    public UserExtended removeFilm(Film film) {
        this.films.remove(film);
        film.getUserExtendeds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserExtended)) {
            return false;
        }
        return id != null && id.equals(((UserExtended) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtended{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", provincia='" + getProvincia() + "'" +
            ", note1='" + getNote1() + "'" +
            ", note2='" + getNote2() + "'" +
            ", note3='" + getNote3() + "'" +
            "}";
    }
}
