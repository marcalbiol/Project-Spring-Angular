import {Component, Input, OnInit} from '@angular/core';
import {Cliente} from '../cliente';
import {ClienteService} from "../cliente.service";
import {ActivatedRoute, Router} from "@angular/router";
import swal from "sweetalert2";
import {HttpEventType} from "@angular/common/http";
import {ModalService} from "./modal.service";
import {AuthService} from "../../usuarios/auth.service";
import {Factura} from "../../facturas/models/factura";
import {FacturaService} from "../../facturas/services/Factura.service";

@Component({
  selector: 'detalle-cliente',
  templateUrl: './detalle.component.html'
})

export class DetalleComponent implements OnInit {

  @Input() cliente: Cliente;
  titulo: string = "Detalle del cliente";
  public fotoSeleccionada: File;
  public progreso: number = 0;

  constructor(private clienteService: ClienteService
    , private activatedRuote: ActivatedRoute
    , public modalService: ModalService
    , private router: Router
    , public authService: AuthService,
              private FacturaService: FacturaService) {

  }

  ngOnInit() {
    /*
     this.activatedRuote.paramMap.subscribe(params => {
      let id: number = +params.get('id');
      if (id) {
        this.clienteService.getCliente(id).subscribe(cliente => {
          this.cliente = cliente;
        });
      }
    });
     */
  }

  seleccionarFoto(event) {
    this.fotoSeleccionada = event.target.files[0];
    this.progreso = 0;
    //validacion si es image o no
    if (this.fotoSeleccionada.type.indexOf('image') < 0) {
      // no es image
      swal('Error seleccionar imagen', 'El archivo debe ser una imagen', 'error')
      this.fotoSeleccionada = null;
    }
  }

  subirFoto() {
    // validar que se sube una foto y no otro archivo
    if (!this.fotoSeleccionada) {
      swal('Error Upload', 'Error: debes seleccionar una foto', 'error')
    } else {
      this.clienteService.subirFoto(this.fotoSeleccionada, this.cliente.id).subscribe(event => {

        // progreso de el upload de la foto
        if (event.type === HttpEventType.UploadProgress) {
          this.progreso = Math.round(100 * event.loaded / event.total);
        } else if (event.type === HttpEventType.Response) {
          let response: any = event.body;
          this.cliente = response.cliente as Cliente

          // para actualizar el cliente y la pagina cuando se suba una nueva
          this.modalService.notificarUpload.emit(this.cliente);

          swal('La foto se ha subido correctamente', response.mensaje, 'success');
        }
      })
    }
  }

  cerrarModal() {
    this.modalService.cerrarModal();
    this.fotoSeleccionada = null;
    this.progreso = 0;
  }

  delete(factura: Factura): void {
    // @ts-ignore
    swal({
      title: 'Estas seguro que quieres eliminar',
      text: `${factura.descripcion}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.FacturaService.delete(factura.id).subscribe(response => {
          this.cliente.facturas = this.cliente.facturas.filter(f => f !== factura)
          swal(
            'Factura eliminada',
            'Con exito',
            'success'
          )
        })
      }
    })
  }
}



