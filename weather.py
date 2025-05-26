from typing import Any
import httpx
from mcp.server.fastmcp import FastMCP

# 初始化FastMCP服务器
mcp = FastMCP("weather")

api_key = "f77481ccfa9e3311f14c3159548ead9c"  # 替换为你的OpenWeatherMap API Key

# 常量
NWS_API_BASE = "https://api.openweathermap.org/data/2.5/weather"
USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"

# 温度单位转换，将开尔文转化为摄氏度
def kelvin_to_celsius(kelvin: float) -> float:
 return kelvin - 273.15

async def get_weather_from_cityname(cityname: str) -> dict[str, Any] | None:
    """向openweathermap发送请求并进行适当的错误处理。"""
    headers = {
    "User-Agent": USER_AGENT,
    "Accept": "application/geo+json"
    }
    params = {
    "q": cityname,
    "appid": api_key
    }
    
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(NWS_API_BASE, headers=headers, params=params)
            response.raise_for_status()
            return response.json()
        except Exception:
          return None
 

async def get_weather_from_latitude_longitude(latitude: float, longitude: float) -> dict[str, Any] | None:
    """向openweathermap发送请求并进行适当的错误处理。"""
    headers = {
    "User-Agent": USER_AGENT,
    "Accept": "application/geo+json"
    }
    params = {
    "lat": latitude,
    "lon": longitude,
    "appid": api_key
    }
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(NWS_API_BASE, headers=headers, params=params)
            response.raise_for_status()
            return response.json()
        except Exception:
            return None

def format_alert(feature: dict) -> str:
    """将接口返回的天气信息进行格式化文本输出"""
    if feature["cod"] == 404:
     return"参数异常，请确认城市名称是否正确。"
    elif feature["cod"] == 401:
      return"API key 异常，请确认API key是否正确。"
    elif feature["cod"] == 200:
        return f"""
            City: {feature.get('name', 'Unknown')}
            Weather: {feature.get('weather', [{}])[0].get('description', 'Unknown')}
            Temperature: {kelvin_to_celsius(feature.get('main', {}).get('temp', 0)):.2f}°C
            Humidity: {feature.get('main', {}).get('humidity', 0)}%
            Wind Speed: {feature.get('wind', {}).get('speed', 0):.2f} m/s
            """
    else:
     return "未知错误，请稍后再试。"

@mcp.tool()
async def get_weather_from_cityname_tool(city: str) -> str:
    """Get weather information for a city.

    Args:
    city: City name (e.g., "wuhan"). For Chinese cities, please use pinyin
    """
    data = await get_weather_from_cityname(city)
    return format_alert(data)

@mcp.tool()
async def get_weather_from_latitude_longitude_tool(latitude: float, longitude: float) -> str:
    """Get weather information for a location.
    Args:
    latitude: Latitude of the location
    longitude: Longitude of the location
    """
    data = await get_weather_from_latitude_longitude(latitude, longitude)
    return format_alert(data)

if __name__ == "__main__":
    import sys
    if len(sys.argv) > 1:
        import asyncio
        city = sys.argv[1]
        result = asyncio.run(get_weather_from_cityname_tool(city))
        print(result)
    else:
        # 初始化并运行服务器
        mcp.run(transport='stdio')