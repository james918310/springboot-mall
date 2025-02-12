import { Outlet } from "react-router-dom";
import Nav from "./nav-component";
import React from "react";

const Layout = ({
  currentUser,
  setCurrentUser,
  searchQuery,
  setSearchQuery,
  cartItems,
  setCartItems,
}) => {
  return (
    <>
      <Nav
        className="fixed-nav" // 新增 className
        currentUser={currentUser}
        setCurrentUser={setCurrentUser}
        searchQuery={searchQuery}
        setSearchQuery={setSearchQuery}
        cartItems={cartItems}
        setCartItems={setCartItems}
      />
      <Outlet />
    </>
  );
};

export default Layout;
