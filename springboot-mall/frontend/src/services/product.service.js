import axios from "axios";

const API_URL = "http://localhost:8080/products";

class ProductService {
  products(
    selectedCategory,
    searchQuery,
    orderBy,
    sort,
    limit,
    currentPage,
    minPrice,
    maxPrice,
  ) {
    console.log("server limit" + limit);
    console.log("server selectedCategory" + selectedCategory);
    return axios.get(API_URL, {
      params: {
        category:
          selectedCategory !== "All Categories" ? selectedCategory : undefined,
        search: searchQuery,
        orderBy,
        sort,
        minPrice: minPrice,
        maxPrice: maxPrice,
        limit: limit,
        offset: (currentPage - 1) * limit,
      },
    });
  }
}

export default new ProductService();
