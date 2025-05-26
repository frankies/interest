import requests
from concurrent.futures import ThreadPoolExecutor

def read_urls_from_file(file_path):
    """从文件中读取 URL 列表"""
    try:
        with open(file_path, 'r') as file:
            urls = [line.strip() for line in file if line.strip()]
        return urls
    except FileNotFoundError:
        print(f"文件 {file_path} 未找到！")
        return []

def check_url_status(url):
    """检查单个 URL 的状态"""
    try:
        # 发送请求并设置超时时间为 5 秒
        response = requests.get(url, timeout=5)
        if response.status_code == 200:
            return f"✅ 链接 {url} 可访问"
        else:
            return f"😢 链接 {url} 返回状态码: {response.status_code}"
    except requests.exceptions.RequestException as e:
        return f"😢 链接 {url}, 请求失败: {e}"

def check_urls_status_concurrently(urls):
    """并发检查 URL 列表的状态"""
    with ThreadPoolExecutor(max_workers=10) as executor:  # 设置最大线程数为 10
        results = executor.map(check_url_status, urls)
    for result in results:
        print(result)

def main():
    # 指定 URL 文件路径
    file_path = "url.txt"

    # 从文件中读取 URL 列表
    urls = read_urls_from_file(file_path)

    if not urls:
        print("未找到有效的 URL 列表，程序退出。")
        return

    # 并发检查 URL 状态
    print(f"开始检查链接状态：{len(urls)} 个链接")
    check_urls_status_concurrently(urls)

if __name__ == "__main__":
    main()