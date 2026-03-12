# Nanobot - Simple Chat

A Python-based chatbot application with multi-channel support.

## Prerequisites

- Docker and Docker Compose
- Python 3.8+ (for local development)

## Quick Start


### 1. Build the Docker Image

```bash
docker compose build
```

### 2. Create Local State Directory

```bash
mkdir -p .nanobot
```

### 3. Initialize Configuration (Onboarding)

On first run, initialize the configuration:

```bash
docker compose --profile tools run --rm onboard
```

This generates the configuration skeleton in `.nanobot/` directory.

## Usage

### Running the Agent

Run a single agent execution with a message:

```bash
docker compose --profile tools run --rm agent
```

Or with a custom message:

```bash
docker compose run --rm nanobot agent -m "Hello"
```

### Starting Gateway

Start the gateway service (e.g., Telegram):

```bash
docker compose --profile runtime up gateway
```

### WhatsApp Setup (Linux Host Network Mode)

Terminal 1 - Login to channels:

```bash
docker compose --profile whatsapp run --rm channels-login
```

Terminal 2 - Start gateway:

```bash
docker compose --profile whatsapp run --rm gateway-host
```

## Directory Structure

```
.
├── .nanobot/                  # Local state directory (created on first run)
│   ├── config.json           # Main configuration
│   ├── workspace/             # Working directory
│   └── memory/                # State storage
├── .github/                   # GitHub workflows and configurations
├── docker/                    # Docker build files
│   └── Dockerfile            # Container image build configuration
├── docker-compose.yml        # Service definitions and profiles
├── README.md                 # This file
└── SECURITY.md               # Security practices and guidelines
```

## Configuration

Configuration files are automatically generated in `.nanobot/` during the onboarding process. Key files include:
- `config.json` - Main application configuration
- `workspace/` - Working directory for runtime operations
- `memory/` - Persistent state and memory storage

## Project Structure Details

### Docker Setup
The `docker/Dockerfile` is based on the official Nanobot Docker repository and includes:
- Python 3.12 runtime environment
- Node.js for bridge functionality
- Nanobot source code and dependencies
- Automated bridge setup on container startup

**Dockerfile Source**: https://raw.githubusercontent.com/ciri/nanobot-docker/refs/heads/main/Dockerfile

### Docker Compose Services
- **nanobot** - Main container with default status command
- **onboard** - One-time setup and configuration initialization
- **gateway** - Gateway service for channel integration (Telegram, WhatsApp, etc.)
- **agent** - Agent execution service for processing messages
- **channels-login** - WhatsApp/channel authentication service

## Development

### For Local Development
If you prefer to develop locally without Docker:

```bash
# Install Python dependencies
pip install -e .

# Run nanobot commands directly
nanobot status
nanobot onboard
nanobot agent -m "test message"
```

## Support

For issues or questions:
- Check the [Security](SECURITY.md) guidelines
- Review the Docker Compose configuration
- Refer to the [official Nanobot documentation](https://github.com/HKUDS/nanobot)