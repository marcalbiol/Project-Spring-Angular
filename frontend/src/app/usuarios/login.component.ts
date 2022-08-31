import {Component} from "@angular/core";
import {Usuario} from "./usuario";
import swal from 'sweetalert2';
import {AuthService} from "./auth.service";
import {Router} from '@angular/router'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  titulo: string = 'Inicia sesion'

  usuario: Usuario;

  constructor(private authService: AuthService, private router: Router) {
    this.usuario = new Usuario();
  }

  ngOnInit() {
    // comprobaremos si estamos logeados
    if (this.authService.estaLogeado()) {
      this.router.navigate(['/clientes'])
      swal('Login', `Ya estas logeado`, 'info')
    }
  }

  login(): void {
    //funcion que muestra el usuario cada vez que iniciamos sesion
    console.log(this.usuario)

    if (this.usuario.nombre == null || this.usuario.password == null) {
      swal('Error Login', 'usuario o password vacio', 'error')

      return;
    }

    // en todos los observables se tiene que utilizar el subscribe para que ejecute alguna funcion
    this.authService.login(this.usuario).subscribe(response => {

        // contiene todos los datos del usuario -> response acces token
        this.authService.guardarUsuario(response.access_token);
        this.authService.guardarToken(response.access_token);
        let usuario = this.authService.usuario;
        this.router.navigate(['/clientes']);
        swal('Login', `Hola ${usuario.username}, has iniciado sesion con exito`, 'success')
      }, error => {
        if (error.status == 400) {
          swal('Error Login', 'Usuario o password incorrecta', 'error')
        }
      }
    );
  }


}
