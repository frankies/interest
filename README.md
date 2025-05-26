# Weather Information CLI

A simple command-line weather information tool that provides current weather data for cities worldwide using the OpenWeatherMap API.

## Features

- Get real-time weather information for any city
- Display temperature in Celsius
- Show humidity and wind speed
- Support for both city names and geographical coordinates
- Simple command-line interface

## Prerequisites

- Python 3.7 or higher
- `httpx` library for HTTP requests
- `mcp.server.fastmcp` for MCP server functionality
- OpenWeatherMap API key

## Installation

1. Clone the repository
2. Install dependencies:
```bash
pip install httpx mcp-server
```

## Usage

### Command Line Interface

Get weather by city name:
```bash
python weather.py <city_name>
```

Example:
```bash
python weather.py shanghai
```

### As MCP Server

Run as an MCP server:
```bash
python weather.py
```

## Output Format

The weather information includes:
- City name
- Weather description
- Temperature in Celsius
- Humidity percentage
- Wind speed in meters per second

## Environment Variables

- `api_key`: Your OpenWeatherMap API key (currently hardcoded in the script)

## API Reference

This project uses the OpenWeatherMap API:
- Current Weather Data API: https://api.openweathermap.org/data/2.5/weather

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.