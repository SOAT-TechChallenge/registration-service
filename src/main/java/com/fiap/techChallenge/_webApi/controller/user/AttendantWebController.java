package com.fiap.techChallenge._webApi.controller.user;

import com.fiap.techChallenge._webApi.dto.AttendantRequestDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantInputDTO;
import com.fiap.techChallenge.core.controller.user.AttendantController;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user/attendant")
@Tag(name = "Attendant", description = "APIs relacionadas aos Atendentes")
public class AttendantWebController {

    private final AttendantController compositeDataSource;

    public AttendantWebController(CompositeDataSource compositeDataSource) {
        this.compositeDataSource = AttendantController.build(compositeDataSource);
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<AttendantDTO> create(@RequestBody @Valid AttendantRequestDTO attendantRequest) {
        var result = compositeDataSource.create(
                new AttendantInputDTO(
                        attendantRequest.name(),
                        attendantRequest.email(),
                        attendantRequest.cpf()
                ));

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<AttendantDTO> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AttendantRequestDTO attendantRequest
    ) {
        AttendantDTO attendant = compositeDataSource.update(
                id,
                new AttendantInputDTO(
                        attendantRequest.name(),
                        attendantRequest.email(),
                        attendantRequest.cpf()
                ));

        return ResponseEntity.status(HttpStatus.OK).body(attendant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendantDTO> findById(@PathVariable("id") UUID id) {
        AttendantDTO attendant = compositeDataSource.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(attendant);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<AttendantDTO> findByCpf(@PathVariable("cpf") String cpf) {
        AttendantDTO attendant = compositeDataSource.findByCpf(cpf);

        return ResponseEntity.status(HttpStatus.OK).body(attendant);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AttendantDTO>> list() {
        List<AttendantDTO> attendants = compositeDataSource.list();

        return ResponseEntity.status(HttpStatus.OK).body(attendants);
    }
}
