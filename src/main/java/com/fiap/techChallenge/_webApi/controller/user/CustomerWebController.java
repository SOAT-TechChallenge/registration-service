package com.fiap.techChallenge._webApi.controller.user;

import com.fiap.techChallenge._webApi.dto.CustomerRequestDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerInputDTO;
import com.fiap.techChallenge.core.controller.user.CustomerController;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user/customer")
@Tag(name = "Customer", description = "APIs relacionadas aos Clientes")
public class CustomerWebController {

    private final CustomerController customerController;

    public CustomerWebController(CompositeDataSource compositeDataSource) {
        this.customerController = CustomerController.build(compositeDataSource);
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<CustomerDTO> create(@RequestBody @Valid CustomerRequestDTO customerRequest) {
        var result = customerController.create(
                new CustomerInputDTO(
                        customerRequest.name(),
                        customerRequest.email(),
                        customerRequest.cpf(),
                        customerRequest.anonymous()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<CustomerDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerRequestDTO customerRequest
    ) {
        var result = customerController.update(
                id,
                new CustomerInputDTO(
                        customerRequest.name(),
                        customerRequest.email(),
                        customerRequest.cpf(),
                        customerRequest.anonymous()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable("id") UUID id) {
        var result = customerController.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<CustomerDTO> findByCpf(@PathVariable("cpf") String cpf) {
        var result = customerController.findByCpf(cpf);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // TODO: REVISAR SE VAI CHAMAR SÓ OS QUE NÃO SÃO ANÔNIMOS
    @GetMapping("/list")
    public ResponseEntity<List<CustomerFullDTO>> list() {
        var result = customerController.listNotAnonym();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
