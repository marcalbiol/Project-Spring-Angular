import {Component, OnInit} from '@angular/core';
import {Cliente} from "./cliente";
import {ClienteService} from "./cliente.service";
import {ActivatedRoute, Router} from '@angular/router';
import swal from 'sweetalert2';
import {Region} from "./region";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {
  public cliente: Cliente = new Cliente()
  regiones: Region[];
  public titulo: string = "Crear cliente"

  // invocamos el metodo create del service
  constructor(private clientService: ClienteService, private router: Router,
              private activatedRouter: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.cargarCliente();
    this.clientService.getRegiones().subscribe(regiones => this.regiones = regiones);

  }

  // recibe los parametros (id), preguntamos si el id existe, busca el cliente por el id
  cargarCliente(): void {
    this.activatedRouter.params.subscribe(params => {
      let id = params['id']
      if (id) {
        this.clientService.getCliente(id).subscribe((cliente) => this.cliente = cliente)
      }
    })
  }

  // se conectara al apirest
  create(): void {
    console.log(this.cliente)
    this.clientService.create(this.cliente)
      .subscribe(cliente => {
          this.router.navigate(['/clientes'])
          swal('nuevo cliente', `Cliente ${this.cliente.nombre} creado con exito`, 'success')
        }
      )
    console.log("Clicked")
    console.log(this.cliente)
  }

  // update
  update(): void {
    console.log(this.cliente)
    this.cliente.facturas = null;
    this.clientService.update(this.cliente)
      .subscribe(cliente => {
          this.router.navigate(['/clientes'])
          swal('Cliente actualizado', `Cliente ${this.cliente.nombre.toUpperCase()} actualizadocon exito`, 'success')
        }
      )
  }

  compararRegion(o1: Region, o2: Region): boolean {
    if (o1 === undefined && o2 === undefined) {
      return true;
    }
    return o1 === null || o2 === null || o1 === undefined || o2 === undefined ? false : o1.id === o2.id;
  }
}
