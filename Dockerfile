# Android SDK 基础镜像，后续 Node/Cordova/CLI 镜像继承此层
FROM node:20-slim  AS base

ARG ANDROID_PLATFORM=android-35
ARG ANDROID_BUILD_TOOLS=35.0.0
ARG ANDROID_CMDLINE_ZIP=commandlinetools-linux-11076708_latest.zip
ENV ANDROID_SDK_ROOT="/opt/android-sdk"
ENV PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools"
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    unzip \
    wget \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

RUN set -eux; \
    mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools"; \
    cd "$ANDROID_SDK_ROOT/cmdline-tools"; \
    wget -q  "https://dl.google.com/android/repository/${ANDROID_CMDLINE_ZIP}" -O tools.zip; \
    unzip -q tools.zip; \
    mv cmdline-tools latest; \
    rm tools.zip; \
    yes | sdkmanager --licenses >/dev/null; \
    sdkmanager "platform-tools" "platforms;${ANDROID_PLATFORM}" "build-tools;${ANDROID_BUILD_TOOLS}"; \
    rm -rf $ANDROID_SDK_ROOT/.android \
           $ANDROID_SDK_ROOT/.cache \
           $ANDROID_SDK_ROOT/cmdline-tools/latest/lib/package.xml \
           $ANDROID_SDK_ROOT/cmdline-tools/latest/lib/monitor-x86_64/*; \
    rm -rf /root/.android/avd || true


## 基础镜像：使用较小的 Debian slim 版 Node 20（Alpine 不适合安装 Android SDK）
FROM node:20-slim AS cordova-android-build

#LABEL maintainer="YourCompany DevOps <devops@yourcompany.com>"
#LABEL description="Cordova + Android SDK 35 + JDK17 Build Image for GitLab CI"

## ---- 可选代理参数（构建时传入可减少外网阻塞） ----
ARG http_proxy
ARG https_proxy
ARG no_proxy

# 设置构建时代理环境变量
ENV http_proxy=${http_proxy}
ENV https_proxy=${https_proxy}
ENV no_proxy=${no_proxy}

# 环境变量
ENV DEBIAN_FRONTEND=noninteractive
ENV ANDROID_SDK_ROOT="/opt/android-sdk"
ENV PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools"

## 基础镜像已包含 ANDROID SDK，无需重复参数化
COPY --from=base $ANDROID_SDK_ROOT $ANDROID_SDK_ROOT

## 如需额外工具（如 gradle、npm、yarn、cordova），可在此处安装
RUN apt-get update && apt-get install -y --no-install-recommends \
    gradle \ 
    openjdk-17-jdk \
    && rm -rf /var/lib/apt/lists/*

## Android SDK 已在基础镜像中，无需重复安装

## 可选：配置私服（如不需要可在构建参数里不传）
ARG NPM_REGISTRY=""
RUN if [ -n "$NPM_REGISTRY" ]; then npm config set registry "$NPM_REGISTRY"; fi
## 按需在 CI 步骤执行： npm install -g cordova  或其他 CLI

# 清理无用文件以减小体积
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /root/.cache

WORKDIR /workspace

## 默认进入 shell；CI 中通常通过命令覆盖
CMD ["bash"]
