import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Usuario} from "./usuario";

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private http: HttpClient) {
  }

  private _usuario: Usuario;

  public get usuario(): Usuario {
    if (this._usuario != null) { // si existe
      return this._usuario;
    } else if (this._usuario == null && sessionStorage.getItem("Usuario") != null) {
      this._usuario = JSON.parse(sessionStorage.getItem('Usuario')) as Usuario;
      return this._usuario
    }
    return new Usuario();
  }

  private _token: string;

  public get token(): string {
    if (this._token != null) { // si existe
      return this._token;
    } else if (this._token == null && sessionStorage.getItem("token") != null) {
      this._token = sessionStorage.getItem('token');
      return this._token
    }
    return null;
  }

  // AUTENTICACION
  login(usuario: Usuario): Observable<any> {

    const urlEndpoint = 'http://localhost:8080/oauth/token';

    const credenciales = btoa('angularapp' + ':' + '12345');

    const httpHeaders = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + credenciales
    });
    let params = new URLSearchParams();
    params.set('grant_type', 'password');
    params.set('username', usuario.nombre);
    params.set('password', usuario.password);

    console.log(params.toString);
    // realizamos la peticion
    return this.http.post<any>(urlEndpoint, params.toString(), {headers: httpHeaders})
  }

  // GUARDAR TOKENS Y USUARIO EN EL SESION STORAGE

  guardarUsuario(accessToken: string): void {
    let payload = this.obtenerDatosToken(accessToken);
    this._usuario = new Usuario();
    this._usuario.nombre = payload.nombre;
    this._usuario.apellido = payload.apellido;
    this._usuario.username = payload.user_name;
    this._usuario.roles = payload.authorities;
    sessionStorage.setItem('Usuario', JSON.stringify(this._usuario)); // convierte objeto en string
  }


  guardarToken(accessToken: string): void {
    this._token = accessToken;
    sessionStorage.setItem('token', accessToken);
  }

  obtenerDatosToken(accessToken: string): any {
    if (accessToken != null) {
      return JSON.parse(atob(accessToken.split(".")[1]))
    }
    return null;
  }

  public estaLogeado(): boolean {
    let payload = this.obtenerDatosToken(this.token);
    if (payload != null && payload.user_name && payload.user_name.length > 0) {
      return true;
    } else {
      return false;
    }
  }

  //LOGOUT
  logout(): void {
    this._token = null;
    this._usuario = null;
    sessionStorage.clear();
  }

  hasRole(role: string): boolean {
    if (this.usuario.roles.includes(role)) {
      return true;
    } else {
      return false;
    }


  }


}

