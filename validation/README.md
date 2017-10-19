# curso-k8s-inventario-validacion

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
      
### Grpcc
Iniciar cliente:

      $ grpcc -p ValidacionService.proto -a 127.0.0.1:6565 -s ValidacionService -i
      
Validar creacion de  producto:

      $ client.validacionCreate({nombre:"tenis"}, printReply)