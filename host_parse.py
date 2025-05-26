# 假设 url.txt 每行一个 URL
import socket

def read_urls(file_path):
    with open(file_path, 'r') as f:
        return [line.strip() for line in f if line.strip()]

def get_ip(url):
    try:
        hostname = url.split('//')[-1].split('/')[0]
        return socket.gethostbyname(hostname)
    except Exception as e:
        return None

def main():
    urls = read_urls('url.txt')
    mapping = []
    for url in urls:
        ip = get_ip(url)
        if ip:
            hostname = url.split('//')[-1].split('/')[0]
            mapping.append(f"{ip}\t{hostname}")
    # 输出为 /etc/hosts 格式
    print("# /etc/hosts 格式映射")
    for line in mapping:
        print(line)

if __name__ == "__main__":
    main()