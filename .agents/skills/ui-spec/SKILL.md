---
name: ui-spec
description: 喚醒 UI/UX Agent 產出「Antigravity 1:1 手機互動式 HTML 原型 (generative_ui) + Figma MCP/插件同步結構 + Android XML 切版標註規格書 (02-ui-spec.md)」。當人類 RD 需要畫面設計稿與 XML 切版規格時使用。
---

# 宅影視 (Nerdia) UI/UX 雙軌原型與 Android XML 規格生成 (`/ui-spec`)

本技能專為支援人類 RD **「看 UI/UX 產出的畫面，親手實作出 Android XML Layout」** 而設計，結合 **(1) Antigravity 1:1 互動式 HTML 原型** 與 **(2) Figma 同步工作流 + Android XML 詳細標註**。

---

## 執行步驟

### Step 1：讀取現有設計系統 (Design Tokens)
在設計任何新畫面或修改舊畫面時，必須先檢視：
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/themes.xml` / `styles.xml`
- 鄰近功能的 `app/src/main/res/layout/*.xml`
確保配色（深色影視主題）、字體大小、卡片圓角、圖示風格與既有 App 100% 融合。

### Step 2：產出 1:1 手機互動式 HTML 原型 (`prototype.html`)
1. 先呼叫 `lookup_skill` 讀取 `generative_ui` 技能指南。
2. 產出具備 **390x844px 手機外框比例** 的獨立 HTML 檔案：
   - 存放於 `docs/specs/<slug>/prototype.html`（隨 Git 版控保存）。
   - 同步寫入當前對話的 Artifact 目錄 (`<appDataDir>/brain/<conversation-id>/<slug>-prototype.html`) 並開啟 `UserFacing: true`，讓人類 RD 可直接在 Antigravity 右側預覽面板點擊互動！
3. **原型必備功能**：
   - 頂部或底部提供 **「狀態切換控制列 (State Switcher)」**：一鍵切換預覽 `1. 載入中 (Shimmer Skeleton)`、`2. 正常資料 (Normal Content)`、`3. 空狀態 (Empty State)`、`4. 錯誤/斷網 (Error State)`。
   - **懸浮標註模式 (Inspect Mode Toggle)**：提供一個切換開關，開啟時在畫面上直接顯示關鍵元件的 `dp` 間距、`sp` 字級與建議的 `android:id`！

### Step 3：Figma 整合與同步（雙向支援）
1. **若環境已設定 Figma MCP / Talk-to-Figma Plugin**：
   - 可透過 MCP 工具讀取人類 RD 提供的 Figma URL 節點資訊，或透過 WebSocket 插件直接在 Figma 畫布建立對應 Frame。
2. **HTML 一鍵轉入 Figma (`html.to.design`)**：
   - `prototype.html` 採用乾淨的語意化 Flexbox/Grid 結構，人類 RD 可隨時在 Figma 使用免費插件 `html.to.design` 將 `prototype.html` 直接轉換為可編輯的 Figma 原生圖層。

### Step 4：撰寫 Android XML 切版標註規格書 (`docs/specs/<slug>/02-ui-spec.md`)
規格書必須包含：
1. **目標 Layout 檔名建議**（如 `fragment_xxx.xml`, `item_xxx.xml`, `bottom_sheet_xxx.xml`）。
2. **完整 View Tree 階層結構樹**（標明每個節點的 Android 元件類別，如 `androidx.constraintlayout.widget.ConstraintLayout`、`com.facebook.shimmer.ShimmerFrameLayout`、`androidx.recyclerview.widget.RecyclerView`）。
3. **元件屬性與 ViewBinding ID 對照表**：
   - `android:id` | 元件型別 | 寬高 (`match_parent`/`wrap_content`/`0dp`) | Constraint 約束關係 | 間距 (`margin`/`padding` dp) | 樣式/色碼 (`sp`, `@color/...`)
4. **四大狀態切換邏輯表**：明確定義當 ViewModel 處於 Loading / Success / Empty / Error 時，哪些 View 的 `visibility` 為 `VISIBLE` 或 `GONE`。

> ⚠️ **權限紅線**：絕不直接替人類 RD 建立或修改 `app/src/main/res/layout/*.xml`，將 XML 實作留給人類 RD！
