import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/auth.service";

const LoginComponent = ({ currentUser, setCurrentUser }) => {
  const navigate = useNavigate();
  let [email, setEmail] = useState("");
  let [password, setPassword] = useState("");

  const handleEmail = (e) => {
    setEmail(e.target.value);
  };
  const handlePassword = (e) => {
    setPassword(e.target.value);
  };
  const handleLogin = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      alert("所有欄位必填！");
      return;
    }
    try {
      const response = await AuthService.login(email, password); // 正確解析 Promise
      alert("登入成功");
      localStorage.setItem("user", JSON.stringify(response.data));
      window.alert("登入成功");
      setCurrentUser(AuthService.getCurrentUser());
      navigate("/"); // 登入成功後跳轉
    } catch (e) {
      console.error("登入失敗：", e); // 捕獲錯誤
      alert("登入失敗，請稍後再試");
    }
  };
  const handleClick = () => {
    console.log("Google Button Clicked");
    // 重定向到 Google 登录 URL，并传递角色信息
    // 備註1
    window.location.href = `http://localhost:8080/api/user/auth/google`;
  };

  // Google OAuth 回調處理
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code"); // 從 URL 中獲取授權碼

    if (code) {
      // 發送請求到後端，處理 Google 登入
      // 備註2ㄌ
      fetch("http://localhost:8080/login/oauth2/code/google", {
        method: "GET",
        credentials: "include", // 確保帶上 cookies（如果需要）
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json();
        })
        .then((data) => {
          if (data.token) {
            // 保存 Token 到 localStorage
            localStorage.setItem("jwt", data.token);
            // 設置當前用戶
            setCurrentUser(data.user);
            // 跳轉到主頁或 Dashboard
            window.location.href = "/";
          } else {
            console.error("登入失敗：", data.message);
            alert(data.message || "登入失敗，請稍後再試");
          }
        })
        .catch((error) => {
          console.error("伺服器錯誤：", error);
          alert("伺服器錯誤，請稍後再試");
        });
    }
  }, []);

  return (
    <div style={{ padding: "3rem" }} className="col-md-12">
      <div>
        <div className="form-group">
          <label htmlFor="username">電子信箱：</label>
          <input
            onChange={handleEmail}
            type="text"
            className="form-control"
            name="email"
          />
        </div>
        <br />
        <div className="form-group">
          <label htmlFor="password">密碼：</label>
          <input
            onChange={handlePassword}
            type="password"
            className="form-control"
            name="password"
          />
        </div>
        <br />
        <div className="form-group">
          <button onClick={handleLogin} className="btn btn-primary btn-block">
            <span>登入系統</span>
          </button>
        </div>
        <br />
        <div>
          {/* google登入按鈕 */}
          <div style={{ margin: "5 re" }} ass="col-md-12">
            <button
              onClick={handleClick}
              className="btn btn-lg btn-google"
              style={{
                margin: "5 rem",
                padding: "0.1rem 0.5rem",
                backgroundColor: "rgb(7, 7, 7)",
                color: "rgb(255, 255, 255)",
              }}
            >
              <img
                src="https://img.icons8.com/color/16/000000/google-logo.png"
                alt="Google logo"
              />
              google登入
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
