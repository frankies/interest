#!/usr/bin/env bash
set -euo pipefail

# init.sh - 初始化 SDKMan, 安装 JDK25 (GraalVM), 并设置 Maven 本地仓库为 /tmp/repo

echo "==> 初始化：备份并移除现有 ~/.sdkman（如果存在）"
if [ -d "$HOME/.sdkman" ]; then
  tar zcvf "$HOME/sdkman-backup_$(date +%F-%kh%M).tar.gz" -C "$HOME" .sdkman
  rm -rf "$HOME/.sdkman"
fi

echo "==> 安装 SDKMan 到 /tmp/sdkman"
export SDKMAN_DIR="/tmp/sdkman"
curl -s "https://get.sdkman.io" | bash

echo "==> 尝试加载 SDKMan 环境以使用 sdk 命令"
if [ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]; then
  # shellcheck source=/dev/null
  source "$SDKMAN_DIR/bin/sdkman-init.sh"
elif [ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]; then
  # shellcheck source=/dev/null
  source "$HOME/.sdkman/bin/sdkman-init.sh"
fi

echo "==> 写入 .sdkmanrc（用于批量安装）"
cat > ".sdkmanrc" <<'EOF'
java=25.0.2-graalce
jbang=0.136.0
EOF

echo "==> 使用 'sdk env install' 批量安装（优先）"
if command -v sdk >/dev/null 2>&1; then
  if ! sdk env install; then
    echo "sdk env install 失败，回退到单独安装"
    sdk install java 25.0.2-graalce || true
    sdk install jbang || true
  fi
else
  echo "sdk 命令不可用，跳过 sdk env install"
fi

echo "==> 设置 Maven 本地仓库为 /tmp/repo"
mkdir -p "$HOME/.m2"
cat > "$HOME/.m2/settings.xml" <<'EOF'
<settings>
  <localRepository>/tmp/repo</localRepository>
</settings>
EOF

echo "==> 完成：SDKMan 安装于 $SDKMAN_DIR，Maven 本地仓库设置为 /tmp/repo"
