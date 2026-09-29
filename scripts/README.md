# scripts

| 目录 | 用途 |
|------|------|
| `dev/` | 本地验证，命令与 CI 对齐（可选） |

## 仓库布局

- `projects/plugins/intellij-fluent` — Fluent Marketplace 插件
- `projects/packages` — 唯一共享库（代码 only，无 `resources`，工程名 `:packages`）
- `projects/designs` — 品牌/设计素材（与 packages、plugins 平级）

## 本地 CI

```bash
./gradlew ciVerify
```

## Run vs Build

| 任务 | 行为 |
|------|------|
| `./gradlew runIde` | 一次拉起 IDE，加载全部 `:plugins/*`（共用 sandbox） |
| `./gradlew buildPlugins` | 分别打出每个插件自己的 zip，互不合并 |
| `:plugins:intellij-*:publishPlugin` | 各自上传与审核 |
