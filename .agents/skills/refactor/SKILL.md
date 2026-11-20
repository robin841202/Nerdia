---
name: refactor
description: 啟動宅影視 (Nerdia)「重構 / 優化舊程式碼（含 Java 漸進遷移至 Kotlin 與邊做邊學導師制）」標準三階段工作流。當使用者想重構現有類別、解耦 MVVM/Repository、消除技術債或學習新架構時使用。
---

# 宅影視 (Nerdia) 舊程式碼重構與導師制學習工作流 (`/refactor`)

本技能用於執行宅影視專案的**「2. 重構/優化舊程式碼（結合邊做邊學機制）」**標準作業流程，讓每一次重構都成為解鎖 [`docs/learning/SKILL_TREE.md`](../../../docs/learning/SKILL_TREE.md) 現代 Android 技能的實戰修練場。

---

## 🟢 Gate 1：跨領域重構診斷 ➔ 範圍與「單一主修新技術」對齊

1. **現況掃描與技能樹媒合**：
   - 先讀取 [`docs/learning/SKILL_TREE.md`](../../../docs/learning/SKILL_TREE.md)。
   - **資深 RD (`sr_rd_agent`)**：深入讀取目標模組之 `.java` 與 `.xml` 檔案，診斷目前的技術債（如：ViewModel 直接 `new Repository()`、散落多個 `MutableLiveData`、Guava `ListenableFuture` 巢狀回呼、Adapter 全刷 `notifyDataSetChanged()` 等），並從 `SKILL_TREE.md` 推薦 **唯一 1 個本次重構主修的新技術焦點 (1-New-Concept Budget)**。
   - **QA (`qa_agent`)**：盤點該模組現有的功能行為基準（Behavior Baseline），確保重構過程「外部行為不變、不產生 Regression Bug」。
   - **UI/UX (`uiux_agent`) & PM (`pm_agent`)**：評估重構是否涉及 UI 狀態體驗改善。
2. **向人類 RD 提問對齊 (`ask_question`)**：
   - 確認重構範圍，並與人類 RD 拍板**本次唯一主修的 1 個新技術節點**（其他部分則維持熟悉寫法或平順過渡，避免一次改太多而卡死）。
3. **產出重構規格 (`docs/specs/refactor-<slug>/01-prd.md`)**，並將 `SKILL_TREE.md` 對應節點標記為 `🔥 本次修練中`。
4. **🛑 停駐等待簽核 (Checkpoint 1)**：確認重構範圍與學習目標後才進入 Gate 2。

---

## 🟡 Gate 2：分階段「填空題式重構藍圖」& 測試計畫

1. **Git Flow 分支檢核**：從最新的 `develop` 分支切出 `feature/refactor-<slug>`。
2. **資深 RD 產出導師制重構藍圖 (`docs/specs/refactor-<slug>/03-tech-design.md`)**：
   - **Before vs. After 架構對照圖 (Mermaid)**。
   - **🎓 本次主修新技術深度解析（Why 原理 + 舊寫法痛點 vs. 新寫法優勢）**。
   - **分步拆解指南與 `// TODO(學習挑戰)` 填空式鷹架**：
     - 將重構拆解為可小步編譯的安全步驟。
     - 針對本次主修的新技術核心區塊，提供介面簽章與 `// TODO(學習挑戰): [思考提示與建議使用的 API]`，不直接給出完整重構後程式碼，讓人類 RD 親手改寫體會！
   - ⚠️ **紅線檢查**：資深 RD **絕不主動修改 `app/src/main/` 下的程式碼**。
3. **QA 產出回歸測試計畫 (`docs/specs/refactor-<slug>/04-qa-report.md` Part A)**。
4. **🛑 交棒給人類 RD 實作**：提醒人類 RD 若在實作 `// TODO(學習挑戰)` 時卡關，隨時輸入 **`/coach`** 呼叫三段式漸進提示；完成後輸入 **`/verify`**。

---

## 🔵 Gate 3：QA 結對測試 & 知識微筆記沉澱

呼叫 `/verify` 技能，透過 QA 結對測試 (`// TODO(測試挑戰)`)、資深 RD Code Review，最終自動更新 `SKILL_TREE.md` 並產出本次重構的《實戰學習微筆記》與雙語 Commit Message。
