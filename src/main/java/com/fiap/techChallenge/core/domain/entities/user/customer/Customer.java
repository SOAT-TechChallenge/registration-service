package com.fiap.techChallenge.core.domain.entities.user.customer;

import java.util.UUID;

import com.fiap.techChallenge.core.domain.entities.user.CPF;
import com.fiap.techChallenge.core.domain.entities.user.User;

public class Customer extends User {

    private final boolean anonymous;

    private Customer(UUID id, String name, String email, CPF cpf, boolean anonymous) {
        super(id, name, email, cpf);
        this.anonymous = anonymous;
    }

    public static Customer build(UUID id, String name, String email, String cpfNumber, boolean anonymous) {

        if (!anonymous) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Nome obrigatório");
            }
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("Email obrigatório");
            }
            if (cpfNumber == null || cpfNumber.isBlank()) {
                throw new IllegalArgumentException("CPF obrigatório");
            }

            CPF cpf = new CPF(cpfNumber);

            return new Customer(id, name, email, cpf, false);
        } else {
            return new Customer(id, name, null, null, true);
        }
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    @Override
    public UUID getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getFormattedCpf() {
        if (cpf == null) return null;

        return super.getFormattedCpf();
    }
    
    @Override
    public String getUnformattedCpf() {
        if (cpf == null) return null;

        return super.getUnformattedCpf();
    }

    @Override
    public void setCpf(String cpfNumber) {
        super.setCpf(cpfNumber);
    }
}
