package com.example.demo.controllers;

import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.Producto;
import com.example.demo.models.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class FacturaRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/facturas/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Factura show(@PathVariable Long id) {
        return clienteService.findFacturaById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("facturas/{id}")
    public void delete(@PathVariable Long id) {
        clienteService.findFacturaById(id);
    }

    @GetMapping("/facturas/filtrar-productos/{term}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Producto> filtrarProductos(@PathVariable String term) {
        return clienteService.findProductoByNombre(term);
    }

    @PostMapping("/facturas")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Factura crear(@RequestBody Factura factura) {
        return clienteService.saveFactura(factura);
    }

}
