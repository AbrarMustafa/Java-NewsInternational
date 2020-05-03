package news.international.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccessModel {

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
