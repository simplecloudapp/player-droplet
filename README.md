# Player-Droplet

Player-Droplet is a microserver designed to manage player connections and provide an API for other droplets or plugins such as Spigot, Minestom, Velocity, and BungeeCord, with Kyori Adventure support and the ability to scale in Kubernetes.

## Getting Started

To get started with Player-Droplet, you can run it in Kubernetes or Docker using the provided image.

The server uses the following environment variables:

| Variable Name | Description | Default Value |
|---------------|-------------|---------------|
| `GRPC_PORT` | The port the gRPC server should listen on. | `5816` |
| `MONGO_CONNECTION_STRING` | The connection string for the MongoDB instance. | (required) |
| `MONGO_DATABASE` | The name of the MongoDB database to use. | `players` |
| `REDIS_HOST` | The hostname or IP address of the Redis instance. | `127.0.0.1` |
| `REDIS_PORT` | The port number for the Redis instance. | `6379` |
| `REDIS_PASSWORD` | The password for the Redis instance. | `null` |
| `RABBITMQ_HOST` | The hostname or IP address of the RabbitMQ instance. | `127.0.0.1` |
| `RABBITMQ_USERNAME` | The username to use when connecting to RabbitMQ. | `user` |
| `RABBITMQ_PASSWORD` | The password to use when connecting to RabbitMQ. | `bitnami` |

To use the API, you'll need to install the Player-Droplet plugin on your platform. The plugin uses the following environment variables:

| Variable Name | Description | Default Value |
|---------------|-------------|---------------|
| `PLAYER_DROPLET_HOST` | The hostname or IP address of the Player-Droplet server. | `127.0.0.1` |
| `PLAYER_DROPLET_PORT` | The port number for the gRPC server on the Player-Droplet server. | `5816` (should match `GRPC_PORT` on the server) |

## Contributing

If you're interested in contributing to Player-Droplet, please follow these steps:

1. Fork the repository.
2. Create a new branch: `git checkout -b my-new-feature`
3. Make your changes and commit them: `git commit -am 'feat: some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request.

## License

Player-Droplet is licensed under the MIT License. See the `[LICENSE](LICENSE)` file for more information.