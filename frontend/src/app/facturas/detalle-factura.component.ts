import {Component, OnInit} from '@angular/core';
import {FacturaService} from "./services/Factura.service";
import {Factura} from "./models/factura";
import {ActivatedRoute} from "@angular/router";
import {Cliente} from "../clientes/cliente";
import {ClienteService} from "../clientes/cliente.service";

@Component({
  selector: 'app-detalle-factura',
  templateUrl: './detalle-factura.component.html'
})
export class DetalleFacturaComponent implements OnInit {
  factura: Factura;
  titulo: string = 'Factura';
  cliente: Cliente;

  constructor(private facturaService: FacturaService,
              private activatedRoute: ActivatedRoute,
              private clienteService: ClienteService) {
  }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      let id = +params.get('id');
      this.facturaService.getFactura(id).subscribe
      (factura => this.factura = factura);
      this.facturaService.getFactura(id).subscribe(factura => {
        this.factura = factura
      });
      this.clienteService.getCliente(id).subscribe(factura => {
        this.cliente = factura
      })
    });
  }
}
