---
name: new-feature
description: 啟動宅影視 (Nerdia)「新需求討論、定案、開發、驗證（含邊做邊學導師制）」標準三階段關卡工作流。當使用者想規劃或開發新功能時使用。
---

# 宅影視 (Nerdia) 新需求開發與導師制學習工作流 (`/new-feature`)

本技能用於執行宅影視專案的**「1. 新需求討論、定案、開發、驗證（結合邊做邊學機制）」**標準作業流程。請嚴格遵守三階段簽核關卡（Stage Gates），**未獲得人類 RD 簽核前，不得自行跨越關卡或修改 `app/src/main/` 業務程式碼**。

---

## 準備動作：確認 Subagents 已註冊
若當前對話尚未定義 `pm_agent`, `uiux_agent`, `sr_rd_agent`, `qa_agent`，請先讀取 `.agents/subagents/` 下的定義檔並透過 `define_subagent` 註冊（注意：`sr_rd_agent` 必須維持 `enable_write_tools: false` 唯讀沙盒）。

---

## 🟢 Gate 1：跨領域圓桌討論 ➔ 需求、UI 與「單一主修新技術」定案

1. **啟動跨領域圓桌研究（深度研究軌）**：
   - 先讀取 [`docs/learning/SKILL_TREE.md`](../../../docs/learning/SKILL_TREE.md) 了解人類 RD 目前的技能樹進度。
   - 使用 `invoke_subagent`（`Workspace: "inherit"`, `Model: "inherit"`）平行派發任務給：
     - **`pm_agent`**：分析使用者痛點、User Stories、MVP 範圍，並檢查 `app/src/main/java/com/robinhsueh/nerdia/model/` 中 TMDB / YouTube / OMDb API 的資料支援度。
     - **`uiux_agent`**：檢視現有 `res/layout/` 與 `res/values/`，評估新畫面的入口、操作動線與四大狀態（Shimmer Loading / Content / Empty / Error）。
     - **`sr_rd_agent`**：掃描現有 Java MVVM 架構，並**從 `SKILL_TREE.md` 中挑選 1～2 個最適合在此功能順便修練的現代 Android 技術節點候選**（遵守「單次任務限定 1 個核心新技術焦點 `1-New-Concept Budget`」原則）。
     - **`qa_agent`**：提出此功能可能遭遇的極端邊界情境（斷網、未登入 TMDB、API 空資料等）。
2. **交叉詰問與收斂**：
   - 主 Agent 統整四方觀點，標出「跨領域交織火花 / 衝突權衡點」。
   - 使用 `ask_question` 工具向人類 RD 進行**需求與學習目標訪談**（包含拍板本次唯一主修的 1 個新技術節點）。
3. **產出 Gate 1 規格與原型**：
   - 對齊後，在 `docs/specs/<feature-slug>/` 建立：
     - `01-prd.md`：完整 PRD、驗收條件，以及**本次鎖定主修的 `SKILL_TREE.md` 技術節點**。
     - `02-ui-spec.md`：Android XML View Tree 階層、`android:id` 命名建議、`dp`/`sp`/`@color` 標註、Constraint 約束與狀態切換規格。
     - `prototype.html`：遵循 `generative_ui` 技能，產出 1:1 手機比例、可切換狀態的互動式 HTML 原型（同時寫入 Artifact 目錄以供即時預覽，並支援匯入 Figma）。
   - 將 `docs/learning/SKILL_TREE.md` 中該技術節點狀態標記為 `🔥 本次修練中`。
4. **🛑 停駐等待簽核 (Checkpoint 1)**：
   - 向人類 RD 展示 `01-prd.md`、`02-ui-spec.md` 與互動原型，確認通過後才進入 Gate 2。

---

## 🟡 Gate 2：Git 分支準備 ➔ 「Why + 對比範例 + 填空題式鷹架」與測試計畫

當人類 RD 簽核通過 Gate 1 後執行：

1. **Git Flow 分支檢核**：
   - 依據 [GIT_WORKFLOW.md](../../../GIT_WORKFLOW.md)，確認工作區位於最新的 `develop` 分支，並建立功能分支 `git checkout -b feature/<feature-slug>`。
2. **資深 RD 產出導師制技術設計書 (`docs/specs/<feature-slug>/03-tech-design.md`)**：
   - 內容必須包含：
     - **MVVM 架構與資料流圖 (Mermaid)**
     - **🎓 本次主修新技術深度講解（Why 原理 + 舊 Java vs. 新 Kotlin 對比範例）**
     - **檔案新增/修改清單、類別/Method 簽章與 XML ViewBinding 對照表**
     - **🧩 填空題式實作鷹架 (`// TODO(學習挑戰)`)**：針對本次主修的新技術核心邏輯，提供清晰的結構骨架與 `// TODO(學習挑戰): 請嘗試使用...` 思考提示，**絕不直接給出完整解答剝奪人類 RD 的實作思考機會**！
   - ⚠️ **紅線檢查**：絕對不可修改 `app/src/main/` 下的任何 `.java`、`.kt` 或 `.xml` 檔案！
3. **QA 產出測試計畫 (`docs/specs/<feature-slug>/04-qa-report.md` Part A)**。
4. **🛑 交棒給人類 RD 實作 (Handoff to Human RD)**：
   - 附上 `02-ui-spec.md` 與 `03-tech-design.md` 連結，提醒人類 RD：「實作 `// TODO(學習挑戰)` 過程中若卡關，可隨時輸入 **`/coach`** 獲得三段式漸進提示；完成後請輸入 **`/verify`** 進入 Gate 3 結對測試與驗收！」

---

## 🔵 Gate 3：QA 結對測試 ➔ 三方聯合驗收與知識微筆記沉澱

當人類 RD 完成開發並呼叫 `/verify` 時，執行 `verify` 技能完成結對測試、Code Review 與學習筆記歸檔。
