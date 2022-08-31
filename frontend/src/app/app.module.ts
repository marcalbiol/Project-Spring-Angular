import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {HeaderComponent} from "./header/header.component";
import {FooterComponent} from "./footer/footer.component";
import {DirectivaComponent} from './directiva/directiva.component';
import {ClientesComponent} from './clientes/clientes.component';
import {ClienteService} from "./clientes/cliente.service";
import {RouterModule, Routes} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {FormComponent} from './clientes/form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {registerLocaleData} from "@angular/common";
import localeES from '@angular/common/locales/es';
import {DetalleComponent} from "./clientes/detalle/detalle.component";
import {LoginComponent} from "./usuarios/login.component";
import {AuthGuard} from "./usuarios/guards/auth.guard";
import {RoleGuard} from "./usuarios/guards/role.guard";
import {DetalleFacturaComponent} from './facturas/detalle-factura.component';
import {FacturasComponent} from './facturas/facturas.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatInputModule} from "@angular/material/input";

registerLocaleData(localeES, 'es');
const router: Routes =
  [{path: '', redirectTo: '/clientes', pathMatch: 'full'},
    {path: 'directivas', component: DirectivaComponent},
    {path: 'clientes', component: ClientesComponent},
    {path: 'clientes/form', component: FormComponent, canActivate: [AuthGuard, RoleGuard], data: {role: 'ROLE_ADMIN'}},
    {
      path: 'clientes/form/:id',
      component: FormComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: {role: 'ROLE_ADMIN'}
    },
    {path: 'login', component: LoginComponent},
    {
      path: 'facturas/:id',
      component: DetalleFacturaComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: {role: 'ROLE_USER'}
    },
    {
      path: 'facturas/form/:clienteId',
      component: FacturasComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: {role: 'ROLE_ADMIN'}
    }
  ];

@NgModule({
  declarations: [AppComponent, HeaderComponent, FooterComponent, DirectivaComponent, ClientesComponent, FormComponent, DetalleComponent, LoginComponent, DetalleFacturaComponent, FacturasComponent],
  imports: [BrowserModule, HttpClientModule, FormsModule, RouterModule.forRoot(router), BrowserAnimationsModule, MatFormFieldModule, ReactiveFormsModule, MatAutocompleteModule, MatInputModule],
  providers: [ClienteService, {provide: LOCALE_ID, useValue: 'es'}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
