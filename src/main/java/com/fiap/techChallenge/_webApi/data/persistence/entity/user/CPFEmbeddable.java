package com.fiap.techChallenge._webApi.data.persistence.entity.user;

import jakarta.persistence.Embeddable;

@Embeddable
public class CPFEmbeddable {

    private String number;

    public CPFEmbeddable() {}

    public CPFEmbeddable(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    void setNumber(String number) {
        this.number = number;
    }
}
