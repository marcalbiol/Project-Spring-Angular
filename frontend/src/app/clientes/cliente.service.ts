/*
    AQUI SE OTIENE LA LOGICA DESDE EL BACKEND
 */
import {Injectable} from '@angular/core';
import {Cliente} from "./cliente";
/* IMPORTADO DESDE CLIENTES.JSON.TS  */
import {catchError, map, Observable, tap, throwError} from 'rxjs';

// para importar a traves de manera remota del servidor
import {HttpClient, HttpEvent, HttpHeaders, HttpRequest} from "@angular/common/http";
import swal from "sweetalert2";
import {Router} from '@angular/router';
import {Region} from "./region";
import {AuthService} from "../usuarios/auth.service";

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private urlEndPoint: string = 'http://localhost:8080/api/clientes'  // donde se encuentra el servidor de backend, apirest

  private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'})

  constructor(private http: HttpClient, private router: Router, private authService: AuthService) {
  }

  getRegiones(): Observable<Region[]> {
    // retornamos, y vamos a buscar las regiones a la api rest
    return this.http.get<Region[]>(this.urlEndPoint + '/regiones', {headers: this.agregarAutorizacion()}).pipe(catchError(e => {
      this.noAutorizado(e); // manejo de error para regiones
      return throwError(e);
    }))
  }

  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.urlEndPoint).pipe(
      tap(response => {
        let clientes = response as Cliente[];
        response.forEach(cliente => {
          console.log(cliente.nombre);
        })
      }),
      map(response => {
        let clientes = response as Cliente[];
        return clientes.map(cliente => {

          // cambia el formato de los nombres a mayuscula
          cliente.apellido = cliente.apellido.toUpperCase();

          // cambiar formato a la fecha
          //  cliente.createAt = formatDate(cliente.createAt, 'EEEE dd, MMMM, yyyy', 'es');
          // devolvemos cada cliente con el upperCAse
          return cliente;
        });
      })
    );
  }

  // usamos post, para pasar la url(urlEndpoint), pasamos los datos(clientes) y pasamos el header
  create(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.urlEndPoint, cliente, {headers: this.agregarAutorizacion()}).pipe
    (catchError(e => {

        if (this.noAutorizado(e)) {
          return throwError(e);
        }

        // controla si llega el error 400 bad request desde la validacion al crear el cliente
        if (e.status == 400) {
          return throwError(e);
        }

        // no es necesario el routr
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    )
  }

  getCliente(id): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`, {headers: this.agregarAutorizacion()}).pipe(
      catchError(e => {
        if (this.noAutorizado(e)) {
          return throwError(e);
        }
        this.router.navigate(['/clientes']);
        swal('Error al editar', e.error.mensaje, 'error');
        return throwError(e);
      })
    )
  }

  // recibira el objeto cliente JSON

  // funcion para editar
  update(cliente: Cliente): Observable<Cliente> {
    // cliente que vamos a actualizar, || datos del cliente \\ y cabecera
    return this.http.put<Cliente>(`${this.urlEndPoint}/${cliente.id}`, cliente,
      {headers: this.agregarAutorizacion()}).pipe(catchError(e => {

        if (this.noAutorizado(e)) {
          return throwError(e);
        }

        // controla si llega el error 400 bad request desde la validacion al crear el cliente
        if (e.status == 400) {
          return throwError(e);
        }

        // no es necesario el routr
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    )
  }

  delete(id: number): Observable<Cliente> {
    return this.http.delete<Cliente>(`${this.urlEndPoint}/${id}`,
      {headers: this.agregarAutorizacion()}).pipe(catchError(e => {
        if (this.noAutorizado(e)) {
          return throwError(e);
        }
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    )
  }

  subirFoto(archivo: File, id): Observable<HttpEvent<any>> {
    let formData = new FormData();
    formData.append("archivo", archivo);
    formData.append("id", id)

    let httpHeader = new HttpHeaders();
    let token = this.authService.token;
    if (token != null) {
      httpHeader = httpHeader.append('Authorization', 'Bearer' + token);
    }

    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData, {
      reportProgress: true,
      headers: httpHeader
    });
    return this.http.request(req).pipe(catchError(e => {
      this.noAutorizado(e);
      return throwError(e);
    }));
  }

  private agregarAutorizacion() {
    let token = this.authService.token;
    if (token != null) {
      return this.httpHeaders.append('Authorization', 'Bearer' + token)
    } else {
      return this.httpHeaders; // vacio
    }
  }

  private noAutorizado(e): boolean {
    if (e.status == 401) {
      if (this.authService.estaLogeado()) {
        this.authService.logout();
      }
      this.router.navigate(['/login'])
      return true;
    }

    if (e.status == 403) {
      swal('Acceso denegado', '', 'warning')
      this.router.navigate(['/clientes']);
      return true;
    }
    return false;
  }
}

