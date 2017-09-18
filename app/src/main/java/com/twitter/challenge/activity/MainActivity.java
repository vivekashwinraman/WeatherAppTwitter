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

import com.twitter.challenge.R;
import com.twitter.challenge.adapters.WeatherAdapter;
import com.twitter.challenge.model.Clouds;
import com.twitter.challenge.model.Weather;
import com.twitter.challenge.model.WeatherCondition;
import com.twitter.challenge.model.Wind;
import com.twitter.challenge.network.APIInteractor;
import com.twitter.challenge.utils.NetworkUtils;
import com.twitter.challenge.utils.StandardDevCalculator;
import com.twitter.challenge.utils.TemperatureConverter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
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
    private ArrayList<WeatherCondition> weatherConditionList;
    private String standardDeviation;
    private boolean forecastShown = false;
    private boolean currentWeatherLoaded = false;
    private boolean forecastLoaded = false;
    private WeatherCondition currentWeatherCondition;
    private static final String WEATHER_LIST_TAG = "weather_list";
    private static final String FORECAST_SHOWN_TAG = "forecast_shown";
    private static final String STD_DEVIATION = "std_deviation";
    private static final String CURRENT_WEATHER_CONDITION = "current_weather";
    private static final String CURRENT_WEATHER_LOADED = "current_loaded";
    private static final String FORECAST_LOADED = "forecast_loaded";
    private int FIVE_DAYS =5;


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
        layoutManager
                = new GridLayoutManager(MainActivity.this, FIVE_DAYS);
        recyclerView.setLayoutManager(layoutManager);
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
                    if (!forecastLoaded) {
                        weatherConditionList.clear();
                        adapter.notifyDataSetChanged();
                        makeGetFutureCall();
                    }
                    showHideList(true);
                } else {
                    showHideList(false);
                }

            }
        });
        if (savedInstanceState == null) {
            this.weatherConditionList = new ArrayList<>();
            this.forecastShown = false;
            this.standardDeviation = new String();
            this.currentWeatherCondition = null;
        } else {
            this.weatherConditionList = savedInstanceState.getParcelableArrayList(WEATHER_LIST_TAG);
            this.forecastShown = savedInstanceState.getBoolean(FORECAST_SHOWN_TAG);
            this.standardDeviation = savedInstanceState.getString(STD_DEVIATION);
            this.currentWeatherCondition = savedInstanceState.getParcelable(CURRENT_WEATHER_CONDITION);
            this.currentWeatherLoaded = savedInstanceState.getBoolean(CURRENT_WEATHER_LOADED);
            this.forecastLoaded = savedInstanceState.getBoolean(FORECAST_LOADED);
        }
        showHideList(forecastShown);
        adapter = new WeatherAdapter(weatherConditionList);
        recyclerView.setAdapter(adapter);

        compositeSubscription = new CompositeSubscription();
        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            cloudView.setImageResource(0);
            if (!currentWeatherLoaded) {
                makeGetCurrentCall();
            } else {
                populateCurrentWeatherCondition();
            }

        } else {
            Log.d(TAG, "Internet Connectivity Issues");
            if(currentWeatherLoaded) {
                populateCurrentWeatherCondition();
            } else {
                cloudView.setImageResource(R.mipmap.error_cloud);
            }
        }
    }

    private void showHideList(boolean showHide) {
        for (View view : list) {
            view.setVisibility(showHide ? View.VISIBLE : View.GONE);
        }
        forecastShown = showHide;
        button.setText(forecastShown ? R.string.hide_future : R.string.show_future);
        if (showHide) {
            tempStandardDevView.setVisibility(View.VISIBLE);
            tempStandardDevView.setText(standardDeviation);
        } else {
            tempStandardDevView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(WEATHER_LIST_TAG, weatherConditionList);
        bundle.putBoolean(FORECAST_SHOWN_TAG, forecastShown);
        bundle.putString(STD_DEVIATION, standardDeviation);
        bundle.putParcelable(CURRENT_WEATHER_CONDITION, currentWeatherCondition);
        bundle.putBoolean(CURRENT_WEATHER_LOADED, currentWeatherLoaded);
        bundle.putBoolean(FORECAST_LOADED, forecastLoaded);
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                currentWeatherCondition = weatherCondition;
                populateCurrentWeatherCondition();
            }

            @Override
            public void onCompleted() {
                currentWeatherLoaded = true;
            }
            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    HttpException response = (HttpException) e;
                    Log.d("TAG", "Error code: " + response.code());
                }
            }
        }));
    }


    private void makeGetFutureCall() {
        weatherConditionList.addAll(initialiseFutureList());
        adapter.notifyDataSetChanged();
        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            for (int i = 1; i <= FIVE_DAYS; i++) {
                compositeSubscription.add(APIInteractor.getInstance().getFuture(i).subscribe(new Subscriber<WeatherCondition>() {
                    @Override
                    public void onNext(WeatherCondition weatherCondition) {
                        Log.v(TAG, "day number : " + weatherCondition.getDay());
                        weatherConditionList.set(weatherCondition.getDay() - 1, weatherCondition);
                    }

                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                        List<Float> stdDevs = new ArrayList<>();
                        for (WeatherCondition condition : weatherConditionList) {
                            stdDevs.add(condition.getWeather().getTemp());
                        }
                        standardDeviation = String.format(getResources().getString(R.string.std_dev), StandardDevCalculator.calculate(stdDevs));
                        tempStandardDevView.setText(standardDeviation);
                        forecastLoaded = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            Log.d(TAG, "Error code: " + response.code());
                        }
                    }
                }));
            }
        }

    }

    private List<WeatherCondition> initialiseFutureList() {

        List<WeatherCondition> emptyWeatherConditionList = new ArrayList<>();
        for (int i = 0; i < FIVE_DAYS; i++) {
            emptyWeatherConditionList.add(new WeatherCondition(null, new Weather(0.0f, 0, 0), new Wind("", 0), null, new Clouds(0), ""));
        }
        return emptyWeatherConditionList;

    }

    private void populateCurrentWeatherCondition() {
        if (currentWeatherCondition != null) {
            DecimalFormat df = new DecimalFormat("##.#");
            String temperatureString = String.format(getResources().getString(R.string.temperature),
                    df.format(currentWeatherCondition.getWeather().getTemp()),
                    df.format(TemperatureConverter.celsiusToFahrenheit(currentWeatherCondition.getWeather().getTemp())));

            locationView.setText(currentWeatherCondition.getName());
            temperatureView.setText(temperatureString);
            windSpeedView.setText(String.format(getResources().getString(R.string.wind), currentWeatherCondition.getWind().getSpeed()));
            if (currentWeatherCondition.getClouds().getCloudiness() > 50) {
                cloudView.setImageResource(R.mipmap.rain);
            } else {
                cloudView.setImageResource(R.mipmap.sun);

            }
        }
    }
}
