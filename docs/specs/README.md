# 宅影視 (Nerdia) 跨對話永久記憶庫 (`docs/specs/`)

本目錄為 Nerdia 專案所有 AI Agent（PM、UI/UX、資深 RD、QA）與人類 RD 的 **單一真實資訊來源 (Single Source of Truth, SSOT)**。
每當啟動一個新功能 (`/new-feature`) 或舊程式碼重構 (`/refactor`) 時，請複製 `_template/` 資料夾並命名為 `docs/specs/<feature-or-refactor-slug>/`。

## 標準目錄結構

```text
docs/specs/
├── README.md                  # 本說明文件
├── _template/                 # 標準規格書範本
│   ├── 01-prd.md              # [Gate 1] PM 產出：產品需求與驗收條件
│   ├── 02-ui-spec.md          # [Gate 1] UI/UX 產出：Android XML 切版標註規格與狀態表
│   ├── 03-tech-design.md      # [Gate 2] 資深 RD 產出：MVVM 架構圖、Kotlin 遷移指引與 Method 簽章
│   └── 04-qa-report.md        # [Gate 2 & 3] QA 產出：邊界測試計畫、JUnit 執行結果與 Code Review
└── <feature-slug>/            # 實際功能或重構的記憶庫（隨 feature/ 分支一起版控）
    ├── 01-prd.md
    ├── 02-ui-spec.md
    ├── prototype.html         # [Gate 1] UI/UX 產出：1:1 手機互動式 HTML 原型（可匯入 Figma）
    ├── 03-tech-design.md
    └── 04-qa-report.md
```
