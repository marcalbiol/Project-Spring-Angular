package com.example.demo.models.services;

import com.example.demo.models.entity.Usuario;

public interface IUsuarioService {
    public Usuario findByUsername(String username);

}
