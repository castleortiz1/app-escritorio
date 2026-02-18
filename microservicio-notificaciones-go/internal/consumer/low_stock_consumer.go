package consumer

import (
	"log"

	amqp "github.com/rabbitmq/amqp091-go"
)

func StartLowStockConsumer(rabbitURL string, queueName string) (func(), error) {
	conn, err := amqp.Dial(rabbitURL)
	if err != nil {
		return nil, err
	}

	channel, err := conn.Channel()
	if err != nil {
		_ = conn.Close()
		return nil, err
	}

	_, err = channel.QueueDeclare(queueName, true, false, false, false, nil)
	if err != nil {
		_ = channel.Close()
		_ = conn.Close()
		return nil, err
	}

	messages, err := channel.Consume(queueName, "", true, false, false, false, nil)
	if err != nil {
		_ = channel.Close()
		_ = conn.Close()
		return nil, err
	}

	go func() {
		for message := range messages {
			log.Printf("evento low-stock recibido: %s", string(message.Body))
		}
	}()

	stop := func() {
		_ = channel.Close()
		_ = conn.Close()
	}

	return stop, nil
}
