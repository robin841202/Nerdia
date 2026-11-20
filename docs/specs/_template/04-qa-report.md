# [04-QA-Report] <功能或重構名稱> — 測試計畫與聯合驗收報告

- **負責角色**：QA 測試架構師 (`qa_agent`) & 資深 RD (`sr_rd_agent`)

---

## Part A：Gate 2 測試案例與邊界條件清單 (開發前制定)

### 1. 核心與邊界測試案例 (Test Cases)
| 編號 | 測試類別 (`Happy` / `Edge` / `Negative`) | 前置條件與輸入 | 預期結果 (Expected Behavior) | Gate 3 實測結果 |
| :--- | :---: | :--- | :--- | :---: |
| **TC-01** | `Happy` | 網路正常、API 回傳有效清單 | 停止 Shimmer 並正確渲染 RecyclerView | ⏳ 待驗 |
| **TC-02** | `Edge` | API 回傳空陣列 `[]` 或欄位為 `null` | 不發生 NPE 閃退，正確顯示 Empty State | ⏳ 待驗 |
| **TC-03** | `Negative` | 網路中斷或 HTTP 401/500 錯誤 | 停止 Shimmer 並顯示錯誤提示 | ⏳ 待驗 |
| **TC-04** | `Edge` | 畫面載入中途切換 Tab 或旋轉螢幕 | ViewBinding / Observer 無記憶體洩漏或崩潰 | ⏳ 待驗 |

### 2. 預計撰寫之 JUnit 單元測試項目 (`app/src/test/`)
- [ ] `...Test.kt` / `...Test.java`：測試 ViewModel 狀態轉換與空值防護。

---

## Part B：Gate 3 開發後自動化測試與聯合 Code Review 結果 (`/verify` 填寫)

### 1. QA 自動化測試執行結果
- **新增/更新之測試檔案**：
- **執行指令與結果 (`./gradlew testDebugUnitTest`)**：

### 2. 資深 RD `git diff` Code Review 報告
- **架構與 Kotlin/Java 語法慣用度**：
- **潛在風險與優化建議（含檔案與行號連結）**：

### 3. 建議 Git Commit Message (符合 `GIT_WORKFLOW.md`)
```text
[中文簡述]

[Prefix]: [English Description]
```
