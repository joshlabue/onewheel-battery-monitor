package me.joshlabue.helloble

import android.bluetooth.BluetoothDevice
import android.util.Log


class LeDeviceListAdapter {
    private var list = mutableListOf<BluetoothDevice>();

    fun getIndex(device: BluetoothDevice): Int {
        for(item in list) {
            if(item.address.equals(device.address)) return list.indexOf(item);
        }
        return -1;
    }

    fun addDevice(device: BluetoothDevice) {
        val index = getIndex(device);
        if(index == -1) {
            list.add(device);
            Log.i("BLE: ", "New device found: ${device.name ?: "Unnamed" }, ${device.address}");
        }
        else {
            list.set(index, device);
        }
    }

    fun getList(): MutableList<BluetoothDevice> {
        return list;
    }
}