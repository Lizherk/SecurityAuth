package com.auth.controller;

import com.auth.dto.UsuarioDetalleDTO;
import com.auth.dto.UsuarioUpdateDTO;
import com.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios") 
@RequiredArgsConstructor
public class UsuarioController {

    private final AuthService authService;

    // La ruta final será: /api/admin/usuarios/todos
    @GetMapping("/todos")
    public ResponseEntity<List<UsuarioDetalleDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDetalleDTO> usuarios = authService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // La ruta final será: /api/admin/usuarios/{id}/toggle-acceso
    @PutMapping("/{id}/toggle-acceso")
    public ResponseEntity<String> alternarEstatusUsuario(@PathVariable Long id) {
        authService.alternarEstatusUsuario(id);
        return ResponseEntity.ok("Estatus del usuario actualizado correctamente.");
    }

    // La ruta final será: /api/admin/usuarios/{id}/rol
    @PutMapping("/{id}/rol")
    public ResponseEntity<String> cambiarRolUsuario(
            @PathVariable Long id, 
            @RequestParam String nuevoRol) {
        authService.cambiarRolUsuario(id, nuevoRol);
        return ResponseEntity.ok("Rol actualizado exitosamente a: " + nuevoRol);
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
        return ResponseEntity.ok("Usuario eliminado permanentemente del sistema.");
    }
}