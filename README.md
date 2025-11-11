# 🛒 E-commerce Demo — Spring Boot (Java 17)

**Repository:** `e-commerce-demo`  
**Framework:** Spring Boot (Maven)  
**Package root:** `com.example.ecommerce_demo`

---

## 📘 Overview

A lightweight, in-memory **e-commerce backend** built in Java Spring Boot.  
Implements:

- Add items to cart (snapshot price)
- Checkout (validate & consume 10% discount coupon)
- Auto-generate a 10% coupon every *n*th order
- Admin summary endpoint (total items, purchase totals, discount codes)
- In-memory repositories (no external DB)
- Unit tests for business logic and concurrency

---

## 🧰 Prerequisites

- Java 17 (Temurin / OpenJDK)
- Git
- Maven wrapper (`mvnw`) — included
- Postman or VS Code REST Client (optional for testing)

---

## 📂 Project Layout
src/
main/
java/com/example/ecommerce_demo/
controller/
CartController.java
CheckoutController.java
AdminController.java
dto/
AddToCartRequest.java
CheckoutRequest.java
CheckoutResponse.java
AdminSummary.java
model/
Item.java
CartItem.java
Cart.java
Order.java
DiscountCode.java
repository/
ItemRepository.java
CartRepository.java
OrderRepository.java
DiscountRepository.java
service/
CartService.java
CheckoutService.java
AdminService.java
resources/
application.properties
test/
java/com/example/ecommerce_demo/...
pom.xml

---

## ⚙️ Configuration

**File:** `src/main/resources/application.properties`

```properties
# Generate a coupon every Nth order (default 5)
ecommerce.coupon.everyNth=5
#Change this number for testing (e.g., set 1 to generate a coupon every checkout).

# (Optional) disable devtools restart if you encounter mapping issues
# spring.devtools.restart.enabled=false
```

## 🚀 Build & Run (step-by-step)
1. Open terminal in project root (folder containing pom.xml).
2. Build (package):  `.\mvnw clean package`
3. Run with Maven wrapper (development): `.\mvnw spring-boot:run`
4. Visit the health endpoint to confirm the app is running:
```
GET http://localhost:8080/ping
# Response: "E-commerce service is up 🚀"
```
---
## APIs (endpoints) — Usage Examples

Base URL: `http://localhost:8080`

### 1) Health
- GET /ping
- Response: plain text `E-commerce service is up 🚀`


### 2) Cart endpoints
- Add item to cart `POST /cart/{userId}/items`  
Body (JSON):  `{ "itemId": "item-1", "quantity": 2 }`
- Get cart `GET /cart/{userId}`
- Example: `GET http://localhost:8080/cart/user-1`

### 3) Checkout
- `POST /cart/{userId}/checkout`
- Body (optional): `{ "couponCode": "SAVE10-abc123" }`
- Successful response (201 Created):
  ```
  {
  "orderId":"uuid-...",
  "userId":"user-1",
  "totalAmount":998.00,
  "discountAmount":99.80,
  "finalAmount":898.20,
  "appliedDiscountCode":"SAVE10-abc123",
  "generatedDiscountCode":"SAVE10-xyz456" // present if this checkout triggered nth-order generation
  }
  ```
### 4) Admin endpoints  
Admin summary `GET /admin/summary`  
Returns:
```
{
  "totalItemsPurchased": 10,
  "totalPurchaseAmount": 12345.00,
  "discountCodes": [
    { "code":"SAVE10-abc123", "discountPercent":10, "used":false, ... }
  ],
  "totalDiscountAmount": 123.45
}
```

## How to import into Postman

- Open Postman → Import → Choose File and select docs/postman_collection.json.
- Postman will import the collection with the requests shown above.
- Edit request bodies or user IDs as needed; run requests against http://localhost:8080.

## Testing
Run unit tests:  `.\mvnw test `.  
Included tests:
- `CartServiceTest` — tests add-to-cart behavior
- `CheckoutServiceTest` — tests checkout flow & discount application
- `DiscountConcurrencyTest` — ensures single-use discount consumption under concurrency


## Design decisions & notes

- Discount validation at checkout — discounts are validated at the final stage to avoid honoring expired/used coupons prematurely.
- Price snapshot at add-to-cart — cart items store unitPrice as a snapshot to ensure stable order totals.
- In-memory stores — ConcurrentHashMap + small synchronized blocks used for thread safety in this demo.
- Atomic coupon consumption — markAsUsedIfAvailable() uses synchronization on the coupon object to guarantee single-use.
- Order counter — OrderRepository maintains an AtomicLong used to determine nth-order coupon generation.
