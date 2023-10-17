use apidamii;

insert into tb_rol values
(1, 'admin'),
(2, 'visitante');

insert into tb_enlace values
(1, 'usuario', 1, '/api/usuario'),
(2, 'Dashboard', 1, '/inicio/dashboard'),
(3, 'Libros', 1, '/inicio/libros');

insert into tb_rol_has_enlace values
(1, 1),
(2, 2),
(3, 2);

insert into tb_genero values
(1, 'Ciencia Ficción'),
(2, 'Cocina'),
(3, 'Comicos'),
(4, 'Fantasia'),
(5, 'Romance'),
(6, 'Terror');


