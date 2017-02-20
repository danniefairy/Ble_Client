/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.webbluetoothcg.bletestperipheral;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeartRateServiceFragment extends ServiceFragment  {

  /**
   * See <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.heart_rate.xml">
   * Heart Rate Service</a>
   */
  //改UUID也會改變在MASTER上的名稱
  //https://kunyichen.wordpress.com/2007/06/08/bluetooth-service-uuid-list/
  private static final UUID HEART_RATE_SERVICE_UUID = UUID
      .fromString("0000180D-0000-1000-9000-00805f9b34fb");

  /**
   * See <a href="https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml">
   * Heart Rate Measurement</a>
   */
  private static final UUID HEART_RATE_MEASUREMENT_UUID = UUID
      .fromString("00002A37-0000-1000-9000-00805f9b34fb");


  /**
   * See <a href="https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.body_sensor_location.xml">
   * Body Sensor Location</a>
   */
  //UUID要改成自己可用的 發送
  private static final UUID BODY_SENSOR_LOCATION_UUID = UUID
      .fromString("10101010-1010-1010-1010-10101010101b");


  /**
   * See <a href="https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_control_point.xml">
   * Heart Rate Control Point</a>
   */
  //UUID要改成自己可用的 接收
  private static final UUID HEART_RATE_CONTROL_POINT_UUID = UUID
      .fromString("20202020-2020-2020-2020-2020202020b");

  private BluetoothGattService mHeartRateService;
  private BluetoothGattCharacteristic mHeartRateMeasurementCharacteristic;
  private BluetoothGattCharacteristic mBodySensorLocationCharacteristic;
  private BluetoothGattCharacteristic mHeartRateControlPoint;

  private ServiceFragmentDelegate mDelegate;









  public HeartRateServiceFragment() {
    mHeartRateMeasurementCharacteristic =
        new BluetoothGattCharacteristic(HEART_RATE_MEASUREMENT_UUID,
            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
             0);


    //notify
    mBodySensorLocationCharacteristic =
        new BluetoothGattCharacteristic(BODY_SENSOR_LOCATION_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ|BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ);
    mBodySensorLocationCharacteristic.addDescriptor(
            Peripheral.getClientCharacteristicConfigurationDescriptor());

    mHeartRateControlPoint =
        new BluetoothGattCharacteristic(HEART_RATE_CONTROL_POINT_UUID,
            BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_WRITE);

    mHeartRateService = new BluetoothGattService(HEART_RATE_SERVICE_UUID,
        BluetoothGattService.SERVICE_TYPE_PRIMARY);

    mHeartRateService.addCharacteristic(mBodySensorLocationCharacteristic);
    mHeartRateService.addCharacteristic(mHeartRateControlPoint);
  }

  //在這裡註冊元件
  //----
  String gettext="";
  //----
  //---累加
  List<String> value = new ArrayList<String>();
  //---
  //---switch
  private Switch mySwitch;
  //---
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    final View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
    //spinner
    final Spinner spinner = (Spinner) view.findViewById(R.id.spinnner);
    //spinner
    //switch
    mySwitch = (Switch) view.findViewById(R.id.mySwitch);
    //switch
    //----
    final Button button = (Button) view.findViewById(R.id.bt);
    button.setOnClickListener(buttoninput);
    final EditText editText = (EditText) view.findViewById(R.id.et);
    final EditText editText2 = (EditText) view.findViewById(R.id.edaccount);
    String a="@";
    gettext = editText2.getText().toString()+a+editText.getText().toString();
    //----
    //接收從ACTIVITY傳過來的資料
    if (getArguments() != null) {
      String title = "";
      String message = "";
      //這裡接收到的資料
      final String str = (String) getArguments().get("str");
      Log.d("tag", "@@@" + str);
      //取第一個值-----
      final char c0 = str.charAt(str.length() - 1);
      if (String.valueOf(c0).equals("k"))
      {
        title = "Authentication";
        message = "Success to log in";
        mBodySensorLocationCharacteristic.setValue("l");
        mDelegate.sendNotificationToDevices(mBodySensorLocationCharacteristic);
      }
      //取第一個值-----
        final char c = str.charAt(0);
        if (String.valueOf(c).equals("p")) {
          title = "Authentication";
          message = "Insert your password!";
        } else if (String.valueOf(c).equals("l")) {
          title = "Management";
          message = "Please choose the equipments!";
        }
        else if(String.valueOf(str.charAt(str.length() - 1)).equals("k")){
          //do nothing
          //password
          button.setVisibility(View.GONE);
          editText.setVisibility(View.GONE);
          editText2.setVisibility(View.GONE);
        } else {
          title = "Unknown";
          message = "Unknown information!";
        }
        //建立一個ArrayAdapter物件，並放置下拉選單的內容

        //sensor name
        value.add("sensor0");
        value.add("sensor1");
        //value.add("sensor2");
        //value.add("sensor3");
        //value.add("sensor4");

        //抓k尾巴 回傳l
        final ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, value);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title) //
                .setMessage(message) //
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    // TODO
                    dialog.dismiss();
                    if (!String.valueOf(c).equals("p"))//password
                    {
                      button.setVisibility(View.GONE);
                      editText.setVisibility(View.GONE);
                      editText2.setVisibility(View.GONE);
                      mySwitch.setVisibility((View.GONE));
                    }
                    //spin---
                    if (String.valueOf(c).equals("l")) {
                      //設定下拉選單的樣式
                      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spinner.setAdapter(adapter);
                      //設定項目被選取之後的動作
                      spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        public void onItemSelected(final AdapterView adapterView, View view, int position, long id) {
                          //switch
                          mySwitch.setVisibility(View.VISIBLE);


                          //這裡面再放東西 switch?
                          final String d="d";
                          mySwitch.setText(adapterView.getSelectedItem().toString());
                          mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                              if (isChecked) {
                                Log.d("tag", "@@~~on~~");
                                //這裡改輸出
                                //String output = adapterView.getSelectedItem().toString() + " Turn On";
                                String output = d+String.valueOf(adapterView.getSelectedItemPosition()) + "N";
                                mBodySensorLocationCharacteristic.setValue(output);
                                mDelegate.sendNotificationToDevices(mBodySensorLocationCharacteristic);

                              } else {
                                Log.d("tag", "@@~~off~~");
                                //這裡改輸出
                                //String output = adapterView.getSelectedItem().toString() + " Turn Off";
                                String output = d+String.valueOf(adapterView.getSelectedItemPosition())+ "F";
                                mBodySensorLocationCharacteristic.setValue(output);
                                mDelegate.sendNotificationToDevices(mBodySensorLocationCharacteristic);
                              }
                            }
                          });
                          //這裡面再放東西 switch?
                        }
                        public void onNothingSelected(AdapterView arg0) {

                        }
                      });
                    } else {
                      spinner.setVisibility(View.GONE);
                      if (String.valueOf(c).equals("p"))
                        mySwitch.setVisibility(View.GONE);
                    }
                    //spin---
                  }
                }) ;//不需要cancel
                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    // TODO
                    //password
                    button.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    //spinner
                    spinner.setVisibility(View.GONE);
                    //switch
                    mySwitch.setVisibility(View.GONE);
                    dialog.dismiss();
                  }
                });*/
        builder.show();
        //---
      } else {
        //password
        button.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        editText2.setVisibility(View.GONE);
        //spinner
        spinner.setVisibility(View.GONE);
        //switch
        mySwitch.setVisibility(View.GONE);
      }
  //}




    return view;
  }



  //-----
  //輸入
  private final OnClickListener buttoninput = new OnClickListener() {
    @Override
    public void onClick(View v) {
      TextView text= (TextView)getView().findViewById(R.id.et);
      TextView text2= (TextView)getView().findViewById(R.id.edaccount);
      String a="@";
      //p開頭回傳
      gettext=("p"+text2.getText().toString()+a+text.getText()).toString();
      Log.d("test", "gettext=" + gettext);
      mBodySensorLocationCharacteristic.setValue(gettext.getBytes());
      mDelegate.sendNotificationToDevices(mBodySensorLocationCharacteristic);

      getView().setVisibility(View.GONE);
      text.setVisibility(View.GONE);
      text2.setVisibility(View.GONE);
    }
  };
  //-----

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mDelegate = (ServiceFragmentDelegate) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement ServiceFragmentDelegate");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mDelegate = null;
  }

  @Override
  public BluetoothGattService getBluetoothGattService() {
    return mHeartRateService;
  }

  @Override
  public ParcelUuid getServiceUUID() {
    return new ParcelUuid(HEART_RATE_SERVICE_UUID);
  }






  @Override
  public int writeCharacteristic(BluetoothGattCharacteristic characteristic, int offset, byte[] value) {
    if (offset != 0) {
      return BluetoothGatt.GATT_INVALID_OFFSET;
    }
    // Heart Rate control point is a 8bit characteristic
    if (value.length != 1) {
      return BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH;
    }
    if ((value[0] & 1) == 1) {
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {

        }
      });
    }
    return BluetoothGatt.GATT_SUCCESS;
  }
}
