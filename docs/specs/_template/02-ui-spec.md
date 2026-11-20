# [02-UI-Spec] <功能名稱> — 視覺原型與 Android XML 切版規格書

- **互動式 HTML 原型檔案**：`prototype.html`（可於 Antigravity 預覽面板直接互動，或透過 Figma `html.to.design` 插件匯入 Figma）
- **Figma 連結（選填）**：
- **目標 Android XML 檔案**：`app/src/main/res/layout/<layout_name>.xml`

---

## 1. 操作動線與頁面轉場 (Navigation & User Flow)
- **入口位置**：從哪個 Activity / Fragment / BottomSheet 進入？
- **Navigation Action / Intent 參數**：

## 2. Android XML View Tree 結構與屬性標註 (供人類 RD 切版)

```text
ConstraintLayout (root, match_parent x match_parent, bg: @color/...)
 ├── ShimmerFrameLayout (@+id/shimmer_layout)
 ├── RecyclerView (@+id/rv_items, 0dp x 0dp)
 └── LinearLayout (@+id/layout_empty_state, visibility="gone")
```

### 元件詳細規格對照表
| 建議 `android:id` | Android 元件型別 | 寬 x 高 (`dp`) | Constraint 約束條件 | 間距 (`margin` / `padding`) | 文字與色彩 (`sp` / `@color`) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `@+id/...` | `TextView` | `0dp x wrap_content` | `topToTop="parent"`, `startToStart="parent"` | `marginHorizontal="16dp"` | `16sp`, `@color/white`, `bold` |

## 3. 四大頁面狀態切換規格 (UI State Matrix)
| 狀態 (State) | Shimmer (`@+id/...`) | 主內容 (`@+id/...`) | 空狀態 (`@+id/...`) | 錯誤提示 (`@+id/...`) | 互動行為說明 |
| :--- | :---: | :---: | :---: | :---: | :--- |
| **1. 載入中 (Loading)** | `VISIBLE` (startShimmer) | `GONE` | `GONE` | `GONE` | 顯示骨架屏動畫 |
| **2. 正常顯示 (Success)** | `GONE` (stopShimmer) | `VISIBLE` | `GONE` | `GONE` | 渲染資料列表 |
| **3. 空資料 (Empty)** | `GONE` | `GONE` | `VISIBLE` | `GONE` | 顯示空狀態圖示與提示詞 |
| **4. 錯誤/斷網 (Error)** | `GONE` | `GONE` | `GONE` | `VISIBLE` | 顯示重試按鈕或 Snackbar |

## 4. 新增資源建議 (`res/values/`)
- **`strings.xml`**：
- **`colors.xml` / `dimens.xml`**：
- **`drawable/`**：
