package news.international.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsModel {
    @SerializedName("news")
    List<News> news;
    List<Categories> categories;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public class News{

        @SerializedName("id")
        private int id;

        @SerializedName("newsKey")
        private String newsKey;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("url")
        private String url;

        @SerializedName("author")
        private String author;

        @SerializedName("image")
        private String image;

        @SerializedName("language")
        private String language;

        @SerializedName("published")
        private String published;

        public boolean isMarked;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNewsKey() {
            return newsKey;
        }

        public void setNewsKey(String newsKey) {
            this.newsKey = newsKey;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }
    }


    public class Categories{
        @SerializedName("category")
        public String category;

    }
}
