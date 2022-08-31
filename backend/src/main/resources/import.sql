/* REGIONES */
INSERT INTO regiones (id, nombre)
VALUES (1, 'ESPANYA');
INSERT INTO regiones (id, nombre)
VALUES (2, 'FRANCIA');
INSERT INTO regiones (id, nombre)
VALUES (3, 'ITALIA');
INSERT INTO regiones (id, nombre)
VALUES (4, 'RUSIA');
/* CLIENTES */
insert into clientes (region_id, apellido, create_at, email, nombre)
VALUES (1, 'Albiol', '2020-12-03', 'marc@gmail.com', 'marc');
insert into clientes (region_id, apellido, create_at, email, nombre)
VALUES (3, 'Vasco', '2020-12-03', 'nicol@gmail.com', 'nicol');
insert into clientes (region_id, apellido, create_at, email, nombre)
VALUES (3, 'Alcacer', '2020-12-03', 'paco@gmail.com', 'paco');

/* USUARIOS Y ROLES */
-- USUARIOS
INSERT INTO usuarios (username, password, enabled)
VALUES ('marc', '$2a$10$GgKzfwvm8nW2JXSo8/0OHOYg3NjyYmA0V/N6M99F2VLAxknQ7fZcy', 1);
INSERT INTO usuarios (username, password, enabled)
VALUES ('admin', '$2a$10$IChh4eMnfqq.r32SK/S1zejeodCEZBOtNa5CjJJLSYwdZiV4vINtG', 1);

-- ROLES
INSERT INTO roles (nombre)
VALUES ('ROLE_USER');
INSERT INTO roles (nombre)
VALUES ('ROLE_ADMIN');

-- ASIGNACION DE ROLES
INSERT into usuarios_roles (usuarios_id, roles_id)
VALUES (1, 1);
INSERT into usuarios_roles (usuarios_id, roles_id)
VALUES (2, 2);
INSERT into usuarios_roles (usuarios_id, roles_id)
VALUES (2, 1);

-- PRODUCTOS
INSERT INTO productos (nombre, precio)
VALUES ('Panasonic Pantalla LCD', 300);
INSERT INTO productos (nombre, precio)
VALUES ('Sony Camara digital DSC-W320B', 500);
INSERT INTO productos (nombre, precio)
VALUES ('Apple iPod shuffle', 150);
INSERT INTO productos (nombre, precio)
VALUES ('Sony Notebook Z110', 700);
INSERT INTO productos (nombre, precio)
VALUES ('Hewlett Packard Multifuncional F2280', 220);
INSERT INTO productos (nombre, precio)
VALUES ('Bianchi Bicicleta Aro 26', 900);
INSERT INTO productos (nombre, precio)
VALUES ('Mica Comoda 5 Cajones', 499);

/* MODIFICAR PRODUCTOS */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at)
VALUES ('Factura equipos de oficina', null, 1, NOW());

INSERT INTO facturas_items (cantidad, factura_id, producto_id)
VALUES (1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id)
VALUES (2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id)
VALUES (1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id)
VALUES (1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at)
VALUES ('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id)
VALUES (3, 2, 6);