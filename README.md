Servidor Híbrido de Documentos Estructurados
============================================

Autores
-------

* García Limón, Jesús
* Gutiérrez Jácome, Alberto
* Vázquez Fernández, Pablo

Instalación y ejecución
-----------------------

Se asume la previa compilación, con un compilador de código Java, de todo el
código fuente dentro del árbol de directorios `src/`. Se asume, además, que
todos los ficheros .class generados en la compilacion cuelgan de un arbol de
directorios `bin/` bajo la misma estructura que el directorio del código fuente
original. Para dicha tarea se recomienda el uso de alguna herramienta de
compilacion de codigo Java, como pueden ser Ant, Maven o incluso algun IDE como
Eclipse o IntelliJ, y no ser realizada manualmente con el compilador de Java
(javac).

#### Configuración

La configuración del servidor híbrido de documentos estructurados se realiza a
través de un fichero XML encontrado en la raíz del proyecto bajo el nombre
configuration.xml. Dicho fichero de configuración cuenta con tres grandes
bloques de parámetros, el primero de ellos, `connections`, presenta el
siguiente aspecto:

    <connections>
      <http>10001</http>
      <webservice>http://localhost:20001/xmlserver</webservice>
      <numClients>50</numClients>
    </connections>

El elemento http hace referencia al puerto donde escuchará el servidor dentro
de la máquina local que lo ejecute. El elemento webservice hace referencia a la
URL que utilizará el servidor dentro de la máquina local para la publicación
del servicio web desde el que podrán solicitar documentos el resto de los
servidores de la red P2P. El último de los elementos, numClients especifica el
número de clientes concurrentes que es capaz de soportar el servidor
ejecutándose en la máquina local.

El segundo de los bloques de configuración, `database`, hace referencia a la
conexión a la base de datos para el servidor local, y presenta el siguiente
aspecto:

    <database>
      <user>dai_user</user>
      <password>dai_pass</password>
      <url>jdbc:mysql://localhost/HybridServer</url>
    </database>

El elemento `user` hace referencia al nombre de usuario que el servidor local
utilizará para conectarse a la base de datos. A su vez, el elemento `password`
hace referencia a la contraseña de acceso a la base de datos utilizada para
dicho usuario. Y por último, el elemento `url` hace referencia a la URL de
conexión a la base de datos, que será utilizada por JDBC para realizar la
conexión desde el programa Java.

El último bloque de configuración, `servers`, hace referencia a los distintos
servidores remotos a utilizar para recuperar documentos que no se encuentren
dentro del servidor local, proporcionando así una red P2P para compartir
documentos dentro de todos los servidores híbridos desplegados.

    <servers>
      <server name="Server 2"
              wsdl="http://localhost:20002/xmlserver?wsdl"
              namespace="http://service.controller.hybridserver.dai.esei.uvigo.es/"
              service="DocumentServiceImplService"
              httpAddress="http://localhost:10002/" />
    </servers>

El bloque `servers` puede contar con tantos elementos `server` como considere
necesario según la red de servidores utilizada. Cada elemento server cuenta con
un conjunto de atributos que definen la conexión realizada hacia el mismo,
`name` establece el nombre que el servidor local utilizará para nombrar a dicho
servidor (se mostrará en los listados de documentos), `wsdl` hace referencia a
la URL del WSDL del servidore remoto, al igual que `namespace` hace referencia
al espacio de nombres que el servicio del servidor remoto utiliza. El atributo
`service` hace referencia al servicio proporcionado por el servidor remoto, que
utilizará el servidor local para obtener los documentos. Se tratará de una
clase Java que implemente la interfaz DocumentService existente dentro del
proyecto en el paquete `es.uvigo.esei.dai.hybridserver.controller.service`. Por
último, el atributo `httpAddress` hace referencia a la URL del servidor HTTP
remoto.

Conste que dicho fichero de configuración será validado según el esquema XSD
existente en `xml/configuration.xsd`, y que es necesario proporcionar la
localización de dicho fichero en el atributo `xsi:schemaLocation` dentro del
elemento raíz `configuration` del fichero de configuración. El atributo
schemaLocation proporcionado en el fichero de configuracion de ejemplo ha sido
especificado de forma correcta para que la validación pueda realizarse
satisfactoriamente.

#### Ejecución

Para la ejecución del servidor híbrido de documentos estructurados, desde la
raíz del proyecto, y asumiendo que todo el código haya sido compilado dentro
del directorio `bin`, puede realizarse con el siguiente comando:

    $ java -cp bin:lib/mysql.jar es.uvigo.esei.dai.hybridserver.Launcher configuration.xml

Nótese la inclusión en el Classpath del fichero `lib/mysql.jar`. dicho fichero
JAR proporcionaría el driver para la conexión a una base de datos MySQL. Éstos
drivers serán necesarios siempre que se desee realizar una conexión hacia una
base de datos, y dependerán de la plataforma concreta. Habitualmente, la página
web del sistema de bases de datos dispone de un apartado de descargas desde
donde obtener dichos ficheros JAR con la implementación del driver adecuado.

#### Configuracion de la base de datos

La base de datos hacia donde se desee conectar el servidor deberán estar
disponibles las tablas `HTML`, `XMLT`, `XSD` y `XSLT`. Todas ellas debeán
contar con una columna `uuid` y una columna `content`. A mayores, la tabla
`XSLT` debe contar con otra columna `xsd` que haga referencia al `uuid` de la
tabla `XSD`, pero sin configurarse como una clave foránea hacia la misma.

Dentro del directorio `sql/` se proporcionan dos scripts SQL para la creacion
de dichas tablas, junto a la inserción de diez documentos HTML de prueba, en un
sistema MySQL y en un sistema Apache Derby.

El script para Apache Derby asumira que ya se ha establecido (e.g. por consola)
una conexion a una base de datos existente donde crear las tablas, puesto que el
sistema no proporciona ninguna sintaxis SQL para la creacion directa. Mientras
tanto, el script para MySQL se encargará de la creación de una base de datos
con el nombre `HybridServer` y el usuario `dai_user` con contraseña `dai_pass`.

Documentación
-------------

Dentro del directorio `doc/` se encuentra la documentación técnica del sistema,
incluyendo el subdirectorio `javadoc` con toda la documentación JavaDoc del
código presente en el directorio `src/`, y un documento PDF con documentación
adiccional, así como un pequeño manual de usuario sobre cómo utilizar el
servidor híbrido desde el punto de vista del cliente.

