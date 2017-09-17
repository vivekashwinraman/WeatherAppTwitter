package com.twitter.challenge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.challenge.R;
import com.twitter.challenge.adapters.WeatherAdapter;
import com.twitter.challenge.model.WeatherCondition;
import com.twitter.challenge.network.WeatherClient;
import com.twitter.challenge.network.WeatherInterface;
import com.twitter.challenge.utils.TemperatureConverter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    WeatherInterface weatherInterface;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager horizontalLayoutManager;
    private WeatherAdapter adapter;


    private final ArrayList<WeatherCondition> weatherConditionList = new ArrayList<>();
    private final ArrayList<Subscription> subscriptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);

        weatherInterface = WeatherClient.getClient().create(WeatherInterface.class);
        //makeGetCurrentCall();

        final TextView temperatureView = (TextView) findViewById(R.id.temperature);
        temperatureView.setText(getString(R.string.temperature, 34f, TemperatureConverter.celsiusToFahrenheit(34)));

        //makeGetFutureCall();
        //Log.d("Future", ""+weatherConditionList.size());


        adapter = new WeatherAdapter(weatherConditionList);

        horizontalLayoutManager
                = new GridLayoutManager(MainActivity.this, 5);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.setAdapter(adapter);

        for (int i = 1 ; i <= 5; i++) {
            Observable<WeatherCondition> call = weatherInterface.getFuture(""+i);

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

            subscriptions.add(subscription);
            Log.d("Future"+i, ""+subscriptions.size());
        }
    }


    private void makeGetCurrentCall() {

        Call call = weatherInterface.getCurrent();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("Hello", ""+response);
                WeatherCondition weatherCondition = (WeatherCondition) response.body();
                Toast.makeText(getApplicationContext(), weatherCondition.getName(), Toast.LENGTH_LONG).show();
                Log.d("Hello", weatherCondition.getName());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Hello", t.getMessage());
                call.cancel();
            }
        });

    }

}
