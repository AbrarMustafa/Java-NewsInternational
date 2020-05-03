package news.international.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import news.international.MyApplication;
import news.international.NewsScreenType;
import news.international.R;
import news.international.activities.CategoryActivity;
import news.international.activities.MainActivity;
import news.international.adapter.NewsAdapter;
import news.international.models.NewsModel;
import news.international.utils.AppConstants;
import news.international.utils.MyPrefrences;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SavedFragment extends Fragment implements View.OnClickListener {
    public static NewsAdapter adapter;
    RecyclerView recycleView;
    public MyPrefrences myPrefrences;
    private static final String ARG_PARAM = "param";
    public static SwipeRefreshLayout pullToRefresh;
    LinearLayoutCompat linear_category;
    LinearLayoutCompat linear_status;
    NewsModel newsModel;

    public SavedFragment() {
        myPrefrences = new MyPrefrences(getApplicationContext());

        // Required empty public constructor
    }

    public static SavedFragment newInstance(String param) {
        SavedFragment fragment = new SavedFragment();
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

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        linear_category = view.findViewById(R.id.linear_category);
        linear_status = view.findViewById(R.id.linear_status);

        linear_category.setOnClickListener(this);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLatestNews();

            }
        });
        linear_status.setVisibility(View.GONE);

        getLatestNews();
        return view;
    }

    //=======================================Latest NEWS==============================//
    public void getLatestNews() {
        pullToRefresh.setRefreshing(true);
        linear_category.setVisibility(View.GONE);
        ((MyApplication) getApplicationContext()).apiService.getMarkedNews("Token "+myPrefrences.getToken()).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<NewsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NewsModel _newsModel) {
                        newsModel = _newsModel;
                        if (_newsModel.getNews() != null&&getActivity()!=null&& ((MainActivity)getActivity()).isActivityRunning) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linear_category.setVisibility(View.VISIBLE);
                                    adapter = new NewsAdapter(NewsScreenType.SavedNews, getActivity(), newsModel);
                                    recycleView = view.findViewById(R.id.recycleView);
                                    recycleView.setAdapter(adapter);
                                    recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error" + e);
                        if (getActivity()!=null&& ((MainActivity)getActivity()).isActivityRunning) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (newsModel == null) {
                                        linear_status.setVisibility(View.VISIBLE);
                                        view.findViewById(R.id.imageView_error).setVisibility(View.GONE);
                                        view.findViewById(R.id.imageView_error).setVisibility(View.GONE);
                                        ((TextView) view.findViewById(R.id.textview_error)).setText(getString(R.string.nosavedvideo));
                                    }
                                    pullToRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (getActivity()!=null&& ((MainActivity)getActivity()).isActivityRunning) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.findViewById(R.id.linear_status).setVisibility(View.GONE);
                                    pullToRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }
                });

        linear_status.setVisibility(View.GONE);
    }

    public void selectCategory() {
        String[] array = new String[newsModel.getCategories().size()];
        for (int position = 0; position < newsModel.getCategories().size(); position++) {
            NewsModel.Categories category = newsModel.getCategories().get(position);
            array[position] = category.category;
        }
        Bundle b = new Bundle();
        b.putStringArray(AppConstants.CATEGORIES, array);
        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        intent.putExtras(b);
        getActivity().startActivityForResult(intent, AppConstants.CATEGORYREQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.linear_category) {
            selectCategory();
        }
    }
}