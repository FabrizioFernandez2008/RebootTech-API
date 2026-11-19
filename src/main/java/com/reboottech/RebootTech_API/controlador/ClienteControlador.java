package com.reboottech.RebootTech_API.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reboottech.RebootTech_API.dto.ClienteSolicitudDTO;
import com.reboottech.RebootTech_API.dto.ClienteRespuestaDTO;
import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Cliente;
import com.reboottech.RebootTech_API.servicio.ClienteServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;

    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @GetMapping
    public ResponseEntity<List<ClienteRespuestaDTO>> obtenerClientes() {

        List<ClienteRespuestaDTO> respuesta = clienteServicio.obtenerClientes()
                .stream()
                .map(this::convertirARespuesta)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteRespuestaDTO> buscarPorDni(
            @PathVariable String dni) {

        Cliente cliente = clienteServicio.buscarPorDni(dni)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cliente no encontrado."));

        return ResponseEntity.ok(convertirARespuesta(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteRespuestaDTO> registrarCliente(
            @Valid @RequestBody ClienteSolicitudDTO datos) {

        Cliente cliente = convertirAEntidad(datos);

        Cliente registrado = clienteServicio.registrarCliente(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirARespuesta(registrado));
    }

    @PutMapping("/{dni}")
    public ResponseEntity<ClienteRespuestaDTO> actualizarCliente(
            @PathVariable String dni,
            @Valid @RequestBody ClienteSolicitudDTO datos) {

        Cliente cliente = convertirAEntidad(datos);

        Cliente actualizado = clienteServicio.actualizarCliente(dni, cliente);

        return ResponseEntity.ok(convertirARespuesta(actualizado));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String dni) {

        clienteServicio.eliminarCliente(dni);

        return ResponseEntity.noContent().build();
    }

    private Cliente convertirAEntidad(ClienteSolicitudDTO datos) {

        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar los datos del cliente.");
        }

        return new Cliente(
                datos.getDni(),
                datos.getNombreCompleto(),
                datos.getTelefono(),
                datos.getCorreo()
        );
    }

    private ClienteRespuestaDTO convertirARespuesta(Cliente cliente) {

        return new ClienteRespuestaDTO(
                cliente.getDni(),
                cliente.getNombreCompleto(),
                cliente.getTelefono(),
                cliente.getCorreo()
        );
    }
}
