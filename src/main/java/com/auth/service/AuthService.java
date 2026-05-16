package com.auth.service;

import java.util.List;

import com.auth.dto.AuthLoginRequestDTO;
import com.auth.dto.AuthRegistroRequestDTO;
import com.auth.dto.AuthResponseDTO;
import com.auth.dto.UsuarioDetalleDTO;
import com.auth.dto.UsuarioUpdateDTO; 

public interface AuthService {

	AuthResponseDTO registrar(AuthRegistroRequestDTO request);

    AuthResponseDTO login(AuthLoginRequestDTO request);

    UsuarioDetalleDTO obtenerUsuarioPorId(Long id);

    List<UsuarioDetalleDTO> obtenerTodosLosUsuarios();

    List<UsuarioDetalleDTO> obtenerUsuariosPorRol(String rol);

    void alternarEstatusUsuario(Long id);

    void cambiarRolUsuario(Long id, String nuevoRol);

    UsuarioDetalleDTO actualizarUsuario(Long id, UsuarioUpdateDTO request);

    void eliminarUsuario(Long id);
}
    
