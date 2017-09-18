package com.twitter.challenge.network;

import com.twitter.challenge.model.WeatherCondition;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class APIInteractor {
    private volatile static APIInteractor instance = null;
    private WeatherInterface weatherInterface;

    private APIInteractor() {
        this.weatherInterface = WeatherClient.getClient().create(WeatherInterface.class);
    }

    public static APIInteractor getInstance() {
        if (instance == null) {
            synchronized (APIInteractor.class) {
                if (instance == null) {
                    instance = new APIInteractor();
                }
            }
        }
        return instance;
    }


    public Observable<WeatherCondition> getFuture(final int dayNumber) {
        return Observable.create(new Observable.OnSubscribe<WeatherCondition>() {
            @Override
            public void call(final Subscriber<? super WeatherCondition> subscriber) {
                try {
                    Call<WeatherCondition> call = weatherInterface.getFuture(dayNumber + "");
                    call.enqueue(new Callback<WeatherCondition>() {
                        @Override
                        public void onResponse(Call<WeatherCondition> call, Response<WeatherCondition> response) {
                            WeatherCondition weatherCondition = response.body();
                            weatherCondition.setDay(dayNumber);
                            subscriber.onNext(weatherCondition);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onFailure(Call<WeatherCondition> call, Throwable t) {
                            t.printStackTrace();
                            subscriber.onError(t);
                        }
                    });
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WeatherCondition> getCurrent(){
        return Observable.create(new Observable.OnSubscribe<WeatherCondition>() {
            @Override
            public void call(final Subscriber<? super WeatherCondition> subscriber) {
                try {
                    Call<WeatherCondition> call = weatherInterface.getCurrent();
                    call.enqueue(new Callback<WeatherCondition>() {
                        @Override
                        public void onResponse(Call<WeatherCondition> call, Response<WeatherCondition> response) {
                            WeatherCondition weatherCondition = response.body();
                            subscriber.onNext(weatherCondition);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onFailure(Call<WeatherCondition> call, Throwable t) {
                            t.printStackTrace();
                            subscriber.onError(t);
                        }
                    });
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
