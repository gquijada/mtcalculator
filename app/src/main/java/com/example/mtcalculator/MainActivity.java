package com.example.mtcalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialog;

import android.content.Context;
import android.icu.text.NumberFormat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    final Context context = this;

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private double purchaseAmount = 0.0;
    private double downPaymentAmount = 0.0;
    private double interestRate = 1.0;
    private double value = 0.0;
    private int duration = 1;

    private TextView textViewPurchasePrice;
    private TextView textViewDownPaymentAmount;
    private TextView textViewInterestRate;
    private TextView textViewDuration;
    private Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPurchasePrice = (TextView) findViewById(R.id.textViewPurchasePrice);
        textViewDownPaymentAmount = (TextView) findViewById(R.id.textViewDownPaymentAmount);
        textViewInterestRate = (TextView) findViewById(R.id.textViewInterestRate);
        textViewDuration = (TextView) findViewById(R.id.textViewDuration);

        EditText editTextPurchasePrice = (EditText) findViewById(R.id.editTextPurchasePrice);
        editTextPurchasePrice.addTextChangedListener(this.getEditableTextWatcher(textViewPurchasePrice, "PP"));

        EditText editTextDownPaymentAmount = (EditText) findViewById(R.id.editTextDouwnPaymentAmount);
        editTextDownPaymentAmount.addTextChangedListener(this.getEditableTextWatcher(textViewDownPaymentAmount, "DPA"));

        EditText editTextInterestRate = (EditText) findViewById(R.id.editTextInterestRate);
        editTextInterestRate.addTextChangedListener(this.getEditableTextWatcher(textViewInterestRate, "IR"));

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = progress;
                textViewDuration.setText(String.valueOf(progress));
                if (progress <= 0) {
                    seekBar.setProgress(1);
                    textViewDuration.setText(String.valueOf(1));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        calculate = (Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double loanAmount = purchaseAmount - downPaymentAmount;
                Loan loan = new Loan(interestRate, duration, loanAmount);

                final AppCompatDialog dialog = new AppCompatDialog(context);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Result");

                TextView monthlyPayment = (TextView) dialog.findViewById(R.id.monthly_payment);
                monthlyPayment.setText(currencyFormat.format(loan.getMonthlyPayment()));

                TextView totalPayment = (TextView) dialog.findViewById(R.id.total_payment);
                totalPayment.setText(currencyFormat.format(loan.getTotalPayment()));

                // reset loan data
                loan.setAnnualInterestRate(0.0);
                loan.setNumberOfYears(1);
                loan.setLoanAmount(0.0);

                Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public TextWatcher getEditableTextWatcher(final TextView textView, final String type) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    value = Double.parseDouble(s.toString()) / 100.0;
                    if (type.equals("PP")) {
                        purchaseAmount = value;
                        textView.setText(currencyFormat.format(value));
                    } else if (type.equals("DPA")) {
                        downPaymentAmount = value;
                        textView.setText(currencyFormat.format(value));
                    } else if (type.equals("IR")) {
                        interestRate = value;
                        textView.setText(String.valueOf(value));
                    }
                } catch (NumberFormatException e) {
                    textView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}