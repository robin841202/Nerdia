# Nerdia Git 工作流規範 (Git Workflow Guidelines)

這份文件紀錄了 Nerdia 專案的 Git 分支管理、提交規範與版本發布流程，供開發、修正 Bug 及發布版本時參考。技術架構與設定請參考 [DEVELOPMENT.md](DEVELOPMENT.md)。

## 1. 分支策略 (Branching Strategy)

本專案採用類似 **Git Flow** 的管理模式：

### 核心分支 (Core Branches)
*   **`master`**:
    *   **用途**: 存放穩定、已發布的程式碼。
    *   **規則**: 禁止直接在 `master` 提交代碼。所有變更必須透過 `develop` 分支合併進來。
    *   **標籤**: 每個 `master` 上的提交都應該打上版本標籤（如 `v1.2.1`）。
*   **`develop`**:
    *   **用途**: 主要開發集成分支。
    *   **規則**: 所有的 Feature 分支與 Hotfix 分支最終都會匯集於此。這是開發環境的最新狀態。

### 輔助分支 (Supporting Branches)
*   **`feature/ <功能名稱>`**:
    *   **起點**: `develop`
    *   **終點**: `develop`
    *   **用途**: 開發新功能（如 `feature/in-app_update`）。
    *   **命名範例**: `feature/login-ui`, `feature/streaming-api`
*   **`hotfix/ <版本號>`**:
    *   **起點**: `develop` (或從特定的 Tag 簽出)
    *   **終點**: `develop` (隨後合併至 `master`)
    *   **用途**: 緊急修復生產環境的 Bug。
    *   **命名範例**: `hotfix/1.2.2`

---

## 2. 提交規範 (Commit Message Convention)

為了維持歷史紀錄的可讀性，請遵循以下格式（中文簡述後請保留一個空行）：

**格式**:
```text
[中文簡述]

[Prefix]: [English Description]
```

### 常用 Prefix 意義：
*   **`feat:`**: 新增功能 (Feature)。
*   **`fix:`**: 修復 Bug。
*   **`docs:`**: 僅修改文件 (Documentation)。
*   **`style:`**: 修改程式碼格式（如空白、縮排、分號，不影響邏輯）。
*   **`refactor:`**: 重構程式碼（非功能新增也非 Bug 修復）。
*   **`perf:`**: 改善效能的程式碼變更 (Performance)。
*   **`test:`**: 新增或修改測試案例 (Testing)。
*   **`build:`**: 影響建置系統或外部依賴的變更（如 Gradle, Maven, AGP）。
*   **`ci:`**: 修改 CI 設定檔或腳本 (Continuous Integration)。
*   **`chore:`**: 雜務（更新版本號、修改 .gitignore 等不影響開發原始碼的變更）。
*   **`revert:`**: 還原之前的 Commit。
*   **`BREAKING CHANGE:`**: 重大變更（通常在 Prefix 後方加上 `!`，如 `feat!:`）。

### 範例：
*   範例 1：
    ```text
    首頁 > 新增 Netflix 電影分類

    feat: add Netflix movie category
    ```
*   範例 2：
    ```text
    詳細資料頁面 > 修正影視評分查看 bug

    fix: resolve null exception in RateDetails
    ```
*   範例 3：
    ```text
    升級專案建置環境

    build: upgrade Gradle to 8.5 for Java 21 support
    ```

---

## 3. 標準作業流程 (Workflows)

### A. 開發新功能 (Developing a Feature)
1.  切換至 `develop` 並更新：`git checkout develop && git pull`
2.  建立功能分支：`git checkout -b feature/my-new-feature`
3.  開發並提交 (Commit)。
4.  合併回 `develop`：
    ```bash
    git checkout develop
    git merge feature/my-new-feature
    git branch -d feature/my-new-feature
    ```

### B. 發布新版本 (Releasing a Version)
1.  在 `develop` 分支更新 `app/build.gradle` 中的 `versionName` 與 `versionCode`。
2.  提交變更：
    ```text
    更新版本號至 1.3.0

    chore: update app version to 1.3.0
    ```
3.  將 `develop` 合併至 `master`:
    ```bash
    git checkout master
    git merge develop
    ```
4.  打上版本標籤：`git tag -a v1.3.0 -m "Release v1.3.0"`
5.  推送至 GitHub：`git push origin master --tags`

### C. 緊急修復 Bug (Handling a Hotfix)
1.  從 `develop` 或 `master` (取決於 Bug 影響範圍) 建立 hotfix 分支：`git checkout -b hotfix/1.2.2`
2.  修復 Bug 並提交。
3.  合併回 `develop`:
    ```bash
    git checkout develop
    git merge hotfix/1.2.2
    ```
4.  (選配) 如果需要立即更新生產版本，重複上述「發布新版本」流程合併至 `master`。

---

## 4. 懶人包：當下我該怎麼做？

| 情境 | 動作 |
| :--- | :--- |
| **我要寫新功能** | 從 `develop` 開一個 `feature/xxx` 分支。 |
| **我要修一個小 Bug** | 如果不急，直接在 `develop` 修；如果急，開 `hotfix/xxx`。 |
| **我要準備發布了** | 在 `develop` 改版本號，合併進 `master` 並打 `tag`。 |
| **我要改 Gradle 設定** | 在 `develop` (或功能分支) 提交，Prefix 使用 `build:`。 |
| **我要改說明文件** | 在 `develop` (或功能分支) 提交，Prefix 使用 `docs:`。 |
