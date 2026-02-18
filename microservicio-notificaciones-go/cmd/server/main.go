package main

import (
	"log"
	"os"

	"github.com/appescritorio/microservicio-notificaciones-go/internal/consumer"
	apphttp "github.com/appescritorio/microservicio-notificaciones-go/internal/http"
)

func main() {
	rabbitURL := envOrDefault("RABBITMQ_URL", "amqp://app:app@localhost:5672/")
	queueName := envOrDefault("RABBITMQ_QUEUE", "stock.low")
	port := envOrDefault("PORT", "8082")

	stopConsumer, err := consumer.StartLowStockConsumer(rabbitURL, queueName)
	if err != nil {
		log.Printf("consumer no iniciado: %v", err)
	} else {
		defer stopConsumer()
	}

	if err := apphttp.StartServer(port); err != nil {
		log.Fatalf("error iniciando servidor: %v", err)
	}
}

func envOrDefault(name string, fallback string) string {
	value := os.Getenv(name)
	if value == "" {
		return fallback
	}
	return value
}
