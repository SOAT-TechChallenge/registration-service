package com.fiap.techChallenge.core.presenter;

import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerAnonymDTO;
import com.fiap.techChallenge.core.domain.entities.user.attendant.Attendant;
import com.fiap.techChallenge.core.domain.entities.user.customer.Customer;

public class UserPresenter {

    public static CustomerFullDTO toCustomerDTO(Customer customer) {
        return new CustomerFullDTO(
                customer.getId(),
                customer.getName(),
                customer.getFormattedCpf(),
                customer.getEmail(),
                customer.isAnonymous()
        );
    }

    public static CustomerAnonymDTO toAnonymCustomerDTO(Customer customer) {
        return new CustomerAnonymDTO(
                customer.getId(),
                customer.getName(),
                customer.isAnonymous()
        );
    }

    public static AttendantDTO toAttendantDTO(Attendant attendant) {
        return new AttendantDTO(
                attendant.getId(),
                attendant.getName(),
                attendant.getEmail(),
                attendant.getFormattedCpf()
        );
    }
}
