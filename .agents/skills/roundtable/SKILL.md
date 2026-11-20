---
name: roundtable
description: 啟動宅影視 (Nerdia)「PM + UI/UX + 資深 RD + QA 跨領域知識交織圓桌會議」。支援「輕量同視窗多角色沙盤推演」與「深度多 Subagent 並行交叉詰問」，用於腦力激盪、評估新點子或解決複雜設計衝突。
---

# 跨領域知識交織圓桌會議 (`/roundtable`)

當人類 RD 提出一個新構想、架構疑問或體驗痛點，希望看到 **PM、UI/UX、資深 RD、QA** 四個領域專家交織碰撞出火花時，執行本技能。

---

## 運作模式：混合雙軌制 (Hybrid Tiered Roundtable)

請根據問題的規模自動選擇（或依使用者指定）以下軌道：

### 軌跡 A：輕量快思軌（同視窗多角色沙盤推演 — 預設用於日常快速討論）
在單一回應中，依序展現四方交織辯論：
1. **🎯 [PM 觀點]**：分析這個點子對影視迷的核心價值、使用頻率與 TMDB/YouTube/OMDb 資料可行性。
2. **🎨 [UI/UX 觀點]**：回應 PM 的構想，指出在手機畫面上的呈現方式、互動層級與 4 大狀態（Loading/Empty/Error/Content）設計。
3. **🏗️ [資深 RD 觀點]**：回應 PM 與 UI/UX，直接點出現有 Nerdia Codebase（Java MVVM / Room / Retrofit / XML ViewBinding）的實作成本、潛在效能或 API 限制，並提出「若改用某種折衷做法或 Kotlin 協程，開發成本可降低一半」的技術建議。
4. **🛡️ [QA 觀點]**：挑出上述討論中最容易爆 Bug 的邊界條件（斷網、空值、生命週期重建）。
5. **⚡ [圓桌總主持 — 交織共識與決策提問]**：總結三方/四方碰撞後的最佳推薦方案，並透過 `ask_question` 請人類 RD 做出決策。

---

### 軌跡 B：深度研究軌（多 Subagent 並行研究 + 交叉詰問 — 用於正式立項或複雜重構）
當議題需要翻閱大量既有程式碼、比對多個 Layout XML 與 API Model 時：
1. **第一輪（並行獨立研究）**：
   - 確認 `pm_agent`, `uiux_agent`, `sr_rd_agent`, `qa_agent` 已定義（若未定義則先從 `.agents/subagents/` 讀取並呼叫 `define_subagent`）。
   - 呼叫 `invoke_subagent` 同時派出四位 Agent 針對議題進行深度檔案檢索與分析。
2. **第二輪（跨領域交叉詰問 Cross-Examination）**：
   - 當四位 Subagent 回報初步結論後，若發現觀點衝突（例如 `sr_rd_agent` 發現某 API 欄位不存在，或 `uiux_agent` 的設計需要新增本地 Room 表），使用 `send_message` 將衝突點發送給相關 Subagent 進行第二輪修正。
3. **第三輪（收斂匯報與訪談）**：
   - 主 Agent 將交織討論後的精華整理為對照表，並透過 `ask_question` 向人類 RD 提問。
