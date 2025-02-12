import React, { useState } from "react";
import AuthService from "../services/auth.service";
import { Link } from "react-router-dom";
import { FaBook, FaShoppingCart, FaUserCircle } from "react-icons/fa";

const NavComponent = ({
  currentUser,
  setCurrentUser,
  searchQuery,
  setSearchQuery,
  cartItems,
  setCartItems,
}) => {
  const [dropdownVisible, setDropdownVisible] = useState(false); // 控制下拉選單顯示

  const handleLogout = () => {
    const confirmLogout = window.confirm("您確定要登出嗎？"); // 顯示確認視窗
    if (confirmLogout) {
      AuthService.logout(); // 清空 local storage
      window.alert("登出成功");
      setCurrentUser(null);
    }
  };

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value); // 更新搜尋內容
  };

  return (
    <div>
      <nav>
        <header className="header">
          <div className="header-content">
            <FaBook size={30} />
            <div>
              <Link to="/" className="header-link">
                <h1>Book Cart</h1>
              </Link>
            </div>
            <input
              type="text"
              placeholder="Search books or authors"
              className="search-bar"
              value={searchQuery}
              onChange={handleSearchChange}
            />
            <Link className="shopping-icon" to="/ShoppingCar">
              <div className="cart-icon">
                <FaShoppingCart size={30} />
                <span className="cart-count">{cartItems.length}</span>
              </div>
            </Link>
            {!currentUser && (
              <div className="login-register-container">
                <Link className="login-button" to="/login">
                  登入
                </Link>
                <Link className="register-button" to="/register">
                  註冊
                </Link>
              </div>
            )}

            {currentUser && (
              <div
                className="user-dropdown"
                onMouseEnter={() => setDropdownVisible(true)}
                onMouseLeave={(e) => {
                  // 檢查滑鼠是否離開了下拉範圍
                  if (
                    !e.relatedTarget ||
                    !e.currentTarget.contains(e.relatedTarget)
                  ) {
                    setDropdownVisible(false);
                  }
                }}
              >
                <div className="user-icon">
                  <FaUserCircle size={40} />
                </div>
                {dropdownVisible && (
                  <div className="dropdown-menu">
                    <p>個人資料</p>
                    <Link to="/orderHistory">訂單紀錄</Link>
                    <button onClick={handleLogout}>登出</button>
                  </div>
                )}
              </div>
            )}
          </div>
        </header>
      </nav>
    </div>
  );
};

export default NavComponent;
