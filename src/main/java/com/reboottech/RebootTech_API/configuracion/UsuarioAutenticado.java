/*
 * Esta clase representa la información del trabajador que ha iniciado sesión.
 * Se utiliza para transportar los datos del usuario autenticado durante el
 * procesamiento de las solicitudes. No se encuentra dentro de los modelos
 * porque no representa una entidad que se almacene en la base de datos,
 * sino información temporal utilizada para gestionar la autenticación.
 */
package com.reboottech.RebootTech_API.configuracion;

public record UsuarioAutenticado(
        String dni,
        String usuario,
        String rol) {
}
