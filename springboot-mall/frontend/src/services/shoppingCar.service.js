import axios from "axios";

const API_URL = "http://localhost:8080/users/";

class ShoppingCarService {
  // 發送購物車資料
  addToShoppingCart(userId, cartItems) {
    return axios.post(API_URL + userId + `/shoppingCar`, {
      buyItemList: cartItems,
    });
  }

  // 下訂單
  createOrder(userId, email, cartItems) {
    return axios.post(API_URL + userId + `/orders`, {
      buyItemList: cartItems,
      email: email,
    });
  }
}

export default new ShoppingCarService();
