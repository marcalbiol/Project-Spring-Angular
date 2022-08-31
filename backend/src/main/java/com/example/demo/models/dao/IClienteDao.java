package com.example.demo.models.dao;

import com.example.demo.models.entity.Client;
import com.example.demo.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
// import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends JpaRepository<Client, Long> {

    //metodo para obtener el listado de regiones
    @Query("from Region")
    public List<Region> findAllRegiones();

}
