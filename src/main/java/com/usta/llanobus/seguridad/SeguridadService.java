package com.usta.llanobus.seguridad;

import com.usta.llanobus.clientes.Cliente;
import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.db.BaseDeDatos;

public class SeguridadService {
    private final BaseDeDatos db;

    public SeguridadService(BaseDeDatos db) { this.db = db; }

    public Cliente iniciarSesion(String email, String password) {
        return db.clientes.values().stream()
                .filter(c -> c.email().equalsIgnoreCase(email) && c.password().equals(password))
                .findFirst()
                .orElseThrow(() -> new LlanoBusException("Credenciales inválidas"));
    }
}