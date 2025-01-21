import { BrowserRouter, Route, Routes } from "react-router-dom";
import Layout from "./Components/Layout";
import HomeComponent from "./Components/home-component";
import RegisterComponent from "./Components/register-component";
import LoginComponent from "./Components/login-component";
import { useState } from "react";
import AuthService from "./services/auth.service";
import GoogleCallbackHandler from "./Components/GoogleCallbackHandler";

function App() {
  let [currentUser, setCurrentUser] = useState(AuthService.getCurrentUser);
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <Layout currentUser={currentUser} setCurrentUser={setCurrentUser} />
          }
        >
          <Route index element={<HomeComponent />} />
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
          <Route path="/google/callback" element={<GoogleCallbackHandler />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
