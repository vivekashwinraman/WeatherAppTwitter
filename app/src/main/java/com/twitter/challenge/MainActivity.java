package com.twitter.challenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.challenge.model.WeatherCondition;
import com.twitter.challenge.network.WeatherClient;
import com.twitter.challenge.network.WeatherInterface;
import com.twitter.challenge.utils.TemperatureConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    WeatherInterface weatherInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherInterface = WeatherClient.getClient().create(WeatherInterface.class);
        makeGetCurrentCall();

        final TextView temperatureView = (TextView) findViewById(R.id.temperature);
        temperatureView.setText(getString(R.string.temperature, 34f, TemperatureConverter.celsiusToFahrenheit(34)));
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
