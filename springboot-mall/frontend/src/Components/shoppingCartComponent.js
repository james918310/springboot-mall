// ShoppingCartComponent.js
import React, { useEffect, useState } from "react";
import "../App.css";
import { Link } from "react-router-dom";
import { FaShoppingCart } from "react-icons/fa";
import ShoppingCarService from "../services/shoppingCar.service";

const ShoppingCartComponent = ({ currentUser, cartItems, setCartItems }) => {
  const [shoppingCar, setShoppingCar] = useState([]);
  const totalAmount = shoppingCar.reduce((sum, item) => sum + item.amount, 0);
  const [isSubmitting, setIsSubmitting] = useState(false); // 防止多次點擊
  console.log(cartItems);
  // 對後端做請求的到詳細資料

  useEffect(() => {
    const shoppingCar = async () => {
      try {
        const response = await ShoppingCarService.addToShoppingCart(
          currentUser.user.userId,
          cartItems,
        );

        console.log(response);
        if (response.data.length > 0)
          setShoppingCar(response.data); // 假設後端返回的資料結構中包含 results
        else {
          setShoppingCar([]);
        }
      } catch (error) {
        console.error("Failed to fetch books:", error);
      }
    };
    shoppingCar();
  }, [cartItems]);

  console.log(shoppingCar);
  // 購物車數量加
  const handleAddToCart = (productId, price) => {
    setCartItems((prevItems) => {
      return prevItems.map((item) =>
        item.productId === productId
          ? {
              ...item,
              quantity: item.quantity + 1,
              amount: (item.quantity + 1) * price,
            }
          : item,
      );
    });
  };
  // 購物車數量減
  const handleRemoveFromCart = (productId, price) => {
    setCartItems((prevItems) => {
      return prevItems
        .map((item) =>
          item.productId === productId
            ? {
                ...item,
                quantity: item.quantity - 1,
                amount: (item.quantity - 1) * price,
              }
            : item,
        )
        .filter((item) => item.quantity > 0); // 如果數量小於等於 0，就從購物車中移除
    });
  };

  // 下單
  const createOrder = async () => {
    if (isSubmitting) return; // 如果已經在下單，阻止額外請求

    setIsSubmitting(true); // 標記請求開始
    try {
      const response = await ShoppingCarService.createOrder(
        currentUser.user.userId,
        currentUser.user.email,
        cartItems,
      );

      console.log(response);

      // 清空購物車
      setCartItems([]);
      setShoppingCar([]);

      // 顯示下單成功的彈窗
      alert("下單成功！");
    } catch (error) {
      console.error("Failed to create order:", error);
    } finally {
      setIsSubmitting(false); // 無論成功或失敗，請求結束後都要重設
    }
  };

  if (!currentUser) {
    // 如果用户未登录，显示提示登录
    return (
      <div className="shopping-cart-container">
        <div className="cart-empty">
          <h2>請先登入</h2>
          <Link to="/login" className="continue-shopping-btn">
            登入
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="shopping-cart-container">
      {shoppingCar.length > 0 ? (
        <div className="cart-filled">
          <h2>購物車 ({shoppingCar.length} 件)</h2>
          <div
            className={`cart-table-container ${shoppingCar.length > 5 ? "scrollable" : ""}`}
          >
            <table className="cart-table">
              <thead>
                <tr>
                  <th>商品資料</th>
                  <th>單件價格</th>
                  <th>數量</th>
                  <th>小計</th>
                </tr>
              </thead>
              <tbody>
                {shoppingCar.map((item, index) => (
                  <tr key={index}>
                    <td className="product-info">
                      <img
                        src={item.product.imageUrl}
                        alt={item.product.productName}
                        className="product-image"
                      />
                      <span>
                        {item.product.productName.length > 5
                          ? item.product.productName.substring(0, 5) + "..."
                          : item.product.productName}
                      </span>
                    </td>
                    <td>NT${item.product.price}</td>
                    <td>
                      <button
                        className="quantity-btn"
                        onClick={() =>
                          handleRemoveFromCart(item.product.productId)
                        }
                      >
                        -
                      </button>
                      <span>{item.quantity}</span>
                      <button
                        className="quantity-btn"
                        onClick={() => handleAddToCart(item.product.productId)}
                      >
                        +
                      </button>
                    </td>
                    <td>NT${item.amount}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* 購物總價 + Checkout 按鈕 */}
          <div className="cart-summary">
            <span className="cart-total">
              總金額: <strong>${totalAmount}</strong>
            </span>
            {/*<button className="checkout-btn" onClick={createOrder}>*/}
            {/*  下單*/}
            {/*</button>*/}
            <button
              className="checkout-btn"
              onClick={createOrder}
              disabled={isSubmitting}
            >
              {isSubmitting ? "下單中..." : "下單"}
            </button>
          </div>
        </div>
      ) : (
        <div className="cart-empty">
          <h2>你的購物車是空的</h2>
          <p>記得加入商品到你的購物車</p>
          <FaShoppingCart className="cart-empty-icon" />
          <Link to="/" className="continue-shopping-btn">
            繼續購物
          </Link>
        </div>
      )}
    </div>
  );
};

export default ShoppingCartComponent;
