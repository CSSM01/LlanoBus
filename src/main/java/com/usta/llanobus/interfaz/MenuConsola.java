package com.usta.llanobus.interfaz;

import com.usta.llanobus.buses.Bus;
import com.usta.llanobus.buses.BusService;
import com.usta.llanobus.carga.CargaService;
import com.usta.llanobus.clientes.Cliente;
import com.usta.llanobus.clientes.ClienteService;
import com.usta.llanobus.comercializacion.Tiquete;
import com.usta.llanobus.comercializacion.TiqueteService;
import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.conductores.Conductor;
import com.usta.llanobus.conductores.ConductorService;
import com.usta.llanobus.inteligencia.InteligenciaService;
import com.usta.llanobus.seguridad.SeguridadService;
import com.usta.llanobus.viajes.Ruta;
import com.usta.llanobus.viajes.Viaje;
import com.usta.llanobus.viajes.ViajeService;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola: solo pide datos y llama a los módulos. No tiene lógica de negocio.
 */
public class MenuConsola {
    private final Scanner sc = new Scanner(System.in);

    private final ClienteService clientes;
    private final ConductorService conductores;
    private final BusService buses;
    private final ViajeService viajes;
    private final TiqueteService tiquetes;
    private final CargaService carga;
    private final InteligenciaService inteligencia;
    private final SeguridadService seguridad;

    private Cliente sesion; // cliente con sesión activa (null si no hay)

    public MenuConsola(ClienteService clientes, ConductorService conductores, BusService buses, ViajeService viajes, TiqueteService tiquetes, CargaService carga, InteligenciaService inteligencia, SeguridadService seguridad) {
        this.clientes = clientes;
        this.conductores = conductores;
        this.buses = buses;
        this.viajes = viajes;
        this.tiquetes = tiquetes;
        this.carga = carga;
        this.inteligencia = inteligencia;
        this.seguridad = seguridad;
    }

    // ================= MENÚ PRINCIPAL =================
    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n========== LLANOBUS ==========");
            System.out.println("1. Registrarme como cliente");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Panel administrativo");
            System.out.println("0. Salir");
            int op = leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1 -> registrarCliente();
                    case 2 -> iniciarSesion();
                    case 3 -> menuAdministrativo();
                    case 0 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (LlanoBusException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("¡Hasta pronto!");
    }

    // ================= CLIENTE =================
    private void registrarCliente() {
        System.out.println("\n--- Registro de cliente ---");
        String nombre = leerTexto("Nombre: ");
        String email = leerTexto("Email: ");
        String password = leerTexto("Contraseña: ");
        Cliente c = clientes.registrar(nombre, email, password);
        System.out.println("Cliente registrado con ID " + c.id());
    }

    private void iniciarSesion() {
        System.out.println("\n--- Iniciar sesión ---");
        String email = leerTexto("Email: ");
        String password = leerTexto("Contraseña: ");
        sesion = seguridad.iniciarSesion(email, password);   // módulo 10
        System.out.println("Bienvenido, " + sesion.nombre());
        menuCliente();
    }

    private void menuCliente() {
        while (sesion != null) {
            System.out.println("\n--- Menú del cliente: " + sesion.nombre() + " ---");
            System.out.println("1. Consultar rutas");
            System.out.println("2. Consultar horarios de una ruta");
            System.out.println("3. Comprar tiquete");
            System.out.println("4. Registrar encomienda");
            System.out.println("0. Cerrar sesión");
            int op = leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1 -> consultarRutas();
                    case 2 -> consultarHorarios();
                    case 3 -> comprarTiquete();
                    case 4 -> registrarEncomienda();
                    case 0 -> {
                        sesion = null;
                        System.out.println("Sesión cerrada.");
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (LlanoBusException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void consultarRutas() {
        String origen = leerTexto("Ciudad de origen: ");
        String destino = leerTexto("Ciudad de destino: ");
        List<Ruta> rutas = viajes.consultarRutas(origen, destino);
        rutas.forEach(r -> System.out.println("  " + r));
    }

    private void consultarHorarios() {
        String codigo = leerTexto("Código de la ruta (ej. VIL-BOG): ").toUpperCase();
        viajes.consultarHorarios(codigo).forEach(v -> System.out.println("  " + v));
    }

    private void comprarTiquete() {
        System.out.println("\n--- Comprar tiquete ---");
        // Paso 1: consulta
        consultarRutas();
        String codigo = leerTexto("Código de la ruta elegida: ").toUpperCase();
        List<Viaje> horarios = viajes.consultarHorarios(codigo);
        horarios.forEach(v -> System.out.println("  " + v));

        // Paso 2: selección
        int viajeId = leerEntero("ID del viaje: ");
        Viaje viaje = viajes.buscarViaje(viajeId);
        System.out.println("Asientos ocupados: " + viaje.getAsientosOcupados());
        int asiento = leerEntero("Número de asiento: ");

        // Pasos 3 y 4: pago + confirmación (los hace TiqueteService)
        Tiquete t = tiquetes.comprarTiquete(sesion.id(), viajeId, asiento);
        System.out.println("Compra exitosa: " + t);
    }

    private void registrarEncomienda() {
        int viajeId = leerEntero("ID del viaje: ");
        String descripcion = leerTexto("Descripción: ");
        double peso = leerDecimal("Peso (kg): ");
        System.out.println("Registrada: " + carga.registrarEncomienda(viajeId, descripcion, peso));
    }

    // ================= ADMINISTRATIVO =================
    private void menuAdministrativo() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Panel administrativo ---");
            System.out.println("1. Registrar conductor");
            System.out.println("2. Registrar bus");
            System.out.println("3. Registrar ruta");
            System.out.println("4. Programar viaje");
            System.out.println("5. Ver viajes");
            System.out.println("6. Reporte de ventas");
            System.out.println("0. Volver");
            int op = leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1 -> registrarConductor();
                    case 2 -> registrarBus();
                    case 3 -> registrarRuta();
                    case 4 -> programarViaje();
                    case 5 -> viajes.listarViajes().forEach(v -> System.out.println("  " + v));
                    case 6 -> inteligencia.reporteVentas();
                    case 0 -> volver = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (LlanoBusException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registrarConductor() {
        String nombre = leerTexto("Nombre del conductor: ");
        System.out.println("Registrado: " + conductores.registrar(nombre));
    }

    private void registrarBus() {
        String placa = leerTexto("Placa: ");
        int capacidad = leerEntero("Capacidad (asientos): ");
        System.out.println("Registrado: " + buses.registrar(placa, capacidad));
    }

    private void registrarRuta() {
        String origen = leerTexto("Origen: ");
        String destino = leerTexto("Destino: ");
        System.out.println("Registrada: " + viajes.registrarRuta(origen, destino));
    }

    private void programarViaje() {
        System.out.println("Rutas disponibles:");
        viajes.listarRutas().forEach(r -> System.out.println("  " + r));
        String codigo = leerTexto("Código de ruta: ").toUpperCase();

        System.out.println("Buses disponibles:");
        for (Bus b : buses.listar()) System.out.println("  " + b);
        int busId = leerEntero("ID del bus: ");

        System.out.println("Conductores disponibles:");
        for (Conductor c : conductores.listar()) System.out.println("  " + c);
        int conductorId = leerEntero("ID del conductor: ");

        String fechaHora = leerTexto("Fecha y hora (ej. 2026-10-15 08:00): ");
        double precio = leerDecimal("Precio del tiquete: ");

        Viaje v = viajes.programarViaje(codigo, fechaHora, busId, conductorId, precio);
        System.out.println("Viaje programado: " + v);
    }

    // ================= LECTURA SEGURA =================
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido (usa punto para decimales).");
            }
        }
    }
}