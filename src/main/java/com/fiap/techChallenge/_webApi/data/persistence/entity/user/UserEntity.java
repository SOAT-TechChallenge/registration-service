package com.fiap.techChallenge._webApi.data.persistence.entity.user;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    protected UUID id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @Embedded
    @AttributeOverride(
            name = "number",
            column = @Column(name = "cpf", unique = true, nullable = false, length = 11)
    )
    protected CPFEmbeddable cpf;

    protected UserEntity() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CPFEmbeddable getCpf() {
        return cpf;
    }

    public void setCpf(String cpfNumber) {
        if (cpfNumber == null) {
            this.cpf = null;
            return;
        }

        this.cpf = new CPFEmbeddable(cpfNumber);
    }
}
