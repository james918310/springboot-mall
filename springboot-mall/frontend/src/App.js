import { BrowserRouter, Route, Routes } from "react-router-dom";
import Layout from "./Components/Layout";
import HomeComponent from "./Components/home-component";
import RegisterComponent from "./Components/register-component";
import LoginComponent from "./Components/login-component";
import { useState } from "react";
import AuthService from "./services/auth.service";
import GoogleCallbackHandler from "./Components/GoogleCallbackHandler";
import ShoppingCartComponent from "./Components/shoppingCartComponent";
import OrderHistory from "./Components/OrderHistory";

function App() {
  let [currentUser, setCurrentUser] = useState(AuthService.getCurrentUser);
  const [searchQuery, setSearchQuery] = useState("");
  const [cartItems, setCartItems] = useState([]); // 新增购物车状态
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <Layout
              currentUser={currentUser}
              setCurrentUser={setCurrentUser}
              searchQuery={searchQuery}
              setSearchQuery={setSearchQuery}
              cartItems={cartItems}
              setCartItems={setCartItems}
            />
          }
        >
          <Route
            index
            element={
              <HomeComponent
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
                cartItems={cartItems}
                setCartItems={setCartItems}
              />
            }
          />
          <Route path="register" element={<RegisterComponent />} />
          <Route
            path="login"
            element={
              <LoginComponent
                currentUser={currentUser}
                setCurrentUser={setCurrentUser}
              />
            }
          />
          <Route
            path="ShoppingCar"
            element={
              <ShoppingCartComponent
                currentUser={currentUser}
                setCurrentUser={setCurrentUser}
                cartItems={cartItems}
                setCartItems={setCartItems}
              />
            }
          />
          <Route
            path="orderHistory"
            element={<OrderHistory currentUser={currentUser} />}
          />
          <Route path="/google/callback" element={<GoogleCallbackHandler />} />
          ShoppingCartComponent
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
