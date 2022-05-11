package me.joshlabue.helloble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private lateinit var layout: View

    private lateinit var bluetoothAdapter: BluetoothAdapter;
    private lateinit var bluetoothLeScanner: BluetoothLeScanner;
    private val handler = Handler();
    private var scanning = false;
    private val SCAN_PERIOD: Long = 10000;
    private val leDeviceListAdapter = LeDeviceListAdapter();

    private lateinit var recyclerView: RecyclerView;
//    private val deviceListAdapter = DeviceListAdapter()

    private lateinit var buttonPermsBT: Button;
    private lateinit var buttonPermsLocation: Button;

    private lateinit var buttonScanStart: Button;
    private lateinit var buttonScanStop: Button;

    private val adapter = DeviceListAdapter()

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            leDeviceListAdapter.addDevice(result.device);

            adapter.update(leDeviceListAdapter.getList())

        }

    };


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = view.findViewById(R.id.deviceList)

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.smoothScrollToPosition(0)

        recyclerView.adapter = adapter;

        buttonPermsBT = view.findViewById(R.id.btnBT)
        buttonPermsLocation = view.findViewById(R.id.btnLoc)
        buttonScanStart = view.findViewById(R.id.btnScan)
        buttonScanStop = view.findViewById(R.id.btnScanStop)


        buttonPermsBT.setOnClickListener {
            btnPermBT(it)
        }

        buttonPermsLocation.setOnClickListener {
            btnPermLocation(it)
        }

        buttonScanStart.setOnClickListener {
            btnScan()
        }

        buttonScanStop.setOnClickListener {
            btnScanStop()
        }

        return view;


    }



    private fun scanLeDevice() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner;


        if(!scanning) {
            Log.i("BLE: ", "Scanning for devices")

            handler.postDelayed({
                scanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
            }, SCAN_PERIOD)

            scanning = true;
            bluetoothLeScanner.startScan(leScanCallback)
        }
        else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }



    fun btnScan() {
        scanLeDevice();
    }

    fun btnScanStop() {
        scanning = true
        scanLeDevice();

    }

    fun btnPermBT(view: View) {
        (activity as MainActivity?)!!.permCallbackBT(view);
    }

    fun btnPermLocation(view: View) {
        (activity as MainActivity?)!!.permCallbackLocation(view);
    }

    private inner class DeviceListHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var bluetoothDevice: BluetoothDevice;

        private val labelTitle: TextView = view.findViewById(R.id.device_name);
        private val labelAddress: TextView = view.findViewById(R.id.device_address);

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {

        }

        fun bind(device: BluetoothDevice) {
            this.bluetoothDevice = device;

            labelTitle.text = this.bluetoothDevice.name?: "unnamed"
            labelAddress.text = this.bluetoothDevice.address

            Log.i("ListBind: ", this.bluetoothDevice.address)
        }
    }

    private inner class DeviceListAdapter(): RecyclerView.Adapter<DeviceListHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListHolder {
            val view = layoutInflater.inflate(R.layout.scan_list_entry, parent, false)
            return DeviceListHolder(view);
        }

        private var list: List<BluetoothDevice> = emptyList()

        fun update(newList: List<BluetoothDevice>) {
            this.list = newList
            //Log.i("Update", newList.size.toString())
            notifyDataSetChanged()
        }


        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: DeviceListHolder, position: Int) {
            Log.i("Bind", "binded")
            holder.bind(list[position]);
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MainFragment()
    }
}