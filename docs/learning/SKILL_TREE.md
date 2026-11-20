# 宅影視 (Nerdia) — Android 現代化技術演進技能樹 (`SKILL_TREE.md`)

這份技能樹是依據「宅影視 (Nerdia)」現有原始碼結構量身打造的**個人實戰成長地圖**。
遵循 **「單次任務 1 個新技術預算 (1-New-Concept Budget)」** 原則：每次執行 `/new-feature` 或 `/refactor` 時，資深 RD 會與您在 **Gate 1** 挑選其中 **1 個技能節點** 作為本次主修目標，並在 **Gate 3** 驗收通過後將狀態更新為 `✅ 已掌握`，同時附上對應的實戰筆記連結！

---

## 📊 技能樹總覽與目前進度

狀態圖例：`⬜ 未解鎖/待挑戰` ｜ `🔥 本次修練中` ｜ `✅ 已掌握 (附實戰筆記)`

### 🌊 分支一：現代化資料流與狀態管理 (UDF & Coroutines / Flow)
> **專案痛點起點**：目前如 [`HomeViewModel.java`](../../app/src/main/java/com/robinhsueh/nerdia/viewmodel/HomeViewModel.java) 擁有 14 個獨立的 `MutableLiveData`，且直接在 ViewModel 中使用 Retrofit `Call.enqueue` 處理回呼，缺乏統一的 Loading / Error 狀態封裝。

| 等級 | 技能節點編號與名稱 | 核心學習目標 | 推薦練手目標 (Nerdia 模組) | 狀態 | 實戰筆記 |
| :---: | :--- | :--- | :--- | :---: | :--- |
| **Lv.1** | **A1. Kotlin Data Class 與 Null Safety 轉換** | 掌握 `?`、`?:`、`let`、`@SerializedName` 與 Immutability (`val`) | `model/movie/` 或 `model/user/` | ⬜ | - |
| **Lv.1** | **A2. Retrofit `suspend` 與 Coroutines 基礎 (`viewModelScope`)** | 將 `Call<T>.enqueue` 與 Guava `ListenableFuture` 升級為結構化併發 `suspend fun` | `OmdbRepository` / `PersonRepository` | ⬜ | - |
| **Lv.2** | **A3. 單向資料流 (UDF) 與 `Sealed Interface UiState`** | 用單一 `StateFlow<UiState>` 整合散落的 Loading / Success / Empty / Error 狀態 | `GenresListFragment` / `GenreResultsFragment` | ⬜ | - |
| **Lv.2** | **A4. `StateFlow` vs. `SharedFlow` 與 Lifecycle 安全收集** | 在 XML Fragment 中使用 `repeatOnLifecycle(STARTED)` 安全觀察狀態與一次性事件 (Snackbar/導航) | `LoginTmdbViewModel` / `ProfileFragment` | ⬜ | - |
| **Lv.3** | **A5. 進階 Flow 操作符 (`combine`, `flatMapLatest`, `debounce`, `catch`)** | 實作搜尋防抖 (Debounce) 或多 API 並行聚合 (`coroutineScope { async }`) | `SearchViewModel` / `HomeViewModel` | ⬜ | - |

---

### 🏛️ 分支二：架構解耦與依賴注入 (DI & Clean Architecture)
> **專案痛點起點**：目前 ViewModel 內部直接呼叫 `new MovieRepository()`（硬編碼耦合），導致無法替換假物件 (Fake/Mock) 進行單元測試，且 `init()` 方法需由外部手動呼叫。

| 等級 | 技能節點編號與名稱 | 核心學習目標 | 推薦練手目標 (Nerdia 模組) | 狀態 | 實戰筆記 |
| :---: | :--- | :--- | :--- | :---: | :--- |
| **Lv.1** | **B1. Constructor Injection 與 `ViewModelProvider.Factory`** | 移除 ViewModel 內的 `new Repository()` 與 `init()`，改由建構子注入依賴 | 任一小型 `ViewModel`（如 `SimilarTabViewModel`） | ⬜ | - |
| **Lv.1** | **B2. Repository 介面抽象化與統一 `Result<T>` 錯誤處理** | 定義 Repository Interface 並封裝 Network / Room 例外，不讓 Retrofit `Call` 洩漏到 ViewModel | `GenreRepository` / `WatchlistRepository` | ⬜ | - |
| **Lv.2** | **B3. 導入 Hilt 依賴注入框架 (`@HiltAndroidApp`, `@Inject`, `@Module`)** | 學會使用 Hilt 自動管理 Singleton (Retrofit/Room) 與 `@HiltViewModel` 生命週期 | 全域 Application + 單一 Feature 試點 | ⬜ | - |
| **Lv.3** | **B4. Clean Architecture 領域層 (`UseCase` / Interactor)** | 抽離複雜跨 Repository 商業邏輯（例如：檢查登入狀態 + 同步 TMDB 與本地 Room 清單） | `OperateMediaViewModel` / `Watchlist` 同步邏輯 | ⬜ | - |

---

### 🎨 分支三：現代化列表與進階 Android XML UI
> **專案痛點起點**：您堅持保留並精進 Android XML 切版能力；目前部分 Adapter 與複雜頁面可進一步升級為高效能差量更新與進階 Material 3 / 動態互動佈局。

| 等級 | 技能節點編號與名稱 | 核心學習目標 | 推薦練手目標 (Nerdia 模組) | 狀態 | 實戰筆記 |
| :---: | :--- | :--- | :--- | :---: | :--- |
| **Lv.1** | **C1. 100% ViewBinding 遷移與安全 Lifecycle 釋放** | 掌握 Fragment 中 ViewBinding 的正確建立與清理模式，消除殘留 `findViewById` | 既有 Fragment / Tab 頁面 | ⬜ | - |
| **Lv.1** | **C2. `ListAdapter` + `DiffUtil.ItemCallback` 高效列表** | 告別 `notifyDataSetChanged()` 全刷，利用背景執行緒計算差異並自動播放項目增刪動畫 | `CastsAdapter` / `ReviewAdapter` / `GenresAdapter` | ⬜ | - |
| **Lv.2** | **C3. Material 3 XML 設計系統與深色主題語意化 Token** | 運用 Material 3 `ShapeAppearance`、`ChipGroup`、`CoordinatorLayout` 摺疊效果 | 新功能畫面或 `MediaDetailsActivity` | ⬜ | - |
| **Lv.2** | **C4. `MotionLayout` 宣告式 XML 動畫與 Shared Element Transition** | 純靠 XML `MotionScene` 實作流暢的頂部海報收合或轉場動畫 | `MediaDetailsActivity` / `PersonDetailsActivity` | ⬜ | - |
| **Lv.3** | **C5. Jetpack Paging 3 無限滾動分頁架構 (`PagingSource` + `PagingDataAdapter`)** | 取代手動計算 `page++` 與 ScrollListener，優雅處理分頁載入與底部 Loading Footer | `VerticalBrowseFragment` / 搜尋結果列表 | ⬜ | - |

---

### 🛡️ 分支四：高可測性架構與現代化 Android 測試 (Testing Mastery)
> **學習模式**：在 **Gate 3** 由 QA Agent 幫您架設測試環境與多數案例，並固定留下 **1～2 個 `// TODO(測試挑戰)`** 讓您親手練習！

| 等級 | 技能節點編號與名稱 | 核心學習目標 | 推薦練手目標 (Nerdia 模組) | 狀態 | 實戰筆記 |
| :---: | :--- | :--- | :--- | :---: | :--- |
| **Lv.1** | **D1. JUnit 4/5 基礎與 Given-When-Then 測試結構** | 學會針對純邏輯類別、Mapper 與狀態判斷撰寫高可讀性單元測試 | Model / Utils / Mapper 類別 | ⬜ | - |
| **Lv.2** | **D2. MockK 模擬框架實戰 (`every`, `coEvery`, `verify`)** | 在 Kotlin 中輕鬆模擬 Repository 回傳成功、空列表或拋出網路例外 | 已重構為 Constructor Injection 的 ViewModel | ⬜ | - |
| **Lv.2** | **D3. Coroutines 非同步測試 (`runTest`, `StandardTestDispatcher`, `MainDispatcherRule`)** | 掌握虛擬時間控制，在幾毫秒內測試非同步延遲與 `viewModelScope` 協程 | 使用 Coroutines 的 ViewModel / Repository | ⬜ | - |
| **Lv.3** | **D4. Turbine 狀態流測試 (`stateFlow.test { awaitItem() }`)** | 精準斷言 `UiState` 從 `Loading` ➔ `Success` 或 `Error` 的每一次狀態發射序列 | 採用 UDF `StateFlow` 的 ViewModel | ⬜ | - |
