package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.UserRequestDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private com.empresa.api_level_up_movil.repository.UserRepository clienteRepo;

    public UserResponseDTO saveUser(UserRequestDTO req) {

        try {

            User cliente = new User();
            cliente.setNombre(req.getNombre());
            cliente.setApellido(req.getApellido());
            clienteRepo.save(cliente);

            UserResponseDTO res = new UserResponseDTO();
            res.setId_user(cliente.getId_user());
            res.setNombre(cliente.getNombre());
            res.setApellido(cliente.getApellido());

            return res;

        } catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

}
