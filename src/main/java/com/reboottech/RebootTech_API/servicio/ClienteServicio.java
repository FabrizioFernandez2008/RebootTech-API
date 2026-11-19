package com.reboottech.RebootTech_API.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Cliente;
import com.reboottech.RebootTech_API.repositorio.ClienteRepositorio;
import com.reboottech.RebootTech_API.repositorio.ReparacionRepositorio;

@Service
@Transactional
public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;
    private final ReparacionRepositorio reparacionRepositorio;

    public ClienteServicio(
            ClienteRepositorio clienteRepositorio,
            ReparacionRepositorio reparacionRepositorio) {

        this.clienteRepositorio = clienteRepositorio;
        this.reparacionRepositorio = reparacionRepositorio;
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepositorio.findAll();
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepositorio.findById(dni);
    }

    public Cliente registrarCliente(Cliente cliente) {
        validarCliente(cliente);

        if (clienteRepositorio.existsById(cliente.getDni())) {
            throw new ReglaNegocioException(
                    "El cliente ya se encuentra registrado.");
        }

        return clienteRepositorio.save(cliente);
    }

    public Cliente actualizarCliente(String dni, Cliente clienteActualizado) {
        Cliente cliente = clienteRepositorio.findById(dni)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cliente no encontrado."));

        if (clienteActualizado.getDni() != null
                && !dni.equals(clienteActualizado.getDni())) {
            throw new ReglaNegocioException(
                    "No se permite modificar el DNI del cliente.");
        }

        validarCliente(clienteActualizado);

        cliente.setNombreCompleto(
                clienteActualizado.getNombreCompleto().trim());
        cliente.setTelefono(clienteActualizado.getTelefono().trim());
        cliente.setCorreo(clienteActualizado.getCorreo().trim());

        return clienteRepositorio.save(cliente);
    }

    public void eliminarCliente(String dni) {
        if (!clienteRepositorio.existsById(dni)) {
            throw new RecursoNoEncontradoException(
                    "Cliente no encontrado.");
        }

        if (reparacionRepositorio.existsByDniCliente(dni)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar un cliente que tiene reparaciones registradas.");
        }

        clienteRepositorio.deleteById(dni);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null
                || cliente.getDni() == null
                || cliente.getDni().isBlank()
                || cliente.getNombreCompleto() == null
                || cliente.getNombreCompleto().isBlank()
                || cliente.getTelefono() == null
                || cliente.getTelefono().isBlank()
                || cliente.getCorreo() == null
                || cliente.getCorreo().isBlank()) {

            throw new ReglaNegocioException(
                    "Debe completar todos los datos del cliente.");
        }
    }
}
