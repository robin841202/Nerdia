# 宅影視 (Nerdia) — 個人技術成長與實戰筆記庫 (`docs/learning/`)

本目錄用於沉澱您在開發與重構「宅影視 (Nerdia)」過程中，親手實作並掌握的現代 Android 技術知識。

## 核心機制
1. **技能樹導航 ([`SKILL_TREE.md`](SKILL_TREE.md))**：
   - 在每次啟動 `/new-feature` 或 `/refactor` 的 **Gate 1** 時，資深 RD 會與您一起從 [`SKILL_TREE.md`](SKILL_TREE.md) 挑選 **1 個核心新技術焦點（1-New-Concept Budget）**。
2. **Gate 2「填空題式鷹架 (`// TODO(學習挑戰)`)」**：
   - 資深 RD 不會直接把完整解答貼給您複製貼上，而是在 `03-tech-design.md` 中講解 **Why（底層原理）** 與 **Before vs. After 對比**，並設計思考提示讓您自己動手突破。
   - 若實作途中卡關，隨時輸入 **`/coach`**，資深 RD 會採用「三段式漸進提示」引導您，而不會直接暴雷。
3. **Gate 3「QA 結對測試 (`// TODO(測試挑戰)`)」與微筆記自動歸檔**：
   - 當您完成業務 Code 呼叫 `/verify` 時，QA Agent 會建立測試檔並預留 1～2 個關鍵斷言挑戰給您補完。
   - 當測試全數通過後，AI 團隊會自動更新 [`SKILL_TREE.md`](SKILL_TREE.md) 的完成狀態，並將本次的實戰精華寫入 `docs/learning/notes/<编号-技能名稱>.md`。
