import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const GoogleCallbackHandler = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // 從 URL 中提取 token
    const queryParams = new URLSearchParams(window.location.search);
    const token = queryParams.get("token");

    if (token) {
      // 將 token 存儲在 localStorage 中
      localStorage.setItem("jwtToken", token);

      // 跳轉到首頁
      navigate("/");
    } else {
      console.error("認證失敗：未找到 Token");
      navigate("/"); // 跳轉到登入頁面
    }
  }, [navigate]);

  return <div>正在處理 Google 登入...</div>;
};

export default GoogleCallbackHandler;
