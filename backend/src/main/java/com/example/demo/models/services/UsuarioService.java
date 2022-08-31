package com.example.demo.models.services;

import com.example.demo.models.dao.IUsuarioDao;
import com.example.demo.models.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true) // consulta
public class UsuarioService implements IUsuarioService, UserDetailsService {

    // muestra los errores en consola
    private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // obtener usuario a traves del username
        Usuario usuario = usuarioDao.findByUsername(username);

        if (usuario == null) {
            logger.error("Error en el login, no existe el usuario" + username + " en el sistema");
            throw new UsernameNotFoundException("Error en el login, no existe el usuario" + username + " en el sistema");
        }

        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .peek(authority -> logger.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }
}
