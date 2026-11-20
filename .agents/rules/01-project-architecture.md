# 01. 宅影視 (Nerdia) 技術架構與演進規範

## 1. 專案定位與核心 API
- **產品名稱**：宅影視 Nerdia (`com.robinhsueh.nerdia`)
- **核心價值**：提供影視探索（即將上映、熱映、熱門、分類探索）、演職員認識、觀看清單追蹤、TMDB 帳號同步、評分與影評閱讀、預告片觀看、查詢串流平台 (Watch Providers)。**注意：本 App 非盜版觀看影視用途**。
- **外部 API 依賴**：
  - **TMDB API v3**：核心影視資料、搜尋、帳戶同步與評分 (`TMDB_API_KEY`)
  - **YouTube API & IFrame Player**：預告與相關影音播放 (`YOUTUBE_API_KEY`)
  - **OMDb API**：補充影視評分資訊 (`OMDB_API_KEY`)

## 2. 技術棧現況與「Java → Kotlin 漸進式遷移」規範
- **現有基底**：
  - `compileSdk 35`, `minSdk 27`, `targetSdk 35`
  - 既有程式碼為 Java (`com.robinhsueh.nerdia.{model, view, viewmodel, utils}`)
  - 非同步與資料流：Retrofit 2 + Gson + Guava `ListenableFuture`、Room + Guava、Android Jetpack `ViewModel` + `LiveData`
  - 本地儲存：Room Database、`EncryptedSharedPreferences` (`SharedPreferenceUtils`)
- **漸進式遷移策略 (Java → Kotlin Roadmap)**：
  1. **新功能與重構優先指引 Kotlin**：資深 RD 在設計新模組或重構舊類別（Model / Repository / ViewModel / Fragment / Adapter）時，應主動提供 **Kotlin 慣用寫法 (Idiomatic Kotlin)** 設計，並附上「Java 舊寫法 vs. Kotlin 新寫法」的轉換說明，協助人類 RD 邊做邊學。
  2. **非同步與狀態流演進**：
     - 逐步導入 **Kotlin Coroutines (`suspend`)** 與 **`StateFlow` / `SharedFlow`**。
     - 在與既有 Java 類別互操作時，清楚標示如何透過 `@JvmStatic`、`@JvmOverloads`、`LiveData` 與 `Flow.asLiveData()` 或 Guava `ListenableFuture` 進行平滑銜接，避免一次性大規模破壞既有模組。
  3. **Null Safety 嚴格把關**：充分利用 Kotlin 可空型別 (`?`) 與安全呼叫，消除既有 Java 程式碼潛在的 `NullPointerException`。

## 3. UI 層開發鐵律（維持 XML Layout）
- **堅持使用 Android XML Layout**：
  - 本專案**不導入 Jetpack Compose**，所有畫面均使用 `app/src/main/res/layout/*.xml` 搭配 **ViewBinding**（為主）或既有 **DataBinding**。
  - **UI/UX Agent 產出要求**：所有 UI 規格書必須明確列出對應的 Android XML 容器結構（如 `ConstraintLayout`, `CoordinatorLayout`, `RecyclerView`, `NestedScrollView`, `ShimmerFrameLayout`）、建議的 `android:id` 命名、間距 (`dp`)、字級 (`sp`)、色彩資源 (`@color/...`) 以及 4 大狀態（Shimmer Loading、Content、Empty State、Error/Offline State）的顯示切換邏輯。
