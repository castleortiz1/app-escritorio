package http

import (
	"fmt"
	nethttp "net/http"
)

func StartServer(port string) error {
	mux := nethttp.NewServeMux()
	mux.HandleFunc("/health", func(writer nethttp.ResponseWriter, _ *nethttp.Request) {
		writer.WriteHeader(nethttp.StatusOK)
		_, _ = writer.Write([]byte("{\"status\":\"ok\"}"))
	})

	address := fmt.Sprintf(":%s", port)
	return nethttp.ListenAndServe(address, mux)
}
