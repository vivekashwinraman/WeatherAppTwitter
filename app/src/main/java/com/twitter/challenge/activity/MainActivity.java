package com.twitter.challenge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.challenge.R;
import com.twitter.challenge.adapters.WeatherAdapter;
import com.twitter.challenge.model.WeatherCondition;
import com.twitter.challenge.network.WeatherClient;
import com.twitter.challenge.network.WeatherInterface;
import com.twitter.challenge.utils.TemperatureConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    WeatherInterface weatherInterface;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager horizontalLayoutManager;
    private WeatherAdapter adapter;
    protected CompositeSubscription compositeSubscription;
    private TextView locationView ;
    private TextView temperatureView ;
    private TextView windSpeedView ;
    private Button button ;


    private final ArrayList<WeatherCondition> weatherConditionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureView = (TextView) findViewById(R.id.temperature);
        windSpeedView = (TextView) findViewById(R.id.wind_speed);
        recyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        weatherInterface = WeatherClient.getClient().create(WeatherInterface.class);
        adapter = new WeatherAdapter(weatherConditionList);
        horizontalLayoutManager
                = new GridLayoutManager(MainActivity.this, 5);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
        button = (Button) findViewById(R.id.future_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!recyclerView.isShown()) {
                    if (weatherConditionList.size() < 5) {
                        weatherConditionList.clear();
                        adapter.notifyDataSetChanged();
                        makeGetFutureCall();
                    }
                    button.setText(R.string.hide_future);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    button.setText(R.string.show_future);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
        compositeSubscription = new CompositeSubscription();
        makeGetCurrentCall();
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }


    private void makeGetFutureCall() {

        for (int i = 1; i <= 5; i++) {
            Observable<WeatherCondition> call = weatherInterface.getFuture("" + i);
            Subscription subscription = call
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WeatherCondition>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof HttpException) {
                                HttpException response = (HttpException) e;
                                Log.d("RetrofitTest", "Error code: " + response.code());
                            }
                        }

                        @Override
                        public void onNext(WeatherCondition weatherCondition) {
                            weatherConditionList.add(weatherCondition);
                            adapter.notifyDataSetChanged();
                        }
                    });

            compositeSubscription.add(subscription);
        }
    }


    private void makeGetCurrentCall() {
        Observable<WeatherCondition> call = weatherInterface.getCurrent();
        Subscription subscription = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherCondition>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            Log.d("RetrofitTest", "Error code: " + response.code());
                        }
                    }

                    @Override
                    public void onNext(WeatherCondition weatherCondition) {
                        Toast.makeText(getApplicationContext(), weatherCondition.getName(), Toast.LENGTH_LONG).show();
                        Log.d("Hello", weatherCondition.getName());
                        String temperatureString = String.valueOf(weatherCondition.getWeather().getTemp())+"F/"
                                + TemperatureConverter.celsiusToFahrenheit(weatherCondition.getWeather().getTemp())+"C";
                        locationView.setText(weatherCondition.getName());
                        temperatureView.setText(temperatureString);
                        windSpeedView.setText(String.valueOf(weatherCondition.getWind().getSpeed()));
                    }
                });
        compositeSubscription.add(subscription);
    }

}
