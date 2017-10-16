package main

import (
	"flag"
	"fmt"
	"net/http"

	"github.com/caarlos0/env"
	"github.com/golang/glog"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
	"golang.org/x/net/context"
	"google.golang.org/grpc"

	gw "github.com/CirculoSiete/productGateway/warehouse"
)

type config struct {
	Port            int    `env:"PORT" envDefault:"9090"`
	ProductEndpoint string `env:"PRODUCT_ENDPOINT" envDefault:"localhost"`
	ProductPort     int    `env:"PRODUCT_PORT" envDefault:"6565"`
}

func run() error {
	cfg := config{}
	errParse := env.Parse(&cfg)
	if errParse != nil {
		fmt.Printf("%+v\n", errParse)
	}
	fmt.Printf("%+v\n", cfg)

	var (
		endpoint = fmt.Sprintf("%s:%d", cfg.ProductEndpoint, cfg.ProductPort)
		productEndpoint = flag.String("product_endpoint", endpoint, "endpoint of Products")
	)
	fmt.Printf("%s:%s\n", "Product Endpoint: ", endpoint)

	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithInsecure()}
	err := gw.RegisterProductServiceHandlerFromEndpoint(ctx, mux, *productEndpoint, opts)
	if err != nil {
		return err
	}

	listen := fmt.Sprintf(":%d", cfg.Port)
	fmt.Printf("%s:%s\n", "Listening... ", listen)
	return http.ListenAndServe(listen, mux)
}

func main() {
	flag.Parse()
	defer glog.Flush()

	if err := run(); err != nil {
		glog.Fatal(err)
	}
}
