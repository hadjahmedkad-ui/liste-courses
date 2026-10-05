package com.artkadvision.listecourses;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LinearLayout wantedList, purchases;
    EditText wanted, product, quantity, price, weight;
    TextView subtotal, total;
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wantedList = findViewById(R.id.wantedList);
        purchases = findViewById(R.id.purchases);

        wanted = findViewById(R.id.wanted);
        product = findViewById(R.id.product);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        weight = findViewById(R.id.weight);

        subtotal = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);

        findViewById(R.id.addwanted).setOnClickListener(v -> addWanted());
        findViewById(R.id.addpurchase).setOnClickListener(v -> confirmPurchase());
        findViewById(R.id.scan).setOnClickListener(v -> startScan());
        findViewById(R.id.reset).setOnClickListener(v -> confirmReset());

        refresh();
    }

    void addWanted() {
        String name = wanted.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Entre un produit", Toast.LENGTH_SHORT).show();
            return;
        }

        addRemovableRow(wantedList, name, 0);
        wanted.setText("");
    }

    void confirmPurchase() {
        String name = product.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Entre ou scanne un produit", Toast.LENGTH_SHORT).show();
            return;
        }

        double qty = num(quantity);
        double unitPrice = num(price);
        double kg = num(weight);

        if (qty <= 0 && kg <= 0) qty = 1;

        double amount;

        if (kg > 0) {
            amount = kg * unitPrice;
        } else {
            amount = qty * unitPrice;
        }

        String detail;

        if (kg > 0) {
            detail = name + " — " + fmt(kg) + " kg × "
                    + fmt(unitPrice) + " € = " + fmt(amount) + " €";
        } else {
            detail = name + " — " + fmt(qty) + " × "
                    + fmt(unitPrice) + " € = " + fmt(amount) + " €";
        }

        addRemovableRow(purchases, detail, amount);

        totalAmount += amount;
        refresh();

        product.setText("");
        quantity.setText("");
        price.setText("");
        weight.setText("");
    }

    void addRemovableRow(LinearLayout parent, String text, double amount) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(8, 12, 8, 12);

        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(18);
        label.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        Button delete = new Button(this);
        delete.setText("✕");

        delete.setOnClickListener(v -> {
            parent.removeView(row);

            if (amount > 0) {
                totalAmount -= amount;
                if (totalAmount < 0) totalAmount = 0;
                refresh();
            }
        });

        row.addView(label);
        row.addView(delete);
        parent.addView(row);
    }

    void refresh() {
        subtotal.setText("Sous-total : " + fmt(totalAmount) + " €");
        total.setText("TOTAL : " + fmt(totalAmount) + " €");
    }

    void confirmReset() {
        new AlertDialog.Builder(this)
                .setTitle("Réinitialiser")
                .setMessage("Effacer tous les achats, le sous-total et le total ?")
                .setPositiveButton("Oui", (dialog, which) -> resetAll())
                .setNegativeButton("Non", null)
                .show();
    }

    void resetAll() {
        purchases.removeAllViews();
        totalAmount = 0;
        refresh();
    }

    void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scanne le code-barres du produit");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    double num(EditText editText) {
        try {
            String value = editText.getText().toString()
                    .trim()
                    .replace(",", ".");

            if (value.isEmpty()) return 0;

            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    String fmt(double value) {
        return String.format(Locale.FRANCE, "%.2f", value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                product.setText(result.getContents());
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
                       }
