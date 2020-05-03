//package news.international;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
//import news.international.activities.MainActivity;
//import news.international.interfaces.RequestData;
//import news.international.models.NewsModel;
//
//
//public class ApiInterface extends AsyncTask<Void,Void,Void> {
//
//    public static ArrayList<NewsModel> itemArrayList = new ArrayList<>();
//    String header_title,header_des,header_img ;
//    Bitmap b;
//    RequestData requestData;
//    public ApiInterface(RequestData requestData){
//        this.requestData=requestData;
//    }
//    @Override
//    protected Void doInBackground(Void... voids) {
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        String newsJsonStr = null;
//
//        try {
//            URL url = new URL("http://64.227.10.213:8000/newApi/app/v1/recentnews/");
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//
//            newsJsonStr = buffer.toString();
//
//            JSONObject jsonObj = new JSONObject(newsJsonStr);
//            String date = jsonObj.getString("news");
//
//            JSONArray jsonArray = new JSONArray(date);
//
//            boolean firstItem = true;
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String checkimg = jsonObject.getString("image");
//                if (!checkimg.equals("None")) {
//                    String title= jsonObject.getString("title");
//                    String des = jsonObject.getString("description");
//                    if (!firstItem){
//                        NewsModel item = new NewsModel();
//
//                        item.setTitle(title);
//                        item.setDescription(des);
//                        item.setUrl(jsonObject.getString("url"));
//                        item.setAuthor(jsonObject.getString("author"));
//                        item.setLanguage(jsonObject.getString("language"));
//                        item.setCategory(jsonObject.getString("category"));
//                        item.setImage(checkimg);
//
//
//                        itemArrayList.add(item);
//                    }else {
//
//                        header_title = title;
//                        header_des = des;
//                        header_img = checkimg;
//                        URL o = new URL(checkimg);
//                        InputStream is = new BufferedInputStream(o.openStream());
//                        b = BitmapFactory.decodeStream(is);
//                        firstItem = false;
//                    }
//                }
//            }
//        }
//
//        catch (IOException e) {
//            Log.e("PlaceholderFragment", "Error ", e);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("PlaceholderFragment", "Error closing stream", e);
//                }
//            }
//        }
//        return null;
//    }
//
//        @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        requestData.dataFetch();
//        MainActivity.adapter.notifyDataSetChanged();
//        pullToRefresh.setRefreshing(false);
//
//    }
//}
