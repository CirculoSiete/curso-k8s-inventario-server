# curso-k8s-inventario-server

### Build

Para generar las interfaces del servidor y el cliente GRPC basadas
en nuestro servicio _src/main/proto/InventarioService.proto_

      
      $ ./gradlew build
      
### Clean

Para limpiar las interfaces del servidor y el cliente GRPC.

      
      $ ./gradlew clean
      
### Run

Para correr la aplicacion con Docker Compose, es requisito primero construir la imagen de Docker, la tarea `buildImage` se ancargará de construir la aplicación de Spring Boot y generar el Dockerfile requerido, además de construir la imagen de Docker de forma local.

      $ ./gradlew clean buildImage
      $ docker-compose up --build

