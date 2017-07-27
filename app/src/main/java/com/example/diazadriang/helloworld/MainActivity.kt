package com.example.diazadriang.helloworld

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.util.Random
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
  val DICE_MIN_VALUE = 1
  val DICE_MAX_VALUE = 6

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    RxView.clicks(findViewById(R.id.button))
        .debounce(300, TimeUnit.MILLISECONDS)
        .map { findViewById(R.id.editText) as EditText }
        .map { it.text.toString() }
        .filter { it.isNotEmpty() }
        .map { Integer.parseInt(it) }
        .filter { it <= 5 }
        .flatMap { dicesCount ->
          Observable.range(0, dicesCount)
              .map { Random().nextInt(DICE_MAX_VALUE) + DICE_MIN_VALUE }
              .reduce("", { text, diceValue -> text + diceValue })
              .toObservable()
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(RxTextView.text(findViewById(R.id.text) as TextView))
  }
}
