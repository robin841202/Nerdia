---
name: verify
description: 啟動 Gate 3「開發後 QA 結對測試 (Pair Testing)、資深 RD git diff Code Review、自動更新 SKILL_TREE.md 與產出實戰學習微筆記及雙語 Commit Message」。當人類 RD 完成程式碼與 XML Layout 實作後呼叫。
---

# Gate 3：QA 結對測試、聯合驗收與知識沉澱 (`/verify`)

當人類 RD 完成新功能或重構的業務邏輯與 XML Layout 後，執行本技能進行 **「結對測試 (Pair Testing)」**、全方位品質把關與個人技術資產歸檔。

---

## 執行步驟（兩階段互動式驗收）

### 階段 3A：檢視變更 & QA 架設「結對測試骨架 (`// TODO(測試挑戰)`)」
1. **檢視變更範圍 (`git status` & `git diff`)**：
   - 檢視人類 RD 修改與新增的所有 `.java`、`.kt` 與 `.xml` 檔案，確認 `03-tech-design.md` 中的 `// TODO(學習挑戰)` 是否已順利實作。
2. **🏗️ 資深 RD + UI/UX + PM 初步 `git diff` Code Review**：
   - **資深 RD (`sr_rd_agent`)**：針對人類 RD 親手實作的新技術程式碼給予具體回饋（哪些寫得很道地、哪些邊界或 Lifecycle 細節可以更優雅）。
   - **UI/UX (`uiux_agent`) & PM (`pm_agent`)**：核對 XML 狀態完整性與 PRD 驗收條件。
3. **🛡️ QA Agent 建立結對測試檔 (`Pair Testing`)**：
   - `qa_agent` 在 `app/src/test/java/com/robinhsueh/nerdia/` 下建立對應的 JUnit 測試類別：
     - 自動幫人類 RD 寫好繁瑣的測試環境配置（如 `@Rule`、`TestDispatcher`、Mock 物件宣告、假資料建構）以及大多數邊界測試案例。
     - **刻意在測試檔中留下 1～2 個核心測試方法，標註 `// TODO(測試挑戰): 請嘗試使用 MockK coEvery / Turbine 驗證...`**，並在註解中給出提示步驟！
   - 向人類 RD 展示新建的測試檔連結，詢問：**「我已經幫您架好測試環境並完成了多數邊界測試，在 `[TestFileName]` 中留了 1 個 `// TODO(測試挑戰)` 給您練手！您想現在親手補完再讓我跑測試批改（卡關可喊 `/coach`），還是這次先由我直接幫您補齊並執行測試？」**

---

### 階段 3B：執行測試批改、更新技能樹與產出《實戰學習微筆記》
當人類 RD 補完 `// TODO(測試挑戰)`（或選擇由 QA 直接補齊）後：
1. **執行自動化測試**：
   - 執行 `./gradlew testDebugUnitTest` 驗證所有單元測試是否綠燈通過，並批改人類 RD 撰寫的測試斷言。
2. **更新驗收報告 (`docs/specs/<slug>/04-qa-report.md` Part B)**。
3. **🎓 自動沉澱個人技術資產（知識閉環）**：
   - **更新技能樹**：將 [`docs/learning/SKILL_TREE.md`](../../../docs/learning/SKILL_TREE.md) 中本次修練的技術節點更新為 `✅ 已掌握`，並加上實戰筆記連結。
   - **產出實戰微筆記**：依據 [`docs/learning/_template-note.md`](../../../docs/learning/_template-note.md)，在 `docs/learning/notes/<節點編號-名稱>.md` 自動生成一頁式微筆記，收錄：
     - 本次在 Nerdia 專案中 **您親手寫出的 Before vs. After 關鍵程式碼對照**
     - **核心底層原理與踩坑紀錄**
     - **2 題資深 Android 面試高頻自測題與解答**
4. **產出雙語 Commit Message**：
   - 依據 [GIT_WORKFLOW.md](../../../GIT_WORKFLOW.md) 規範，自動產出可直接複製的中英雙語 Commit Message！
