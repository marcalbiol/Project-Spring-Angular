import {Component} from "@angular/core";
import {AuthService} from "../usuarios/auth.service";
import {Router} from "@angular/router";
import swal from "sweetalert2";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  title: string = 'Control de clientes app'

  constructor(public authService: AuthService, public router: Router) {
  }

  logout(): void {
    this.authService.logout();
    swal('Logout', 'Has cerrado sesion', 'success')
    this.router.navigate(['/login'])
  }

}
