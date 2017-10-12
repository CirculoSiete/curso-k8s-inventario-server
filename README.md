# curso-k8s-inventario-server
      
### Run

Para correr la aplicacion con `Docker Compose`, es requisito primero construir la imagen de `Docker`, la tarea `buildImage` se encargará de construir la aplicación de `Spring Boot` y generar el `Dockerfile` requerido, además de construir la imagen de `Docker` de forma local.

      $ ./gradlew clean buildImage

Una vez construida, se puede proceder a levantar todo el ambiente (`PostgreSQL`, `RabbitMQ`) 

      $ docker-compose up --build
      
### Probar el servicio con un cliente de linea de comandos (CLI)

Se recomienda usar [`grpcc`](https://github.com/njpatel/grpcc) para poder probar el servicio en la linea de comandos. Se instala como un modulo de Node.



Una vez con `grpcc` instalado, se puede proceder a probar.

Iniciar cliente:

      $ grpcc -p src/main/proto/WarehouseService.proto -a 127.0.0.1:6565 -s ProductService -i
      
Crear producto:

      $ client.create({name: "chicle", description: "masticable", price:"5"}, printReply)

Obtener producto:

      $ client.read({id: 1}, printReply)
      
Actualizar producto

      $ client.update({id:1, name: "chicle", description: "masticable :)", price:"5"}, printReply)
      
Eliminar producto:

      $ client.delete({id: 1}, printReply)
      
Listar los productos

* Paginación con defaults (Page=0, Size=100)

      $ var productos = client.list({});
      
* Paginación (Page=1, Size=3)
      
      $ var productos = client.list({page: 1, size: 3});
      
* Para obtener el stream de datos
     
      $ productos.on('data', function(p) { console.log(p); });
      
* Para saber cuando se termino el stream

      $ productos.on('end', function() { console.log("No hay mas productos"); });