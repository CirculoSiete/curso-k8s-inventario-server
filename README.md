# curso-k8s-inventario-server

### Build

Para generar las interfaces del servidor y el cliente GRPC basadas
en nuestro servicio _src/main/proto/InventarioService.proto_

      
      $ ./gradlew build
      
### Clean

Para limpiar las interfaces del servidor y el cliente GRPC.

      
      $ ./gradlew clean
      
### Run

Para correr la aplicacion.

      $ ./gradlew bootRun
      
#### Correr localmente
Para correr localmente es necesario utilizar el archivo de configuracion
`Application-local.yml`
