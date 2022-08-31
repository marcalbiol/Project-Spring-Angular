import {Component, OnInit} from '@angular/core';
import {ClienteService} from "./cliente.service";
import {Cliente} from "./cliente";
import {ModalService} from "./detalle/modal.service";
import swal from "sweetalert2";
import {AuthService} from "../usuarios/auth.service";

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
})
export class ClientesComponent implements OnInit {

  clientes: Cliente[];
  clienteSeleccionado: Cliente;

  constructor(private clienteService: ClienteService
    , public modalService: ModalService
    , public authService: AuthService) {
  }

  ngOnInit(): void {
    this.clienteService.getClientes().subscribe(
      clientes => this.clientes = clientes
    );

    this.modalService.notificarUpload.subscribe(cliente => {
      // el map devuelve a los clientes modificados
      this.clientes = this.clientes.map(clienteOriginal => {
        if (cliente.id == clienteOriginal) {
          clienteOriginal.foto = cliente.foto;
        }
        return clienteOriginal;
      })
    })
  }

  delete(cliente: Cliente): void {
    // @ts-ignore
    swal({
      title: 'Seguro que quieres eliminar?',
      text: 'Cliente: ' + cliente.nombre.toUpperCase() + ' ' + cliente.apellido.toUpperCase(),
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.clienteService.delete(cliente.id).subscribe(response => {
          this.clientes = this.clientes.filter(cli => cli !== cliente)
          swal(
            'Cliente eliminado',
            'Con exito',
            'success'
          )
        })
      }
    })
  }

  abrirModal(cliente: Cliente) {
    // asignamos al cliente pasado por parametro a this.clienteSeleccionado
    this.clienteSeleccionado = cliente;
    //invocamos el metodo de la clase modal que lo abre
    this.modalService.abrirModal();
  }
}
