import React, { useState } from "react";
import AuthService from "../services/auth.service";
import { useNavigate } from "react-router-dom";

const RegisterComponent = () => {
  const navigate = useNavigate();
  let [providerName, setProviderName] = useState("");
  let [email, setEmail] = useState("");
  let [password, setPassword] = useState("");

  const handleProviderName = (e) => {
    setProviderName(e.target.value);
  };
  const handleEmail = (e) => {
    setEmail(e.target.value);
  };
  const handlePassword = (e) => {
    setPassword(e.target.value);
  };

  const handleRegister = (e) => {
    e.preventDefault();
    if (!providerName || !email || !password) {
      alert("所有欄位必填！");
      return;
    }
    // if (!/\S+@\S+\.\S+/.test(email)) {
    //   alert("請輸入有效的電子郵件地址！");
    //   return;
    // }
    if (password.length < 6) {
      alert("密碼長度至少為 6！");
      return;
    }
    AuthService.register(providerName, email, password)
      .then(() => {
        alert("註冊成功，將被導入登入頁面");
        navigate("/login");
      })
      .catch((e) => {
        console.error(e);
        alert("註冊失敗，請稍後再試");
      });
  };

  return (
    <div style={{ padding: "3rem", margin: "70px" }} className="col-md-12">
      <div>
        <div>
          <label htmlFor="providerName">用戶名稱:</label>
          <input
            onChange={handleProviderName}
            type="text"
            className="form-control"
            name="setProviderName"
          />
        </div>
        <br />
        <div className="form-group">
          <label htmlFor="email">電子信箱：</label>
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
            placeholder="長度至少超過6個英文或數字"
          />
        </div>
        <br />
        <button onClick={handleRegister} className="btn btn-primary">
          <span>註冊會員</span>
        </button>
      </div>
    </div>
  );
};

export default RegisterComponent;
