package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.LoginRequestDTO;
import com.empresa.api_level_up_movil.dto.request.UserRequestDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.model.User;
import com.empresa.api_level_up_movil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    public UserResponseDTO saveUser(UserRequestDTO req) {



        User user = new User();
        user.setNombre(req.getNombre());
        user.setApellido(req.getApellido());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRol(req.getRol());
        user.setFecha_registro(req.getFecha_registro());

        userRepo.save(user);

        UserResponseDTO res= new UserResponseDTO();
        res.setId_user(user.getId_user());
        res.setNombre(user.getNombre());
        res.setApellido(user.getApellido());
        res.setEmail(user.getEmail());
        res.setRol(user.getRol());
        res.setFecha_registro(user.getFecha_registro());

        return res;



    }

    public UserResponseDTO getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(user -> {
                    UserResponseDTO res = new UserResponseDTO();
                    res.setId_user(user.getId_user());
                    res.setNombre(user.getNombre());
                    res.setApellido(user.getApellido());
                    res.setEmail(user.getEmail());
                    res.setRol(user.getRol());
                    res.setFecha_registro(user.getFecha_registro());
                    return res;
                })
                .orElse(null);
    }

    public String getToken(LoginRequestDTO req) {
        List<User> users = userRepo.findAll();

        for (User user : users) {
            if (user.getEmail().equals(req.getEmail()) && user.getPassword().equals(req.getPassword())) {
                return jwtService.generateToken(req.getEmail());
            }
        }
        return null;
    }



}
