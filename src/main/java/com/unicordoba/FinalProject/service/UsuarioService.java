package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.entity.Usuario;
import com.unicordoba.FinalProject.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() { return usuarioRepository.findAll(); }

    public Optional<Usuario> obtenerPorId(Integer id) { return usuarioRepository.findById(id); }

    public Usuario guardar(Usuario usuario) {
        // Aquí podrías agregar lógica para encriptar contraseña si quisieras
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) { usuarioRepository.deleteById(id); }
}