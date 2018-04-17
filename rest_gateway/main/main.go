package main

import (
	"flag"
	"fmt"
	//"time"
	"github.com/caarlos0/env"
	_ "github.com/dimiro1/banner/autoload"
	"github.com/golang/glog"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"net/http"

	//"github.com/opentracing-contrib/go-stdlib/nethttp"
	/*opentracing "github.com/opentracing/opentracing-go"
	jaeger "github.com/uber/jaeger-client-go"
	"github.com/uber/jaeger-client-go/zipkin"*/

	gw "github.com/CirculoSiete/productGateway/warehouse"
)

type config struct {
	Port        int    `env:"PORT" envDefault:"9090"`
	ProductHost string `env:"PRODUCT_SVC_HOSTNAME" envDefault:"localhost"`
	ProductPort int    `env:"PRODUCT_SVC_PORT" envDefault:"6565"`
	ZipkinHost  string `env:"ZIPKIN_HOST" envDefault:"localhost"`
	ZipkinPort  int    `env:"ZIPKIN_PORT" envDefault:"9411"`
}

func run() error {
	cfg := config{}
	errParse := env.Parse(&cfg)
	if errParse != nil {
		fmt.Printf("%+v\n", errParse)
		return errParse
	}

	var (
		endpoint        = fmt.Sprintf("%s:%d", cfg.ProductHost, cfg.ProductPort)
		productEndpoint = flag.String("product_endpoint", endpoint, "endpoint of Products")
	)
	fmt.Printf("Product gRPC endpoint: %s\n", endpoint)
	fmt.Printf("Zipkin endpoint: %s:%d\n", cfg.ZipkinHost, cfg.ZipkinPort)

	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithInsecure()}
	err := gw.RegisterProductServiceHandlerFromEndpoint(ctx, mux, *productEndpoint, opts)

	if err != nil {
		return err
	}

	/*zipkinPropagator := zipkin.NewZipkinB3HTTPHeaderPropagator()
	injector := jaeger.TracerOptions.Injector(opentracing.HTTPHeaders, zipkinPropagator)
	extractor := jaeger.TracerOptions.Extractor(opentracing.HTTPHeaders, zipkinPropagator)

	// Zipkin shares span ID between client and server spans; it must be enabled via the following option.
	zipkinSharedRPCSpan := jaeger.TracerOptions.ZipkinSharedRPCSpan(true)

	sender, _ := jaeger.NewUDPTransport("jaeger-agent.istio-system:5775", 0)
	tracer, closer := jaeger.NewTracer(
		"myhelloworld",
		jaeger.NewConstSampler(true),
		jaeger.NewRemoteReporter(
			sender,
			jaeger.ReporterOptions.BufferFlushInterval(1*time.Second)),
		injector,
		extractor,
		zipkinSharedRPCSpan,
	)
	defer closer.Close()
	*/

	listen := fmt.Sprintf(":%d", cfg.Port)
	fmt.Printf("Product REST revert proxy started.\n")
	fmt.Printf("Listening on port %d\n", cfg.Port)
	return http.ListenAndServe(listen, mux)
}

func main() {
	flag.Parse()
	defer glog.Flush()

	if err := run(); err != nil {
		glog.Fatal(err)
	}
}
