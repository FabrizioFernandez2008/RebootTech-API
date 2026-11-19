/*
 * Se agregaron datos iniciales para facilitar las pruebas de las funcionalidades del sistema
 * y verificar la conexión y persistencia de la información en la base de datos.
 */

package com.reboottech.RebootTech_API.configuracion;

import java.util.List;
import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.reboottech.RebootTech_API.modelo.Cliente;
import com.reboottech.RebootTech_API.modelo.Credencial;
import com.reboottech.RebootTech_API.modelo.Reparacion;
import com.reboottech.RebootTech_API.modelo.Trabajador;
import com.reboottech.RebootTech_API.repositorio.ClienteRepositorio;
import com.reboottech.RebootTech_API.repositorio.CredencialRepositorio;
import com.reboottech.RebootTech_API.repositorio.ReparacionRepositorio;
import com.reboottech.RebootTech_API.repositorio.TrabajadorRepositorio;

@Configuration
public class DatosInicialesConfiguracion {

    @Bean
    CommandLineRunner cargarDatosIniciales(
            ClienteRepositorio clienteRepositorio,
            CredencialRepositorio credencialRepositorio,
            ReparacionRepositorio reparacionRepositorio,
            TrabajadorRepositorio trabajadorRepositorio,
            PasswordEncoder passwordEncoder) {

        return args -> {
            
        registrarTrabajador(
                trabajadorRepositorio, reparacionRepositorio,
                credencialRepositorio, passwordEncoder,
                "70123456", "Roberto", "Sánchez",
                "999000001", "Dirección de prueba 1",
                "admin", "ADMINISTRADOR", "Admin123"
        );

        registrarTrabajador(
                trabajadorRepositorio, reparacionRepositorio,
                credencialRepositorio, passwordEncoder,
                "70234567", "Lucía", "Fernández",
                "999000002", "Dirección de prueba 2",
                "recepcion", "RECEPCIONISTA", "Recep123"
        );

        registrarTrabajador(
                trabajadorRepositorio, reparacionRepositorio,
                credencialRepositorio, passwordEncoder,
                "70345678", "Miguel", "Ramírez",
                "999000003", "Dirección de prueba 3",
                "tecnico", "TECNICO", "Tec123456"
        );


            registrarCliente(clienteRepositorio,
                    "71234567", "Juan Pérez",
                    "987654321", "juan@gmail.com");

            registrarCliente(clienteRepositorio,
                    "72345678", "María López",
                    "987654322", "maria@gmail.com");

            registrarCliente(clienteRepositorio,
                    "73456789", "Andrea Ramírez Castillo",
                    "987654324", "andrea.ramirez@gmail.com");

            registrarCliente(clienteRepositorio,
                    "75678912", "Fernando Vargas Ruiz",
                    "987654325", "fernando.vargas@gmail.com");

            registrarCliente(clienteRepositorio,
                    "76789123", "Valeria Mendoza Flores",
                    "987654326", "valeria.mendoza@gmail.com");

            registrarCliente(clienteRepositorio,
                    "77891234", "Diego Herrera Salazar",
                    "987654327", "diego.herrera@gmail.com");

            registrarCliente(clienteRepositorio,
                    "74567891", "Carlos Alberto Torres",
                    "987654328", "carlos.torres@example.com");

            registrarReparaciones(reparacionRepositorio);
        };
    }
    
    private void registrarTrabajador(
                TrabajadorRepositorio trabajadorRepositorio,
                ReparacionRepositorio reparacionRepositorio,
                CredencialRepositorio credencialRepositorio,
                PasswordEncoder passwordEncoder,
                String dni,
                String nombres,
                String apellidos,
                String telefono,
                String direccion,
                String usuario,
                String rol,
                String contrasena) {

        Trabajador trabajadorExistente = trabajadorRepositorio
                .findByUsuarioIgnoreCase(usuario)
                .orElseGet(() ->
                        trabajadorRepositorio.findById(dni).orElse(null));

        if (trabajadorExistente != null) {

                String dniAnterior = trabajadorExistente.getDni();

                if (!Objects.equals(dniAnterior, dni)) {
                List<Reparacion> reparaciones =
                        reparacionRepositorio.findAll();

                for (Reparacion reparacion : reparaciones) {
                        if (Objects.equals(
                                reparacion.getDniTecnico(), dniAnterior)) {
                        reparacion.setDniTecnico(dni);
                        }
                }

                reparacionRepositorio.saveAll(reparaciones);

                trabajadorRepositorio.delete(trabajadorExistente);
                }
        }

        trabajadorRepositorio.save(
                new Trabajador(
                        dni,
                        nombres,
                        apellidos,
                        telefono,
                        direccion,
                        usuario,
                        rol,
                        true
                )
        );

        Credencial credencial = credencialRepositorio
                .findByUsuarioIgnoreCase(usuario)
                .orElse(null);

        if (credencial == null) {
                credencial = new Credencial(
                        usuario,
                        passwordEncoder.encode(contrasena),
                        rol,
                        true
                );
        } else {
                credencial.setRol(rol);
                credencial.setEstado(true);
        }

        credencialRepositorio.save(credencial);
        }


    private void registrarCliente(
            ClienteRepositorio clienteRepositorio,
            String dni,
            String nombre,
            String telefono,
            String correo) {

        if (!clienteRepositorio.existsById(dni)) {
            clienteRepositorio.save(
                    new Cliente(dni, nombre, telefono, correo)
            );
        }
    }

    private void registrarReparaciones(
            ReparacionRepositorio reparacionRepositorio) {

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-002", "72345678", "PC de escritorio",
                "Lenovo", "ThinkCentre M70", null, null,
                "El equipo presenta lentitud.",
                "El cliente solicita revisar el almacenamiento.",
                "2026-11-02", "En espera", null,
                null, null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-003", "71234567", "Laptop",
                "Lenovo", "IdeaPad 3", "LN789456", "Cargador",
                "El equipo presenta problemas de rendimiento.",
                "Se requiere reemplazar el disco de almacenamiento. "
                + "Se recomienda disco SSD Kingston 960 GB. Costo S/539.00.",
                "2026-11-04", "Esperando confirmación", "70345678",
                "Se identificó una falla en la unidad de almacenamiento.",
                null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-004", "73456789", "Laptop",
                "HP", "Pavilion 15", "HP2026A001", "Cargador",
                "El equipo se apaga repentinamente después de unos minutos de uso.",
                "El cliente indica que sucede principalmente al utilizar programas exigentes.",
                "2026-11-05", "En espera", null,
                null, null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-005", "75678912", "PC de escritorio",
                "ASUS", "ROG Strix", "AS2026B002",
                "Teclado y cable de alimentación",
                "El equipo no muestra imagen al encender.",
                "El cliente probó conectar otro monitor sin resultados.",
                "2026-11-05", "En espera", null,
                null, null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-006", "76789123", "Laptop",
                "Dell", "Inspiron 15", "DE2026C003",
                "Cargador y funda",
                "El teclado presenta varias teclas que no responden.",
                "El problema comenzó después de una actualización del sistema.",
                "2026-11-06", "En espera", null,
                null, null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-007", "77891234", "Laptop",
                "Acer", "Aspire 5", "AC2026D004", "Cargador",
                "El equipo presenta lentitud y problemas al iniciar.",
                "Se requiere reemplazar la batería. "
                + "Se recomienda batería original Acer Aspire 5. Costo S/289.00.",
                "2026-11-06", "Esperando confirmación", "70345678",
                "Se identificó un deterioro en la batería del equipo.",
                null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-008", "71234567", "Laptop",
                "Lenovo", "ThinkPad E14", "LE2026E005",
                "Cargador y mouse",
                "La pantalla presenta parpadeos durante el uso.",
                "Se completó la reparación y el equipo está pendiente de recojo.",
                "2026-11-03", "Listo para recojo", "70345678",
                "Se identificó una falla en la conexión interna de la pantalla.",
                "Se revisó y corrigió la conexión interna de la pantalla.",
                "Se realizaron pruebas de encendido y funcionamiento de la pantalla.",
                "2026-11-07"
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-009", "72345678", "PC de escritorio",
                "HP", "ProDesk 400", "HP2026F006",
                "Cable de alimentación",
                "El equipo presentaba problemas de ventilación.",
                "Se realizó limpieza interna y mantenimiento preventivo.",
                "2026-11-01", "Terminado", "70345678",
                "Se encontró acumulación de polvo que afectaba la ventilación.",
                "Se realizó limpieza interna y mantenimiento preventivo.",
                "Se verificó el funcionamiento de los ventiladores y la temperatura del equipo.",
                "2026-11-04"
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-010", "74567891", "Laptop",
                "ASUS", "VivoBook 14", "AS2026G007", "Cargador",
                "El equipo presenta problemas de encendido.",
                "El cliente decidió cancelar el servicio antes de iniciar la revisión.",
                "2026-11-07", "Cancelado", null,
                null, null, null, null
        ));

        registrarReparacion(reparacionRepositorio, new Reparacion(
                "RT-011", "73456789", "Monitor",
                "Samsung", "Essential 24", "SA2026H008",
                "Cable HDMI y cable de alimentación",
                "El monitor muestra líneas horizontales ocasionalmente.",
                "El cliente indica que el problema aparece después de un tiempo de uso.",
                "2026-11-08", "En espera", null,
                null, null, null, null
        ));
    }

    private void registrarReparacion(
            ReparacionRepositorio reparacionRepositorio,
            Reparacion reparacion) {

        if (!reparacionRepositorio.existsById(
                reparacion.getCodigoAtencion())) {
            reparacionRepositorio.save(reparacion);
        }
    }
}
