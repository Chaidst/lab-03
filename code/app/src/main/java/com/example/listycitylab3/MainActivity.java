//package com.example.listycitylab3;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ArrayList<String> dataList;
//    private ListView cityList;
//    private ArrayAdapter<String> cityAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        String[] cities = {
//                "Edmonton", "Vancouver", "Moscow",
//                "Sydney", "Berlin", "Vienna",
//                "Tokyo", "Beijing", "Osaka", "New Delhi"
//        };
//
//        dataList = new ArrayList<>();
//        dataList.addAll(Arrays.asList(cities));
//
//        cityList = findViewById(R.id.city_list);
//        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
//        cityList.setAdapter(cityAdapter);
//    }
//}

package com.example.listycitylab3;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CustomArrayAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize cities with default provinces
        String[] cityNames = {
                "Edmonton", "Vancouver", "Moscow",
                "Sydney", "Berlin", "Vienna",
                "Tokyo", "Beijing", "Osaka", "New Delhi"
        };

        String[] provinces = {
                "Alberta", "British Columbia", "Russia",
                "New South Wales", "Germany", "Austria",
                "Japan", "China", "Japan", "India"
        };

        dataList = new ArrayList<>();
        for (int i = 0; i < cityNames.length; i++) {
            dataList.add(new City(cityNames[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CustomArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Add click listener for editing cities
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
            }
        });
    }

    private void showEditDialog(final int position) {
        City currentCity = dataList.get(position);

        // Create EditTexts for city name and province
        EditText cityNameEdit = new EditText(this);
        EditText provinceEdit = new EditText(this);

        cityNameEdit.setHint("City Name");
        provinceEdit.setHint("Province");
        cityNameEdit.setText(currentCity.getName());
        provinceEdit.setText(currentCity.getProvince());

        // Create a vertical layout for the dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextView cityLabel = new TextView(this);
        cityLabel.setText("City Name:");
        cityLabel.setPadding(0, 0, 0, 8);

        TextView provinceLabel = new TextView(this);
        provinceLabel.setText("Province:");
        provinceLabel.setPadding(0, 20, 0, 8);

        layout.addView(cityLabel);
        layout.addView(cityNameEdit);
        layout.addView(provinceLabel);
        layout.addView(provinceEdit);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit City")
                .setView(layout)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newCityName = cityNameEdit.getText().toString().trim();
                    String newProvince = provinceEdit.getText().toString().trim();

                    if (!newCityName.isEmpty()) {
                        // Update the city object with new values
                        dataList.get(position).setName(newCityName);
                        dataList.get(position).setProvince(newProvince);
                        cityAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // City class to hold city name and province
    public static class City {
        private String name;
        private String province;

        public City(String name, String province) {
            this.name = name;
            this.province = province;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }

    // Custom ArrayAdapter class
    private class CustomArrayAdapter extends ArrayAdapter<City> {

        public CustomArrayAdapter(android.content.Context context, ArrayList<City> cities) {
            super(context, 0, cities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            City city = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
            }

            // Find the TextView in the content layout
            TextView cityTextView = convertView.findViewById(R.id.content_view);

            // Display city name and province (province to the right)
            String displayText = city.getName();
            if (city.getProvince() != null && !city.getProvince().isEmpty()) {
                displayText += " - " + city.getProvince();
            }
            cityTextView.setText(displayText);

            return convertView;
        }
    }
}