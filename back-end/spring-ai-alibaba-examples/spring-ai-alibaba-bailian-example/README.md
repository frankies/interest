# Spring AI Alibaba 集成示例

## Introduction
本示例项目展示了如何使用 Spring AI Alibaba 接入百炼平台知识库、接入百炼平台智能体以及上传文档并解析的功能。您可以直接使用或参考此项目，将 Spring AI Alibaba 集成到您的项目中。

## 快速开始

### 后端设置
1、[application.yml](src/main/resources/application.yml) 中配置对应模型api-key

2、若需要使用智能体，还需在[application-agent.yml](src/main/resources/application-agent.yml) 中配置对应app-id

2、启动：[BailianExampleApplication.java](src/main/java/com/alibaba/cloud/ai/example/bailian/BailianExampleApplication.java)

#### 上传文档并解析
使用步骤：

1.添加advisor DashScopeDocumentAnalysisAdvisor

2.传入resource，支持上传文档、本地文档、链接等方式，选择对应的Resource子类即可

3.注意：上传时SystemMessage会被替换为文件id，当前仅qwen-long模型支持上传文件

[qwen-long官方参考文档](https://help.aliyun.com/zh/model-studio/long-context-qwen-long?spm=a2c4g.11186623.help-menu-2400256.d_0_3_0.19ca535dY5cKIw&scm=20140722.H_2846146._.OR_help-T_cn~zh-V_1)



