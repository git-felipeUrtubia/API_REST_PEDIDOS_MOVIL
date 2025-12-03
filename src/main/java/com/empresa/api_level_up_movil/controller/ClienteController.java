package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.ClienteRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ClienteResponseDTO;
import com.empresa.api_level_up_movil.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> guardarCliente(@RequestBody ClienteRequestDTO req) {

        try {

            ClienteResponseDTO resp = clienteService.saveCliente(req);
            if (resp != null) {
                return ResponseEntity.ok(resp);
            }
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

}
