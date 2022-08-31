package com.example.demo.models.services;

//metodos del crud
// para realizar consultas

import com.example.demo.models.entity.Client;
import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.Producto;
import com.example.demo.models.entity.Region;

import java.util.List;

public interface IClienteService {
    public List<Client> findAll();

    // estos metodos se inyectan luego en el ServiceImp

    // devuelve un cliente id
    public Client save(Client client);

    // elimina
    public void delete(Long id);

    // find by id
    public Client findById(Long id);

    public List<Region> findAllRegiones();

    public Factura findFacturaById(Long id);

    public Factura saveFactura(Factura factura);

    public void deleteFacturaById(Long id);

    public List<Producto> findProductoByNombre(String term);


}
