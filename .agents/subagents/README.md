# Nerdia Custom Subagents 註冊基準庫

由於 Antigravity 的 `define_subagent` 生命週期綁定於單一對話視窗（Session），本目錄永久保存了「宅影視 (Nerdia)」4 大專職 Subagent 的完整系統提示詞 (System Prompts) 與工具權限設定。

當主 Agent 在任何新對話中需要啟動「深度研究軌（多 Subagent 並行與交叉詰問）」時，請先讀取以下配置並呼叫 `define_subagent` 完成註冊：

| Subagent 名稱 (`name`) | 角色定位 | `enable_write_tools` | 基準設定檔 |
| :--- | :--- | :---: | :--- |
| **`pm_agent`** | 專職產品經理 (PM) | `true` (僅寫 `docs/specs/`) | [pm-agent.md](pm-agent.md) |
| **`uiux_agent`** | 專職 UI/UX 設計師 | `true` (僅寫 `docs/specs/` 與 HTML 原型) | [uiux-agent.md](uiux-agent.md) |
| **`sr_rd_agent`** | 資深 Android 首席架構師 | **`false` (嚴格唯讀沙盒)** | [sr-rd-agent.md](sr-rd-agent.md) |
| **`qa_agent`** | 專職 QA 測試架構師 | `true` (寫 `docs/specs/` 與 `app/src/test/`) | [qa-agent.md](qa-agent.md) |
