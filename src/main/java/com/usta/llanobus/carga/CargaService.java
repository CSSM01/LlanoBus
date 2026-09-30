package com.usta.llanobus.carga;

import com.usta.llanobus.db.BaseDeDatos;
import com.usta.llanobus.viajes.ViajeService;

public class CargaService {
    private final BaseDeDatos db;
    private final ViajeService viajeService;

    public CargaService(BaseDeDatos db, ViajeService viajeService) {
        this.db = db;
        this.viajeService = viajeService;
    }

    public Encomienda registrarEncomienda(int viajeId, String descripcion, double pesoKg) {
        viajeService.buscarViaje(viajeId); // valida que el viaje exista
        Encomienda e = new Encomienda(db.siguienteId("encomienda"), viajeId, descripcion, pesoKg);
        db.encomiendas.put(e.id(), e);
        return e;
    }
}