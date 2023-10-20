use apidamii;

insert into tb_rol values
(1, 'admin'),
(2, 'visitante');

insert into tb_enlace values 
(1, 'usuario', 1, '/api/usuario'),
(2, 'Dashboard', 1, '/inicio/dashboard'),
(3, 'Libros', 1, '/inicio/libros'),
(4, 'publicados', 1, '/inicio/publicaciones'),
(5, 'Libros', 1, '/inicio/librostabla');

insert into tb_rol_has_enlace values  
(1, 1),
(2, 2),
(3, 1),
(4, 2),
(5,1); 



insert into tb_genero values
(1, 'Ciencia Ficción'),
(2, 'Cocina'),
(3, 'Comicos'),
(4, 'Fantasia'),
(5, 'Romance'),
(6, 'Terror');

Insert into tb_usuario values
(1, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','admin@email.com',1 ,'admin', now(), 1);

