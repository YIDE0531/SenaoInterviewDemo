# SenaoInterviewDemo

這是一個用於神腦國際 Android 面試的小型展示專案，使用 Jetpack Compose 製作商品清單頁面。

## App畫面
<img src="https://github.com/YIDE0531/SenaoInterviewDemo/blob/master/untitled.gif" width="300" height="700" />

## 🧱 專案架構

- `model`：資料模型與 UI 狀態定義  
- `repository`：商品資料來源
- `viewmodel`：透過 `StateFlow` 管理 UI 狀態  
- `ui`：使用 Jetpack Compose 建立畫面與互動  

## 🔧 使用技術

- **Kotlin + Jetpack Compose**
- **ViewModel + StateFlow**
- **Kotlinx Serialization**：解析 JSON
- **Coil**：圖片載入
- **Material 3**：UI 設計規範
- **Chucker**：除錯用網路封包攔截工具（debug 模式）
- **ktlint**：Kotlin 程式碼格式檢查
- **JUnit**：撰寫基礎單元測試

---

### 🔍 開發指令

```bash
./gradlew ktlintCheck          # 檢查格式
./gradlew ktlintFormat         # 執行Format
