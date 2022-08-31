package com.example.demo.models.dao;

import com.example.demo.models.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {

    // para utilizar estos metodos, se inyectan en el Service
    // metodo que filtra a los productos
    // METODO MANUAL QUE IMPLEMENTA LA CONSULTA
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByNombre(String term);

    // AUTOMATICO CON EL METODO RESERVADO
    // utilizando el metodo 'containing' hace la misma funcion que el Query de arriba, anida con los % en ambos lados
    public List<Producto> findByNombreContainingIgnoreCase(String term);

    public List<Producto> findByNombreStartingWithIgnoreCase(String term);


}
