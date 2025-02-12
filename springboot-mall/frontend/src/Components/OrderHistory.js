import React, { useState } from "react";
import axios from "axios";

const OrderHistory = (currentUser) => {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [orderDetails, setOrderDetails] = useState([]);

  // useEffect(() => {
  //   axios
  //     .get("http://localhost:8080/orders")
  //     .then((response) => setOrders(response.data))
  //     .catch((error) => console.error("Error fetching orders:", error));
  // }, []);

  const fetchOrderDetails = (orderId) => {
    axios
      .get(`http://localhost:8080/orders/${orderId}`)
      .then((response) => {
        setSelectedOrder(orderId);
        setOrderDetails(response.data);
      })
      .catch((error) => console.error("Error fetching order details:", error));
  };

  return (
    <div>
      <h2>歷史訂單</h2>
      <ul>
        {orders.map((order) => (
          <li
            key={order.orderId}
            onClick={() => fetchOrderDetails(order.orderId)}
          >
            訂單 {order.orderId} - 總金額: {order.totalAmount}
          </li>
        ))}
      </ul>

      {selectedOrder && (
        <div>
          <h3>訂單 {selectedOrder} 詳細內容</h3>
          <ul>
            {orderDetails.map((item) => (
              <li key={item.productId}>
                {item.productName} - {item.quantity} 件 - {item.amount}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default OrderHistory;
