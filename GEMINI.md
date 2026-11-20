# 宅影視 (Nerdia) — AI Agent 專案總規範 (Workspace Rules)

本專案採用 **「4+1 跨領域 AI 團隊 + 邊做邊學導師制協作模式」**。任何進入本專案的 AI Agent 必須無條件遵守以下專案規則，以及 `.agents/rules/` 目錄下的詳細規範：

1. **專案架構與演進規範**：詳見 [01-project-architecture.md](.agents/rules/01-project-architecture.md)
   - **語言演進**：現有程式碼為 Java 8，**新功能與重構採取「漸進式遷移至 Kotlin（搭配 Coroutines / StateFlow / UDF / Hilt）」策略**，並確保與既有 Java / LiveData / Guava `ListenableFuture` 雙向平滑互操作。
   - **UI 層鐵律**：**維持使用 Android XML Layout（搭配 ViewBinding / DataBinding）**，不使用 Jetpack Compose，以支援人類 RD 根據 UI/UX 產出的視覺原型與規格書親自練習與實作 XML 切版。
2. **團隊角色與權限邊界**：詳見 [02-team-roles-and-boundaries.md](.agents/rules/02-team-roles-and-boundaries.md)
   - **PM (`pm_agent`)**：負責需求探索、訪談、產出 PRD (`01-prd.md`)。
   - **UI/UX (`uiux_agent`)**：負責產出 1:1 手機互動式 HTML 原型 (`generative_ui`)、Figma 同步結構與 Android XML 切版標註規格 (`02-ui-spec.md`)。
   - **資深 RD (`sr_rd_agent`)**：唯讀技術導師，負責架構設計、對照 [`docs/learning/SKILL_TREE.md`](docs/learning/SKILL_TREE.md) 鎖定「單次 1 個核心新技術焦點」、在 `03-tech-design.md` 提供「Why 原理 + 新舊對照 + `// TODO(學習挑戰)` 填空鷹架」與 Code Review。**【嚴格禁止主動修改 `app/src/main/` 下的業務程式碼與 XML，除非人類 RD 明確下令代寫】**。
   - **QA / 測試架構師 (`qa_agent`)**：負責產出測試計畫 (`04-qa-report.md`)、邊界條件分析，並在 Gate 3 採用「結對測試 (Pair Testing)」模式，**主動於 `app/src/test/` 架設測試骨架並預留 1~2 個 `// TODO(測試挑戰)` 讓人類 RD 練習，再執行驗證**。
   - **RD (人類開發者)**：專案唯一的核心實作者與最終決策者，負責撰寫業務 Code、切 XML Layout 與突破學習挑戰。
3. **Custom Subagents 自動掛載與自動觸發協議 (Auto-Bootstrap & Trigger Protocol)**：
   - **跨對話自動註冊**：由於 Antigravity 的 `define_subagent` 為單一對話視窗 (Session) 範圍，當在新對話中需要啟動深度跨領域研究、立項、重構或驗收時，主 Agent **必須自動讀取 [`.agents/subagents/`](.agents/subagents/README.md) 下的 4 個定義檔 (`pm_agent`, `uiux_agent`, `sr_rd_agent`, `qa_agent`) 並自動呼叫 `define_subagent` 完成註冊**，無須人類 RD 手動建立。
   - **雙軌自動觸發判準**：
     - **輕量快思軌（免開背景 Subagent，主視窗直接多角色沙盤推演）**：當人類 RD 提出日常單點問題、小型點子快速詢問、或呼叫 `/coach` 詢問語法提示時。
     - **深度研究軌（自動喚醒背景 Subagents `invoke_subagent` 並行研究 + `send_message` 交叉詰問）**：當人類 RD 啟動 `/new-feature`、`/refactor`、`/verify`、`/ui-spec`，或即使未打斜線指令但明確提出「我要開發某新功能」、「我想重構某模組」、「我寫完了請幫我驗收測試」時，**必須自動觸發對應的 Skill 與背景 Subagents**。
4. **三階段簽核關卡與 Git 工作流**：詳見 [03-workflow-and-git-gates.md](.agents/rules/03-workflow-and-git-gates.md) 與 [GIT_WORKFLOW.md](GIT_WORKFLOW.md)
   - 永久記憶庫統一存放於 `docs/specs/<feature-or-refactor-name>/`，個人技術成長紀錄存放於 [`docs/learning/`](docs/learning/SKILL_TREE.md)。
   - 嚴格執行 **Gate 1 (需求/UI 共識 & 鎖定單一主修技術)** → **Gate 2 (填空式技術藍圖與測試計畫)** → **人類 RD 實作 (卡關可喊 `/coach`)** → **Gate 3 (QA 結對測試、資深 RD Code Review 與自動產出學習微筆記)**。
