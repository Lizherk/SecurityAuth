package com.auth.service.impl;

import com.auth.dto.AuthLoginRequestDTO;
import com.auth.dto.AuthRegistroRequestDTO;
import com.auth.dto.AuthResponseDTO;
import com.auth.dto.UsuarioDetalleDTO;
import com.auth.dto.UsuarioUpdateDTO;
import com.auth.entity.Rol;
import com.auth.entity.Usuario;
import com.auth.mapper.UsuarioMapper;
import com.auth.repository.RolRepository;
import com.auth.repository.UsuarioRepository;
import com.auth.securityauth.JwtService;
import com.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; 
    

    @Override
    public AuthResponseDTO registrar(AuthRegistroRequestDTO request) {
 
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado en el sistema.");
        }
        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Error: El Rol especificado no existe."));
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setRol(rol);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        String tokenJwt = jwtService.generateToken(usuarioGuardado);
        return usuarioMapper.toAuthResponseDTO(usuarioGuardado, tokenJwt);
    }

    @Override
    public AuthResponseDTO login(AuthLoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        if (!usuario.getActivo()) {
            throw new RuntimeException("Cuenta deshabilitada. Contacte al administrador.");
        }
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta.");
        }
        String tokenJwt = jwtService.generateToken(usuario);
        return usuarioMapper.toAuthResponseDTO(usuario, tokenJwt);
    }

    @Override
    public UsuarioDetalleDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado con el ID: " + id));
        return usuarioMapper.toDetalleDTO(usuario);
    }


    @Override
    public List<UsuarioDetalleDTO> obtenerTodosLosUsuarios() {

        List<Usuario> usuarios = usuarioRepository.findAll();
        

        return usuarios.stream()
                .map(usuarioMapper::toDetalleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void alternarEstatusUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado con el ID: " + id));
        
        
        usuario.setActivo(!usuario.getActivo());
        
     
        usuarioRepository.save(usuario);
    }
    @Override
    public void cambiarRolUsuario(Long id, String nuevoRol) {
        // 1. Buscamos al usuario en la base de datos
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + id));

        
        Rol rolEntidad = rolRepository.findByNombre(nuevoRol)
                .orElseThrow(() -> new RuntimeException("El rol " + nuevoRol + " no existe en la base de datos."));

        // 3. Le asignamos el OBJETO Rol completo, no el String
        usuario.setRol(rolEntidad);

        // 4. Guardamos los cambios
        usuarioRepository.save(usuario);
    }
    @Override
    public UsuarioDetalleDTO actualizarUsuario(Long id, UsuarioUpdateDTO request) {
        // 1. Buscamos al usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        
        if (request.getNombre() != null && !request.getNombre().isEmpty()) {
            usuario.setNombre(request.getNombre());
        }
        
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }

        if (request.getRol() != null && !request.getRol().isEmpty()) {
            Rol nuevoRol = rolRepository.findByNombre(request.getRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuario.setRol(nuevoRol);
        }

      
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        
        return usuarioMapper.toDetalleDTO(usuarioActualizado); 
    }
    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Usuario no encontrado con ID: " + id);
        }
        // Eliminación física de la base de datos
        usuarioRepository.deleteById(id);
    }
    @Override
    public List<UsuarioDetalleDTO> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol_NombreIgnoreCaseAndActivoTrue(rol)
                .stream()
                .map(usuarioMapper::toDetalleDTO)
                .collect(Collectors.toList());
    }
}