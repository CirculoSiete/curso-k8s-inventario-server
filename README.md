# curso-k8s-inventario-server
      
### Run

Para correr la aplicacion con Docker Compose, es requisito primero construir la imagen de Docker, la tarea `buildImage` se ancargará de construir la aplicación de Spring Boot y generar el Dockerfile requerido, además de construir la imagen de Docker de forma local.

      $ ./gradlew clean buildImage

Una vez construida, se puede proceder a levantar todo el ambiente (PostgreSQL, RabbitMQ) 

      $ docker-compose up --build
      
### Grpcc
Iniciar cliente:

      $ grpcc -p src/main/proto/InventarioService.proto -a 127.0.0.1:6565 -s InventarioService -i
      
Crear producto:

      $ client.inventarioCreate({nombre: "chicle", descripcion: "masticable", precio:5}, printReply)

Obtener producto:

      $ client.inventarioGet({id: 1}, printReply)
      
Eliminar producto:

      $ client.inventarioDelete({id: 1}, printReply)
      
Actualizar producto

      $ client.inventarioUpdate({id:1, nombre: "chicle", descripcion: "masticable :)", precio:5}, printReply)


      