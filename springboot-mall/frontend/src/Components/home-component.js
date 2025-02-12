import React, { useEffect, useRef, useState } from "react";
import ReactSlider from "react-slider"; // 使用 react-slider 實現範圍滑條
import "../App.css"; // 確保添加一些 CSS 來設置樣式
import ProductService from "../services/product.service";

const HomeComponent = ({
  searchQuery,
  setSearchQuery,
  cartItems,
  setCartItems,
}) => {
  // 狀態變量：用於存儲當前選中的分類
  const [selectedCategory, setSelectedCategory] = useState("All Categories");
  const [selectedDropdown, setSelectedDropdown] = useState("名稱A~Z"); // 用於存儲下拉選單值
  const [currentPage, setCurrentPage] = useState(1); // 用於存儲當前頁碼
  // 狀態變量：用於存儲搜尋查詢
  // const [searchQuery, setSearchQuery] = useState("");
  // 狀態變量：用於存儲價格範圍
  const [minPrice, setMinPrice] = useState(1); // 最低價格
  const [maxPrice, setMaxPrice] = useState(10000000); // 最高價格

  // 狀態變量：用於存儲排序和分頁
  const [orderBy, setOrderBy] = useState("created_date");
  const [sort, setSort] = useState("desc");
  const [limit, setLimit] = useState(5); // 用於存儲每頁資料數量
  // 狀態變量：用於存儲從後端獲取的書籍
  const [books, setBooks] = useState([]);
  const [totalPages, setTotalPages] = useState(1); // 總頁數

  // 可用的書籍分類列表
  const categories = [
    { display: "所有類別", value: "All Categories" },
    { display: "食品", value: "FOOD" },
    { display: "汽車", value: "CAR" },
    { display: "程式設計", value: "E_BOOK" },
    { display: "漫畫", value: "COMICS" },
    { display: "AI產業", value: "AI_Industry" },
    { display: "現代小說", value: "Modern_Fiction" },
  ];

  const dropdownOptions = [
    { display: "名稱A~Z", value: ["product_name", "ASC"] },
    { display: "最新產品", value: ["created_date", "DESC"] },
    { display: "價格低到高", value: ["price", "ASC"] },
    { display: "價格高到低", value: ["price", "DESC"] },
  ];

  const dropdownOptionsNumber = [1, 5, 10, 20, 30, 50, 100];
  const previousCategoryRef = useRef(selectedCategory); // 使用 useRef 追蹤 selectedCa

  // 購物車儲存變數
  const handleAddToCart = (productId) => {
    setCartItems((prevItems) => {
      const existingItem = prevItems.find(
        (item) => item.productId === productId,
      );
      if (existingItem) {
        // 商品已存在，增加数量
        return prevItems.map((item) =>
          item.productId === productId
            ? { ...item, quantity: item.quantity + 1 }
            : item,
        );
      } else {
        // 商品不存在，新增
        return [...prevItems, { productId, quantity: 1 }];
      }
    });
  };

  // 處理分類切換
  const handleCategoryChange = async (categoryValue) => {
    setSelectedCategory(categoryValue); // 更新當前選中的分類
    setMinPrice(1);
    setMaxPrice(1000000);
  };

  // 處理下拉選單變化
  const handleDropdownChange = (event) => {
    const selectedOption = dropdownOptions.find(
      (option) => option.display === event.target.value,
    );
    if (selectedOption) {
      setSelectedDropdown(selectedOption.display);
      setOrderBy(selectedOption.value[0]);
      setSort(selectedOption.value[1]);
    }
  };

  // 控制資料抓取數量
  const handleDropdownChangeNumber = (event) => {
    setLimit(Number(event.target.value));
    setCurrentPage(1); // 當變更每頁數量時，重置為第1頁
  };

  // 處理頁碼切換
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  // 處理搜尋框輸入變化
  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value); // 更新搜尋內容
  };

  // 從後端獲取書籍資料
  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await ProductService.products(
          selectedCategory,
          searchQuery,
          orderBy,
          sort,
          limit,
          currentPage,
          minPrice,
          maxPrice,
        );

        setBooks(response.data.results); // 假設後端返回的資料結構中包含 results

        setTotalPages(Math.ceil(response.data.total / limit)); // 假設 total 為總記錄數
      } catch (error) {
        console.error("Failed to fetch books:", error);
      }
    };

    fetchBooks();
  }, [
    selectedCategory,
    searchQuery,
    orderBy,
    sort,
    limit,
    currentPage,
    minPrice,
    maxPrice,
  ]);
  console.log(books);

  return (
    <div className="app">
      {/* 主內容區域 */}
      <div className="main-content">
        {/* 側邊欄，用於分類與價格篩選 */}
        <aside className="sidebar">
          <h3>Categories</h3>
          <ul className="categories">
            {categories.map((category) => (
              <li
                key={category.value}
                className={category.value === selectedCategory ? "active" : ""} // 高亮當前選中的分類
                onClick={() => handleCategoryChange(category.value)} // 切換分類
              >
                {category.display}
              </li>
            ))}
          </ul>

          {/* 單條價格篩選滑條區域 */}
          <h3>Price Filter</h3>
          <div className="price-filter">
            <ReactSlider
              className="range-slider"
              thumbClassName="slider-thumb"
              trackClassName="slider-track"
              min={1}
              max={1000000}
              step={10} // 設置步長為 10
              value={[minPrice, maxPrice]}
              onChange={([newMinPrice, newMaxPrice]) => {
                setMinPrice(newMinPrice);
                setMaxPrice(newMaxPrice);
              }}
            />
          </div>
          <p>
            ${minPrice} to ${maxPrice}
          </p>
        </aside>

        {/* 書籍列表 */}
        {/*下拉選單*/}
        <div className="dropdown-container-book-list">
          {/*下拉選單排序與抓取資料數量*/}
          <div className="dropdown-container">
            {/*排序方式*/}
            <select
              value={selectedDropdown}
              onChange={handleDropdownChange}
              className="dropdown"
            >
              {dropdownOptions.map((option, index) => (
                <option key={index} value={option.display}>
                  {option.display}
                </option>
              ))}
            </select>
            {/*抓取資料數量下拉選單*/}
            <select
              value={limit}
              onChange={handleDropdownChangeNumber}
              className="dropdown"
            >
              {dropdownOptionsNumber.map((option, index) => (
                <option key={index} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </div>
          {/*資料主體*/}
          <section className="book-list">
            {books.map((book) => (
              <div className="book-card" key={book.title}>
                <img
                  src={book.imageUrl}
                  alt={book.productName}
                  className="book-image"
                />{" "}
                {/* 書籍封面圖片 */}
                <h3>{book.productName}</h3> {/* 書籍標題 */}
                <p>₹{book.price}</p> {/* 書籍價格 */}
                <p>{book.description}</p> {/* 書籍描述 */}
                {/* 添加购物车按钮 */}
                <button
                  className="add-to-cart"
                  onClick={() => handleAddToCart(book.productId)}
                >
                  Add to Cart
                </button>
              </div>
            ))}
          </section>
        </div>
      </div>
      {/* 頁碼控制 */}
      <div className="pagination">
        <button
          disabled={currentPage === 1}
          onClick={() => handlePageChange(currentPage - 1)}
        >
          上一頁
        </button>
        {Array.from({ length: Math.min(6, totalPages) }, (_, i) => {
          const page = i + 1;
          return (
            <button
              key={page}
              className={currentPage === page ? "active" : ""}
              onClick={() => handlePageChange(page)}
            >
              {page}
            </button>
          );
        })}
        {totalPages > 6 && <span>...</span>}
        {totalPages > 6 && (
          <button onClick={() => handlePageChange(totalPages)}>
            {totalPages}
          </button>
        )}
        <button
          disabled={currentPage === totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
        >
          下一頁
        </button>
      </div>
    </div>
  );
};

export default HomeComponent;
