package com.usta.llanobus.viajes;

import java.util.HashSet;
import java.util.Set;

public class Viaje {
    private final int id;
    private final String rutaCodigo;
    private final String fechaHora;
    private final int busId;
    private final int conductorId;
    private final int totalAsientos;
    private final double precio;
    private final Set<Integer> asientosOcupados = new HashSet<>();

    public Viaje(int id, String rutaCodigo, String fechaHora, int busId,
                 int conductorId, int totalAsientos, double precio) {
        this.id = id;
        this.rutaCodigo = rutaCodigo;
        this.fechaHora = fechaHora;
        this.busId = busId;
        this.conductorId = conductorId;
        this.totalAsientos = totalAsientos;
        this.precio = precio;
    }

    public boolean asientoDisponible(int asiento) {
        return asiento >= 1 && asiento <= totalAsientos && !asientosOcupados.contains(asiento);
    }

    public void ocupar(int asiento)  { asientosOcupados.add(asiento); }
    public void liberar(int asiento) { asientosOcupados.remove(asiento); }
    public int asientosDisponibles() { return totalAsientos - asientosOcupados.size(); }

    public int getId()             { return id; }
    public String getRutaCodigo()  { return rutaCodigo; }
    public String getFechaHora()   { return fechaHora; }
    public int getBusId()          { return busId; }
    public int getConductorId()    { return conductorId; }
    public double getPrecio()      { return precio; }

    @Override
    public String toString() {
        return "Viaje{id=" + id + ", ruta=" + rutaCodigo + ", salida=" + fechaHora
                + ", disponibles=" + asientosDisponibles() + ", precio=" + precio + "}";
    }

    public Set<Integer> getAsientosOcupados() {
        return new java.util.TreeSet<>(asientosOcupados);
    }

}