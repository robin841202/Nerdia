# [01-PRD] <功能或重構名稱>

- **狀態**：`Draft` | `Gate 1 Approved` | `Implemented` | `Verified`
- **對應分支**：`feature/<slug>`
- **🎓 本次鎖定主修新技術 (1-New-Concept Budget)**：`[<節點編號與名稱>]`（對應 [`docs/learning/SKILL_TREE.md`](../../learning/SKILL_TREE.md)）
- **負責角色**：PM Agent (`pm_agent`) & 人類 RD

---

## 1. 背景與問題定義 (Background & Problem Statement)
- **痛點 / 動機**：為什麼我們需要做這個新功能或重構？
- **目標受眾 / 使用情境**：使用者在什麼情境下會觸發此功能？

## 2. 跨領域圓桌決策紀錄 (Roundtable Decisions)
| 討論議題 | PM 觀點 | UI/UX 觀點 | 資深 RD 觀點 (含學習建議) | QA 觀點 | 最終拍板決策 (人類 RD) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 範例議題 | ... | ... | ... | ... | **採用方案 X** |

## 3. 使用者故事與功能範圍 (User Stories & Scope)
### In-Scope (本次實作範圍)
- [ ] **US-01**：身為 [某類使用者]，我想要 [執行某操作]，以便 [獲得某價值]。

### Out-of-Scope (本次暫不實作)
- 暫不處理...

## 4. 外部 API 與資料來源確認 (TMDB / YouTube / OMDb / Room)
- **使用的 Endpoint / 資料表**：
- **欄位限制與授權狀態（是否需登入 TMDB Session）**：

## 5. 驗收條件 (Acceptance Criteria - 供 Gate 3 核對)
- [ ] **AC-01 (正常流程)**：當...時，系統應顯示...
- [ ] **AC-02 (空狀態)**：當 API 回傳空清單時，應顯示...
- [ ] **AC-03 (異常/斷網狀態)**：當無網路連線時，應顯示...
