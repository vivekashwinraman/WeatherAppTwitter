package com.twitter.challenge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.challenge.R;
import com.twitter.challenge.adapters.WeatherAdapter;
import com.twitter.challenge.model.Clouds;
import com.twitter.challenge.model.Weather;
import com.twitter.challenge.model.WeatherCondition;
import com.twitter.challenge.model.Wind;
import com.twitter.challenge.network.APIInteractor;
import com.twitter.challenge.utils.TemperatureConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager horizontalLayoutManager;
    private WeatherAdapter adapter;
    protected CompositeSubscription compositeSubscription;
    private TextView locationView;
    private TextView temperatureView;
    private TextView windSpeedView;
    private Button button;
    private ImageView cloudView;
    private TextView titleView;
    private View dividerView;
    private APIInteractor apiInteractor;


    private final ArrayList<WeatherCondition> weatherConditionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationView = (TextView) findViewById(R.id.location);
        windSpeedView = (TextView) findViewById(R.id.wind_speed);
        temperatureView = (TextView) findViewById(R.id.temperature);
        cloudView = (ImageView) findViewById(R.id.cloud);
        titleView = (TextView) findViewById(R.id.list_title);
        dividerView = findViewById(R.id.divider);

        recyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        apiInteractor = APIInteractor.getInstance();
        adapter = new WeatherAdapter(weatherConditionList);
        horizontalLayoutManager
                = new GridLayoutManager(MainActivity.this, 5);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
        button = (Button) findViewById(R.id.future_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!recyclerView.isShown()) {
                    if (weatherConditionList.size() < 5) {
                        weatherConditionList.clear();
                        adapter.notifyDataSetChanged();
                        makeGetFutureCall();
                    }
                    button.setText(R.string.hide_future);
                    recyclerView.setVisibility(View.VISIBLE);
                    titleView.setVisibility(View.VISIBLE);
                    dividerView.setVisibility(View.VISIBLE);
                } else {
                    button.setText(R.string.show_future);
                    recyclerView.setVisibility(View.GONE);
                    titleView.setVisibility(View.GONE);
                    dividerView.setVisibility(View.GONE);
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

    private void makeGetCurrentCall() {
        compositeSubscription.add(apiInteractor.getCurrent().subscribe(new Subscriber<WeatherCondition>() {
            @Override
            public void onNext(WeatherCondition weatherCondition) {
                Toast.makeText(getApplicationContext(), weatherCondition.getName(), Toast.LENGTH_LONG).show();
                Log.d("Hello", weatherCondition.getName());
                char deg = (char) 0x00B0;
                String temperatureString = String.valueOf(Math.round(weatherCondition.getWeather().getTemp())) + deg + "F/ "
                        + TemperatureConverter.celsiusToFahrenheit(Math.round(weatherCondition.getWeather().getTemp())) + deg + "C";
                locationView.setText(weatherCondition.getName());
                temperatureView.setText(temperatureString);
                windSpeedView.setText("Wind Speed " + String.valueOf(weatherCondition.getWind().getSpeed()));
                if (weatherCondition.getClouds().getCloudiness() > 50) {
                    cloudView.setImageResource(R.mipmap.rain);
                } else {
                    cloudView.setImageResource(R.mipmap.sun);

                }
            }

            @Override
            public void onCompleted() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    HttpException response = (HttpException) e;
                    Log.d("RetrofitTest", "Error code: " + response.code());
                }
            }
        }));
    }


    private void makeGetFutureCall() {
        weatherConditionList.addAll(initialiseFutureList());
        adapter.notifyDataSetChanged();

        for (int i = 1; i <= 5; i++) {
            compositeSubscription.add(apiInteractor.getFuture(i).subscribe(new Subscriber<WeatherCondition>() {
                @Override
                public void onNext(WeatherCondition weatherCondition) {
                    Log.v("test", "day number : " + weatherCondition.getDay());
                    weatherConditionList.set(weatherCondition.getDay()-1, weatherCondition);
                }

                @Override
                public void onCompleted() {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof HttpException) {
                        HttpException response = (HttpException) e;
                        Log.d("RetrofitTest", "Error code: " + response.code());
                    }
                }
            }));
        }

    }

    private List<WeatherCondition> initialiseFutureList() {

        List<WeatherCondition> emptyWeatherConditionList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            emptyWeatherConditionList.add(new WeatherCondition(null, new Weather(0.0f, 0, 0), new Wind("", 0), null, new Clouds(0), ""));
        }
        return  emptyWeatherConditionList;

    }
}
