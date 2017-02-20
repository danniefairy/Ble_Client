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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;


public class Peripherals extends ListActivity {

  // TODO(g-ortuno): Implement heart rate monitor peripheral
  public final static String EXTRA_PERIPHERAL_INDEX = "PERIPHERAL_INDEX";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = new Intent(this, Peripheral.class);
    intent.putExtra(EXTRA_PERIPHERAL_INDEX, 1);
    startActivity(intent);
    finish();
  }


}
