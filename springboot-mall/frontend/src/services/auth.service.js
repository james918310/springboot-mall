import axios from "axios";
const  API_URL = "http://localhost:8080/users"

class AuthService{
    login(){}
    logout(){}
    register(providerName,email,password){
        return  axios.post(API_URL+"/register", {
            providerName,
            email,
            password,
        });
    }
}

export default new AuthService()