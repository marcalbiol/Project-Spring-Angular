package com.example.demo.models.dao;

import com.example.demo.models.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
