package news.international.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import news.international.NewsScreenType;
import news.international.R;
import news.international.activities.CategoryActivity;
import news.international.activities.MainActivity;
import news.international.adapter.NewsAdapter;
import news.international.models.NewsModel;
import news.international.utils.AppConstants;

import static com.facebook.FacebookSdk.getApplicationContext;


public class CategoryNewsFragment extends Fragment implements View.OnClickListener{
    public static NewsAdapter adapter;
    View view;
    NewsModel newsModel;
    RecyclerView recycleView;
    LinearLayoutCompat linear_category;
    private static final String ARG_PARAM = "param";
    public CategoryNewsFragment() {
        // Required empty public constructor
    }
    public static CategoryNewsFragment newInstance(String param) {
        CategoryNewsFragment fragment = new CategoryNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam = getArguments().getString(ARG_PARAM);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_news, container, false);
        newsModel = ((MainActivity) getActivity()).getSelectedCategoryNewsModel();
        if(newsModel.getNews()!=null) {

            view.findViewById(R.id.linear_status).setVisibility(View.GONE);
            adapter = new NewsAdapter(NewsScreenType.CategoryNews,getActivity(),newsModel);
            recycleView = view.findViewById(R.id.recycleView);
            linear_category=view.findViewById(R.id.linear_category);
            linear_category.setOnClickListener(this);
            recycleView.setAdapter(adapter);
            recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.linear_category){
            selectCategory();
        }
    }
    public void selectCategory( ){
        String[] array=new String[newsModel.getCategories().size()];
        for (int position=0;position<newsModel.getCategories().size();position++) {
            NewsModel.Categories category =newsModel.getCategories().get(position);
            array[position]=category.category;
        }
        Bundle b=new Bundle();
        b.putStringArray(AppConstants.CATEGORIES,array);
        Intent intent=new Intent(getApplicationContext(), CategoryActivity.class);
        intent.putExtras(b);
        getActivity().startActivityForResult(intent, AppConstants.CATEGORYREQUEST_CODE);
    }

}