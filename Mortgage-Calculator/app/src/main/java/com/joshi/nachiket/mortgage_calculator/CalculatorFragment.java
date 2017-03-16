package com.joshi.nachiket.mortgage_calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abc on 14-Mar-17.
 */

public class CalculatorFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    DatabaseAdaptor databaseAdaptor;
    EditText city_name, pin_code, mortgage_amount, interest_rate, street_address, output;
    Spinner spinner, spinner_property, spinner_state_list;
    int mortgage_period;
    String property_type, state;
    Button calculate, addLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_layout_a,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get all the relavant text box values in variables
        calculate = (Button) getActivity().findViewById(R.id.calc);
        addLocation = (Button) getActivity().findViewById(R.id.add_location);
        city_name = (EditText) getActivity().findViewById(R.id.et_city_name);
        pin_code = (EditText) getActivity().findViewById(R.id.et_pin_code);
        mortgage_amount = (EditText) getActivity().findViewById(R.id.et_mortgage_amount);
        interest_rate = (EditText) getActivity().findViewById(R.id.et_interest_rate);
        street_address = (EditText) getActivity().findViewById(R.id.et_street_address);
        output = (EditText) getActivity().findViewById(R.id.et_output);

        databaseAdaptor = new DatabaseAdaptor(getActivity().getApplicationContext());

        //you mortgage terms spinner
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        spinner_property = (Spinner) getActivity().findViewById(R.id.spinner_property);
        spinner_state_list = (Spinner) getActivity().findViewById(R.id.spinner_state_list);

        //Create an adaptor for youe spinner
        ArrayAdapter adaptor_mortgage_terms = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.mortgage_terms, android.R.layout.simple_spinner_item);
        ArrayAdapter adaptor_property_type = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.property_type, android.R.layout.simple_spinner_item);
        ArrayAdapter adaptor_state_list = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.state_list, android.R.layout.simple_spinner_item);

        spinner.setAdapter(adaptor_mortgage_terms);
        spinner_property.setAdapter(adaptor_property_type);
        spinner_state_list.setAdapter(adaptor_state_list);

        spinner.setOnItemSelectedListener(this);
        spinner_property.setOnItemSelectedListener(this);
        spinner_state_list.setOnItemSelectedListener(this);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateZip(pin_code.getText().toString());
            }
        });
        //SQLiteDatabase sqLiteDatabase = databaseAdaptor.getWritableDatabase();
    }

    public void addUser () {

        String mort_amt = mortgage_amount.getText().toString();
        String int_rate = interest_rate.getText().toString();
        //Already have mortgage_period accessible here
        String monthly_pay = output.getText().toString();
        //Already have prop_type accessible here
        String street_addr = street_address.getText().toString();
        String cityName = city_name.getText().toString();
        //Already have city accessible here
        String zip = pin_code.getText().toString();

        long id = databaseAdaptor.insertData(cityName, zip);

        if (id < 0) {
            Message.message(getActivity().getApplicationContext(), "FAILURE TO INSERT!");
        } else {
            Message.message(getActivity().getApplicationContext(), "SUCCESS");
        }
    }

    public void calculate () {

        double mort_amount = Double.parseDouble(mortgage_amount.getText().toString());
        double monthly_paymeny = ((mortgage_period) * 12);
        double rate = (Double.parseDouble(interest_rate.getText().toString()) / 1200.00);
        double part_one = Math.pow((rate + 1), monthly_paymeny);
        double numerator = mort_amount * part_one * rate;
        double denominator = part_one - 1;
        double answer = numerator / denominator;

        output.setText(answer + "");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()) {
            case R.id.spinner:
                mortgage_period = Integer.parseInt(spinner.getSelectedItem().toString());
                break;
            case R.id.spinner_state_list:
                state = spinner_state_list.getSelectedItem().toString();
                break;
            case R.id.spinner_property:
                property_type = spinner_property.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Message.message(getActivity().getApplicationContext(), "SUCCESS");
    }

    public void validateZip(String zip) {
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://www.yaddress.net/api/address?AddressLine1=&AddressLine2="+zip;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("ErrorCode") == 0 &&
                                    response.get("City").toString().trim().length() > 0 &&
                                    response.get("State").toString().trim().length() > 0 &&
                                    response.get("Zip").toString().trim().length() > 0 &&
                                    (double) response.get("Latitude")!=0.00 &&
                                    (double) response.get("Longitude")!=0.00 ) {
                                addUser();
                            } else {
                                Message.message(getActivity().getApplicationContext(),"Invalid Address");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("REGI", "Error: " + error.getMessage());
            }
        });
        ExampleRequestQueue.add(request);
    }
}
