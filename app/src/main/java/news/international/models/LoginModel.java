package news.international.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {
        @SerializedName("data")
        String data;

        @SerializedName("token")
        String token;


        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

}
