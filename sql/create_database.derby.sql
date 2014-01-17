-- create_database.derby.sql
--
-- Autores:
--   Garcia Limon, Jesus        <jglimon@esei.uvigo.es>
--   Gutierrez Jacome, Alberto  <agjacome@esei.uvigo.es>
--   Vazquez Fernandez, Pablo   <pvfernandez@esei.uvigo.es>
--
-- Ultima modificacion: 2013/12/07
--
-- Crea todas las tablas necesarias para la correcta ejecución del sistema. Se
-- asumirán como previamente creadas la base de datos y el usuario para la
-- misma (ver ejemplo), el script simplemente creara las tablas necesarias para
-- todas las entidades e insertará un conjunto de datos de ejemplo para las
-- mismas.
--
-- Ejemplo de uso desde una consola Derby (primer comando crea la base de datos
-- con un usuario "dai_user" y contraseña "dai_pass" dentro del directorio
-- "db/hybrid_server"):
--
-- ij> connect 'jdbc:derby:db/hybrid_server;create=true;user=dai_user;password=dai_pass';
-- ij> run 'sql/create_database.derby.sql';

-- posible ERROR 42x01 de las siguientes sentencias, si es la primera vez que
-- se ejecuta el script, puesto que la tablas aun no existiran, puede ignorarse
-- el error sin mayores repercusiones
DROP TABLE HTML;
DROP TABLE XMLT;
DROP TABLE XSD;
DROP TABLE XSLT;

-- tabla para documentos HTML
CREATE TABLE HTML (
    uuid    CHAR(36) NOT NULL,
    content LONG VARCHAR,

    PRIMARY KEY(uuid)
);

-- tabla para documentos XML
CREATE TABLE XMLT (
    uuid    CHAR(36) NOT NULL,
    content LONG VARCHAR,

    PRIMARY KEY(uuid)
);

-- tabla para documentos XSD
CREATE TABLE XSD (
    uuid    CHAR(36) NOT NULL,
    content LONG VARCHAR,

    PRIMARY KEY(uuid)
);

-- tabla para documentos XSLT
CREATE TABLE XSLT (
    uuid    CHAR(36) NOT NULL,
    xsd     CHAR(36) NOT NULL,
    content LONG VARCHAR,

    PRIMARY KEY(uuid)
);

-- insercion de tuplas de ejemplo en la tabla de documentos HTML
INSERT INTO HTML VALUES ('03f5881b-4d92-4990-92a0-d19af3531f8c', '<html>\n<head>\n<title>Contenido 01</title>\n</head>\n<body>\n<h1>Contenido Numero 01</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('1b4d6578-f627-4e49-b628-d0a8bc305393', '<html>\n<head>\n<title>Contenido 02</title>\n</head>\n<body>\n<h1>Contenido Numero 02</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('e83fdd55-ab11-41cd-9039-30f4b138f0ae', '<html>\n<head>\n<title>Contenido 03</title>\n</head>\n<body>\n<h1>Contenido Numero 03</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('c2a5a0c0-a5af-4711-a6da-36fb20c71a24', '<html>\n<head>\n<title>Contenido 04</title>\n</head>\n<body>\n<h1>Contenido Numero 04</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('809c1096-388a-4388-9793-b443f374b030', '<html>\n<head>\n<title>Contenido 05</title>\n</head>\n<body>\n<h1>Contenido Numero 05</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('06b93643-738a-4043-b1fd-80f2e2598271', '<html>\n<head>\n<title>Contenido 06</title>\n</head>\n<body>\n<h1>Contenido Numero 06</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('122e029a-d4d7-45a5-838b-bd4af566b7da', '<html>\n<head>\n<title>Contenido 07</title>\n</head>\n<body>\n<h1>Contenido Numero 07</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('d659c600-57ec-4fbb-a031-61db37bfdc91', '<html>\n<head>\n<title>Contenido 08</title>\n</head>\n<body>\n<h1>Contenido Numero 08</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('91b63245-1252-4298-ad97-045b357fc8d8', '<html>\n<head>\n<title>Contenido 09</title>\n</head>\n<body>\n<h1>Contenido Numero 09</title>\n</body>\n</html>');
INSERT INTO HTML VALUES ('7f28c1a0-0145-49b3-be9d-839a8612e186', '<html>\n<head>\n<title>Contenido 10</title>\n</head>\n<body>\n<h1>Contenido Numero 10</title>\n</body>\n</html>');
