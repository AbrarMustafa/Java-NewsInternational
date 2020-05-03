package news.international.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import news.international.R;
import news.international.adapter.CategoryAdapter;
import news.international.models.NewsModel;
import news.international.utils.AppConstants;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryActivity extends AppCompatActivity {
    String[] array;
    CategoryAdapter categoryAdapter;
    RecyclerView recycleView_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Bundle b=this.getIntent().getExtras();
        array=b.getStringArray(AppConstants.CATEGORIES);

        categoryAdapter=new CategoryAdapter(this,array);
        recycleView_category=findViewById(R.id.recycleView_category);

        recycleView_category.setAdapter(categoryAdapter);
        recycleView_category.setLayoutManager(new GridLayoutManager(this, 3));
    }
}
