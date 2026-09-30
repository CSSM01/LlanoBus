package com.usta.llanobus.comunicaciones;

import com.usta.llanobus.clientes.Cliente;
import com.usta.llanobus.comercializacion.Tiquete;

public class ComunicacionesService {

    public void enviarConfirmacion(Cliente cliente, Tiquete tiquete) {
        System.out.println("[COMUNICACIONES] Correo enviado a " + cliente.email()
                + ": su tiquete #" + tiquete.getId() + " (asiento " + tiquete.getAsiento()
                + ") está " + tiquete.getEstado());
    }
}