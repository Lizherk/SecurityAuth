package com.auth.mapper;

import com.auth.dto.AuthRegistroRequestDTO;
import com.auth.dto.AuthResponseDTO;
import com.auth.dto.UsuarioDetalleDTO; 
import com.auth.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // 1. De DTO de Registro a Entidad (Para cuando alguien se registra)
    public Usuario toEntity(AuthRegistroRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());

        return usuario;
    }

    // 2. De Entidad a Respuesta de Login/Registro (La que devuelve el JWT)
    public AuthResponseDTO toAuthResponseDTO(Usuario usuario, String token) {
        if (usuario == null) {
            return null;
        }

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setId(usuario.getId());        
        response.setActivo(usuario.getActivo());
        response.setEmail(usuario.getEmail());
  
        if (usuario.getRol() != null) {
            response.setRol(usuario.getRol().getNombre());
        }

        return response;
    }

    public UsuarioDetalleDTO toDetalleDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        String nombreRol = (usuario.getRol() != null) ? usuario.getRol().getNombre() : "SIN_ROL";

        return UsuarioDetalleDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(nombreRol)
                .departamentoId(usuario.getDepartamentoId())
                .activo(usuario.getActivo())
                .build();
    }
    
}