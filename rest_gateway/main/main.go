package main

import (
	"flag"
	"net/http"

	"github.com/golang/glog"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
	"golang.org/x/net/context"
	"google.golang.org/grpc"

	gw "github.com/CirculoSiete/productGateway/warehouse"
)

var (
	//TODO: sacar esto a variables de ambiente
	productEndpoint = flag.String("product_endpoint", "localhost:6565", "endpoint of Products")
)

func run() error {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithInsecure()}
	err := gw.RegisterProductServiceHandlerFromEndpoint(ctx, mux, *productEndpoint, opts)
	if err != nil {
		return err
	}

	//TODO: sacar esto a variables de ambiente
	return http.ListenAndServe(":9090", mux)
}

func main() {
	flag.Parse()
	defer glog.Flush()

	if err := run(); err != nil {
		glog.Fatal(err)
	}
}
