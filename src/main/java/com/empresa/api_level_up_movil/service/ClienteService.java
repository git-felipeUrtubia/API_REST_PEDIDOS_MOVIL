package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.ClienteRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ClienteResponseDTO;
import com.empresa.api_level_up_movil.model.Cliente;
import com.empresa.api_level_up_movil.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepo;

    public ClienteResponseDTO saveCliente(ClienteRequestDTO req) {

        try {

            Cliente cliente = new Cliente();
            cliente.setNombre(req.getNombre());
            cliente.setApellido(req.getApellido());
            clienteRepo.save(cliente);

            ClienteResponseDTO res = new ClienteResponseDTO();
            res.setId_cliente(cliente.getId_cliente());
            res.setNombre(cliente.getNombre());
            res.setApellido(cliente.getApellido());

            return res;

        } catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

}
