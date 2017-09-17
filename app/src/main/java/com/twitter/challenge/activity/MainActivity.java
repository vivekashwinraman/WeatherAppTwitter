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
import com.twitter.challenge.utils.StandardDevCalculator;
import com.twitter.challenge.utils.TemperatureConverter;

import java.text.DecimalFormat;
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
    private TextView tempStandardDevView;
    private Button button;
    private ImageView cloudView;
    private TextView titleView;
    private View dividerView;
    private List<View> list;
    private ArrayList<WeatherCondition> weatherConditionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationView = (TextView) findViewById(R.id.location);
        windSpeedView = (TextView) findViewById(R.id.wind_speed);
        temperatureView = (TextView) findViewById(R.id.temperature);
        cloudView = (ImageView) findViewById(R.id.cloud);
        titleView = (TextView) findViewById(R.id.list_title);
        tempStandardDevView = (TextView) findViewById(R.id.sd);
        dividerView = findViewById(R.id.divider);

        recyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        adapter = new WeatherAdapter(weatherConditionList);
        horizontalLayoutManager
                = new GridLayoutManager(MainActivity.this, 5);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
        button = (Button) findViewById(R.id.future_button);
        list = new ArrayList<View>() {{
            add(recyclerView);
            add(titleView);
            add(dividerView);
            add(tempStandardDevView);
        }};
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
                    showHideList(true);
                } else {
                    button.setText(R.string.show_future);
                    showHideList(false);
                }

            }
        });
        compositeSubscription = new CompositeSubscription();
        makeGetCurrentCall();
    }

    private void showHideList(boolean showHide) {
        for(View view : list) {
            view.setVisibility(showHide?  View.VISIBLE : View.GONE);
        }
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
        compositeSubscription.add(APIInteractor.getInstance().getCurrent().subscribe(new Subscriber<WeatherCondition>() {
            @Override
            public void onNext(WeatherCondition weatherCondition) {
                Toast.makeText(getApplicationContext(), weatherCondition.getName(), Toast.LENGTH_LONG).show();
                Log.d("Hello", weatherCondition.getName());

                DecimalFormat df = new DecimalFormat("##.#");
                String temperatureString =  String.format(getResources().getString(R.string.temperature),
                        df.format(weatherCondition.getWeather().getTemp()),
                        df.format(TemperatureConverter.celsiusToFahrenheit(weatherCondition.getWeather().getTemp())));

                locationView.setText(weatherCondition.getName());
                temperatureView.setText(temperatureString);
                windSpeedView.setText(String.format(getResources().getString(R.string.wind),weatherCondition.getWind().getSpeed()));
                if(weatherCondition.getClouds().getCloudiness() > 50) {
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
            compositeSubscription.add(APIInteractor.getInstance().getFuture(i).subscribe(new Subscriber<WeatherCondition>() {
                @Override
                public void onNext(WeatherCondition weatherCondition) {
                    Log.v("test", "day number : " + weatherCondition.getDay());
                    weatherConditionList.set(weatherCondition.getDay() - 1, weatherCondition);
                }

                @Override
                public void onCompleted() {
                    adapter.notifyDataSetChanged();
                    List<Float> stdDevs = new ArrayList<>();
                    for(WeatherCondition weatherCondition : weatherConditionList) {
                        stdDevs.add(weatherCondition.getWeather().getTemp());
                    }

                    String stdDevString =  String.format(getResources().getString(R.string.std_dev), StandardDevCalculator.calculate(stdDevs));
                    tempStandardDevView.setText(stdDevString);
                    tempStandardDevView.setVisibility(View.VISIBLE);
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
