package com.auth.controller;

import com.auth.dto.AuthLoginRequestDTO;
import com.auth.dto.AuthRegistroRequestDTO;
import com.auth.dto.AuthResponseDTO;
import com.auth.dto.UsuarioDetalleDTO;
import com.auth.dto.UsuarioUpdateDTO;
import com.auth.service.AuthService;
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST: http://localhost:8080/api/auth/registro
    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> registrar(@Valid @RequestBody AuthRegistroRequestDTO request) {
        AuthResponseDTO response = authService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); 
    }
    // POST: http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response); 
    }
    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDetalleDTO> obtenerInfoUsuario(@PathVariable Long id) {
  
        UsuarioDetalleDTO response = authService.obtenerUsuarioPorId(id);
        
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<AuthResponseDTO> crearUsuarioAdmin(@Valid @RequestBody AuthRegistroRequestDTO request) {
      
        AuthResponseDTO response = authService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDetalleDTO> actualizarUsuario(
            @PathVariable Long id, 
            @RequestBody UsuarioUpdateDTO request) {
        
        UsuarioDetalleDTO actualizado = authService.actualizarUsuario(id, request);
        return ResponseEntity.ok(actualizado);
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        authService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado de la base de datos.");
    }
    @GetMapping("/admins")
    public ResponseEntity<List<UsuarioDetalleDTO>> obtenerAdministradores() {
        List<UsuarioDetalleDTO> admins = authService.obtenerUsuariosPorRol("ADMIN");
        return ResponseEntity.ok(admins);
    }
}