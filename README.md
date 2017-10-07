# curso-k8s-inventario-server
      
### Run

Para correr la aplicacion con Docker Compose, es requisito primero construir la imagen de Docker, la tarea `buildImage` se ancargará de construir la aplicación de Spring Boot y generar el Dockerfile requerido, además de construir la imagen de Docker de forma local.

      $ ./gradlew clean buildImage

Una vez construida, se puede proceder a levantar todo el ambiente (PostgreSQL, RabbitMQ) 

      $ docker-compose up --build
      
### Probar el servicio con un cliente de linea de comandos (CLI)

Se recomienda usar [`grpcc`](https://github.com/njpatel/grpcc) para poder probar el servicio en la linea de comandos. Se instala como un modulo de Node.



Una vez con `grpcc` instalado, se puede proceder a probar.

Iniciar cliente:

      $ grpcc -p src/main/proto/WarehouseService.proto -a 127.0.0.1:6565 -s ProductService -i
      
Crear producto:

      $ client.create({nombre: "chicle", descripcion: "masticable", precio:5}, printReply)

Obtener producto:

      $ client.read({id: 1}, printReply)
      
Actualizar producto

      $ client.update({id:1, nombre: "chicle", descripcion: "masticable :)", precio:5}, printReply)
      
Eliminar producto:

      $ client.delete({id: 1}, printReply)
      


      