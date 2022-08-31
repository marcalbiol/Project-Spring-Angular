package com.example.demo.models.services;

import com.example.demo.models.dao.IClienteDao;
import com.example.demo.models.dao.IFacturaDao;
import com.example.demo.models.dao.IProductoDao;
import com.example.demo.models.entity.Client;
import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.Producto;
import com.example.demo.models.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // se registra en el contenedor
public class ClienteServiceImp implements IClienteService {

    /*  INYECCIONES DE ATRIBUTO */

    @Autowired
    // inyectamos clienteDao donde se encuentra la consulta a la base de datos (metodos) se recoge aqui
    // y hace un return de los datos
    private IClienteDao clienteDao;

    @Autowired
    private IFacturaDao facturaDao;

    @Autowired
    private IProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {

        // usamos el metodo clienteDae para acceder a los clientes. con el autowired se importa
        return (List<Client>) clienteDao.findAll();
    }
    //TODO PAGINACION
/*
    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        return clienteDao.findAll((org.springframework.data.domain.Pageable) pageable);
    }
 */

    @Override
    @Transactional
    public Client save(Client client) {
        return clienteDao.save(client);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clienteDao.findById(id).orElse(null);  // devuelve si lo encuentra, sino null
    }

    @Override
    @Transactional(readOnly = true)
    public List<Region> findAllRegiones() {
        // devuelve el listado completo de regiones
        return clienteDao.findAllRegiones();
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Factura saveFactura(Factura factura) {
        return facturaDao.save(factura);
    }

    @Override
    @Transactional
    public void deleteFacturaById(Long id) {
        facturaDao.deleteById(id);
    }

    @Override
    @Transactional
    public List<Producto> findProductoByNombre(String term) {
        // hay 3 metodos por escoger
        return productoDao.findByNombreContainingIgnoreCase(term);
    }
}
