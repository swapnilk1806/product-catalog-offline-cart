# Product Catalog & Offline Cart

## 🎥 Screen Recording

**Video Demo:**  
<!-- Add your Google Drive / YouTube Unlisted / other accessible screen recording link here -->

**Video Link:**


---

## 📱 Project Overview

This project is an Android application developed as part of the **Spire Lab Android Developer Practical Assessment**.

The application allows users to:

- Browse products fetched from the DummyJSON Products API
- Search for products
- View detailed product information
- Add products to a shopping cart
- Increase or decrease product quantities
- Remove products from the cart
- View total cart item count
- View total cart price
- Use the cart while offline
- Persist cart data after closing and reopening the application

---

## ✨ Features

### 1. Product Listing

The application fetches products from the DummyJSON Products API and displays:

- Product image
- Product name
- Product price
- Product rating

The application handles:

- Loading state
- Empty product results
- API/network errors
- Retry functionality

---

### 2. Product Search

Users can search for products using the search functionality.

Search results are updated according to the user's search query.

The application also handles:

- Empty search results
- Network/API errors
- Retry after an unsuccessful request

---

### 3. Product Details

Selecting a product opens a product details screen containing:

- Product image
- Product name
- Description
- Price
- Rating
- Category
- Brand
- Stock

The product details screen also provides an option to add the product to the cart.

The product image can be selected to view a larger version.

---

### 4. Shopping Cart

Users can:

- Add products to the cart
- Increase product quantity
- Decrease product quantity
- Remove products
- View all cart items
- View total number of items
- View total cart price

---

### 5. Offline Cart

The shopping cart is stored locally on the device.

The cart remains available when the device has no internet connection.

Users can still:

- Open the cart
- View cart items
- Increase quantity
- Decrease quantity
- Remove items
- View total item count
- View total price

Cart data also remains available after the application is closed and reopened.

---

## 🛠️ Technology Stack

| Technology | Usage |
|---|---|
| Kotlin | Application development |
| Android SDK | Android application platform |
| XML | User interface |
| Kotlin Coroutines | Asynchronous operations |
| REST API | Product data |
| DummyJSON | Product API |
| SQLite / Local Database | Offline cart persistence |
| Gradle | Build system |
| Git | Version control |
| GitHub | Source code repository |

---

## 🌐 API

The application uses the **DummyJSON Products API**.

API Documentation:

https://dummyjson.com/docs/products

Base URL:

https://dummyjson.com/

No API key or account is required.

The application uses the API for product-related operations such as:

- Fetching products
- Searching products
- Fetching product details
- Fetching product categories

---

## 🏗️ Architecture

The project is organized into separate components to keep the code readable and maintainable.

```text
app/
└── src/
    └── main/
        ├── java/
        │   └── com.example.productlist/
        │       ├── MainActivity.kt
        │       │
        │       ├── model/
        │       │   └── Product.kt
        │       │
        │       ├── api/
        │       │   └── ...
        │       │
        │       ├── database/
        │       │   └── ...
        │       │
        │       ├── ui/
        │       │   ├── ProductDetails.kt
        │       │   ├── ProductList.kt
        │       │   └── Cart.kt
        │       │
        │       └── utils/
        │           ├── ImageLoader.kt
        │           └── ...
        │
        └── res/
            ├── drawable/
            ├── mipmap/
            └── values/
```

The application separates:

- UI components
- Product models
- API/network operations
- Local database/cart operations
- Utility functions

This structure makes individual features easier to maintain and modify.

---

## 💾 Local Storage

The shopping cart is persisted locally on the Android device.

The locally stored cart contains the information required to restore the user's cart, including:

- Product information
- Product quantity

The local storage approach ensures that cart operations do not depend on an active internet connection.

Therefore, after the products have been added to the cart, the user can continue managing the cart while offline.

---

## 🔄 Application Flow

```text
                    ┌─────────────────┐
                    │   Open App      │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Product Listing │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
          Search         Product         Cart
              │           Details           │
              │              │              │
              │              ▼              │
              │         Add to Cart         │
              │              │              │
              └──────────────┼──────────────┘
                             ▼
                    ┌─────────────────┐
                    │  Local Storage  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Offline Cart    │
                    └─────────────────┘
```

---

## ⚠️ Error Handling

The application handles the following scenarios:

### No Internet Connection

The application detects network availability and provides an appropriate error state.

### API Request Failure

If the API request fails, the application displays an error state and provides a retry option.

### Request Timeout

Network requests use timeout handling so that the application does not remain stuck indefinitely.

### Empty Search Results

When no products match the search query, an appropriate empty-result message is displayed.

### Empty Product List

If the API returns no products, the application displays an appropriate empty state.

---

## 🎨 Design Decisions

### Simple User Interface

The application uses a simple product-store interface so that the main functionality is easy to understand.

### Product Details

Product information is separated into a dedicated details screen instead of displaying all information on the product listing.

### Local Cart

The cart is persisted locally because offline cart functionality is a core requirement of the assessment.

### Component Separation

The project is divided into smaller files/components instead of placing all functionality into a single large file.

This makes the code easier to read, test, and maintain.

### Image Loading

Product images are loaded asynchronously so that network operations do not block the main UI thread.

---

## 📦 Project Setup

### Requirements

Before running the application, install:

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle configuration
- An Android emulator or physical Android device

---

## 🚀 Build & Run

### 1. Clone the repository

```bash
git clone https://github.com/swapnilk1806/product-catalog-offline-cart.git
```

### 2. Open the project

Open the cloned project in Android Studio.

### 3. Sync Gradle

Allow Android Studio to synchronize the Gradle dependencies.

### 4. Run the application

Connect an Android device or start an Android emulator.

Then select:

```text
Run ▶
```

from Android Studio.

---

## 🔨 Build Using Command Line

From the project root:

### Windows

```bash
gradlew.bat assembleDebug
```

### Linux / macOS

```bash
./gradlew assembleDebug
```

The generated APK will be available inside the project's build output directory.

---

## 📂 GitHub Repository

Source code:

https://github.com/swapnilk1806/product-catalog-offline-cart

---

## 🧪 Assessment Demonstration

The screen recording demonstrates:

- Product listing
- Loading state
- Error/retry state
- Product search
- Product details
- Product image viewing
- Adding products to cart
- Increasing quantity
- Decreasing quantity
- Removing products
- Cart item count
- Cart total price
- Offline cart functionality
- Closing and reopening the application
- Cart persistence after reopening

---

## ⚠️ Known Limitations

- Product data depends on the availability of the DummyJSON API when fetching products.
- New product information cannot be retrieved while completely offline.
- Offline functionality is focused on the locally persisted shopping cart.
- Product images require network access when they have not already been loaded.

---

## 📌 Assessment

**Assessment:** Product Catalog & Offline Cart  
**Organization:** Spire Lab, IISc  
**Role:** Android Developer  
**Platform:** Android  
**Language:** Kotlin

---

## 👨‍💻 Developer

**Swapnil Kadam**

GitHub:

https://github.com/swapnilk1806

---

## 📄 License

This project was created for the Spire Lab Android Developer practical assessment.
