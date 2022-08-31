package com.example.demo.controllers;

import com.example.demo.models.entity.Client;
import com.example.demo.models.entity.Region;
import com.example.demo.models.services.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


// indica el dominio y damos acceso para que pueda enviar y recibir datos
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api") // generamos la url
public class ClienteRestController {
    //logger
    private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);
    @Autowired
    // inyectamos la interfaz del clienteserrviceimp
    private IClienteService clienteService;

    @GetMapping("/clientes")  //mappeamos la url a este metodo
    public List<Client> index() {
        return clienteService.findAll();
    }

    /*
    @GetMapping("/clientes/page/{page}")  //mappeamos la url a este metodo
    public Page<Client> index(@PathVariable Long page) {
        // por pagina mostrara X clientes, lo que indiquemos abajo
        return clienteService.findAll((java.awt.print.Pageable) PageRequest.of(Math.toIntExact(page), 2));
    }
     */

    // se usa Get para devolver y Post para crear

    // @Secured({"ROLE_ADMIN", "ROLE_USER"})
    /* crear MOSTRAR CON ID */
    // devuelve un id en json
    @GetMapping("clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {

        Client cliente = null;
        Map<String, Object> response = new HashMap<>();

        // controlar si no encuentra en la base de datos
        try {
            cliente = clienteService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "error al consultar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (cliente == null) {
            response.put("mensaje", "El cliente id ".concat(id.toString().concat("No existe")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        // si existe
        return new ResponseEntity<Client>(cliente, HttpStatus.OK);
    }


    /* CREAR */
    // devuelve el nuevo cliente creado en la bd
    // enviar el cliente en formato json dentro del Request y lo mapea al objeto cliente y lo guarda
    // @Secured({"ROLE_ADMIN"})
    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Client cliente, BindingResult result) {
        cliente.setCreateAt(new Date());  // añade la fecha de cuando se crea

        // crea el mensaje de error
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            //recoge si hay ererores en la validacion = binding result
            List<String> errors = new ArrayList<>();
            for (FieldError err : result.getFieldErrors()) {
                errors.add("El campo " + err.getField() + " " + err.getDefaultMessage());
            }
            response.put("Errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Client clientNew = null;
        try {
            clientNew = clienteService.save(cliente);
        } catch (DataAccessException e) {
            response.put("mensaje", "error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //mensaje cuando crea el cliente
        response.put("mensaje", "el cliente ha sido creado con exito");
        // response.put("cliente", "N");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    /* ACTUALIZA */
    // actualizar cliente bd recibiendo el id
    //  @Secured({"ROLE_ADMIN"})
    @PutMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@Valid @RequestBody Client cliente, BindingResult result, @PathVariable Long id) {

        // guardamos el cliente que queremos cambiar cogiendo el ID
        Client clienteActual = clienteService.findById(id);

        Client clienteUpdated = null;

        Map<String, Object> response = new HashMap<>();


        if (result.hasErrors()) {
            //recoge si hay ererores en la validacion = binding result
            List<String> errors = new ArrayList<>();
            for (FieldError err : result.getFieldErrors()) {
                errors.add("El campo " + err.getField() + " " + err.getDefaultMessage());
            }
            response.put("Errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }


        if (clienteActual == null) {
            response.put("mensaje", "El cliente no se pudo editar ".concat(id.toString().concat("No existe")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // cliente que viene de la bd  || cliente que queremos cambiar
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());
            clienteActual.setRegion(cliente.getRegion());

            clienteUpdated = clienteService.save(clienteActual);  // cliente actualizado


        } catch (DataAccessException e) {
            response.put("mensaje", "error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //mensaje cuando crea el cliente
        response.put("mensaje", "el cliente ha sido actualizado con exito");
        // response.put("cliente", "N");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    // elimina el id que entre por parametro
    //  @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            Client client = clienteService.findById(id);
            String nombreFotoAnterior = client.getFoto();

            if (nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
                Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
                File archivoFotoAnterior = rutaFotoAnterior.toFile();

                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
                    archivoFotoAnterior.delete();
                }
            }

            clienteService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "error al eliminar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // mensaje si no pasa por el catch == exito
        response.put("mensaje", "el cliente ha sido borrado con exito");
        // response.put("cliente", "N")
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    // @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) throws IOException {

        Map<String, Object> response = new HashMap<>();

        Client client = clienteService.findById(id);

        if (!archivo.isEmpty()) {

            // obtenemos nombre archivo
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
            log.info(rutaArchivo.toString());
            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);

            } catch (IOException e) {
                response.put("mensaje", "error al subir la imagen");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // borrar anterior foto
            String nombreFotoAnterior = client.getFoto();

            if (nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
                Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
                File archivoFotoAnterior = rutaFotoAnterior.toFile();

                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
                    archivoFotoAnterior.delete();
                }
            }
            // si no hay errores
            client.setFoto(nombreArchivo);

            clienteService.save(client);
            response.put("cliente", client);
            response.put("mensaje", "has subido correctamente la foto");
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/img/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {

        Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
        log.info(rutaArchivo.toString());
        Resource recurso = null;

        try {
            recurso = new UrlResource(rutaArchivo.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (!recurso.exists() && !recurso.isReadable()) {
            rutaArchivo = Paths.get("static/images").resolve("hasbu.png").toAbsolutePath();

            try {
                recurso = new UrlResource(rutaArchivo.toUri());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            log.error("Error no se pudo cargar la imagen");
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

        return new ResponseEntity<Resource>(recurso, HttpStatus.OK);
    }

    // listado de regiones
    //@Secured({"ROLE_ADMIN"})
    @GetMapping("clientes/regiones")
    public List<Region> listarRegiones() {
        return clienteService.findAllRegiones();
    }

}

