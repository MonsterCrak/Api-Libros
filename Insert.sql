-- create database apidamii;
use apidamii;

insert into tb_rol values
(1, 'admin'),
(2, 'visitante');

insert into tb_enlace values 
(1, 'Dashboard', 1, '/inicio/dashboard'),
(2, 'Libros', 1, '/inicio/publicaciones'),
(3, 'Mantenimiento Libros', 1, '/inicio/librostabla'),
(4, 'Perfil', 1, '/inicio/perfil');

insert into tb_rol_has_enlace values  
(1, 2),
(2, 1),
(2, 2),
(3, 1),
(4, 2);


insert into tb_genero values
(1, 'Ciencia Ficción'),
(2, 'Cocina'),
(3, 'Comicos'),
(4, 'Fantasia'),
(5, 'Romance'),
(6, 'Terror');

Insert into tb_usuario(clave, email, estado, nombre, paterno, materno, fecha_registro, id_rol) values 
('a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','admin@email.com',1 ,'admin', 'admin', 'admin', now(), 1);

