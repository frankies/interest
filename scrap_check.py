import requests
from bs4 import BeautifulSoup
import re

# GitHub 页面 URL
url = "https://github.com/feiniaoyun"

def extract_xyz_links(url):
    try:
        # 发送 HTTP 请求获取页面内容
        response = requests.get(url)
        response.raise_for_status()  # 检查请求是否成功

        # 使用 BeautifulSoup 解析 HTML
        soup = BeautifulSoup(response.text, 'html.parser')

        # 查找所有的链接标签
        links = soup.find_all('a', href=True)

        # 使用正则表达式筛选以 .xyz 结尾的链接
        xyz_links = [link['href'] for link in links if re.search(r'\.xyz$', link['href'])]

        return xyz_links

    except requests.exceptions.RequestException as e:
        print(f"请求失败: {e}")
        return []

def check_links_status(links):
    for link in links:
        try:
            # 发送请求并设置超时时间为 5 秒
            response = requests.get(link, timeout=5)
            print(f"✅链接: {link}, 状态码: {response.status_code}")
        except requests.exceptions.RequestException as e:
            print(f"链接: {link}, 请求失败: {e}")

def main():
    # 提取 .xyz 链接
    xyz_links = extract_xyz_links(url)
    print("提取到的 .xyz 链接：")
    for link in xyz_links:
        print(link)
    
    # 检查链接状态
    print("\n开始检查链接状态：")
    check_links_status(xyz_links)

if __name__ == "__main__":
    main()