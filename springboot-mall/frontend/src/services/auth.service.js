import axios from "axios";

const API_URL = "http://localhost:8080/users";

class AuthService {
  login(email, password) {
    return axios.post(API_URL + "/login", {
      email,
      password,
    });
  }

  logout() {
    localStorage.removeItem("user");
  }

  register(providerName, email, password) {
    return axios.post(API_URL + "/register", {
      providerName,
      email,
      password,
    });
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem("user"));
  }
}

export default new AuthService();
