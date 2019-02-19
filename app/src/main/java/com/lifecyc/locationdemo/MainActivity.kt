package com.lifecyc.locationdemo

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity(), PermissionUtils.PermissionCallbacks {


    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val REQUEST_PERMISSION_CODE = 12


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun doClick(view:View?) {

        if (!PermissionUtils.hasPermissions(this, *permissions)) {
            PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, permissions)
        } else {


            LocationUtils.register(this, 8000000, 0, object : LocationUtils.OnLocationChangeListener {
                override fun getLastKnownLocation(location: Location?) {
                    Log.e("xyh", "onLocationChanged: " + location?.latitude)
                }

                override fun onLocationChanged(location: Location?) {
                    location?.apply {


                        //位置信息变化时触发
                        Log.e("xyh", "定位方式：" + location.provider)
                        Log.e("xyh", "纬度：" + location.latitude)
                        Log.e("xyh", "经度：" + location.longitude)
                        Log.e("xyh", "海拔：" + location.altitude)
                        Log.e("xyh", "时间：" + location.time)
                        Log.e(
                            "xyh",
                            "国家：" + LocationUtils.getCountryName(
                                this@MainActivity,
                                location.latitude,
                                location.longitude
                            )
                        )
                        Log.e(
                            "xyh",
                            "获取地理位置：" + LocationUtils.getAddress(
                                this@MainActivity,
                                location.latitude,
                                location.longitude
                            )
                        )
                        Log.e(
                            "xyh",
                            "所在地：" + LocationUtils.getLocality(this@MainActivity, location.latitude, location.longitude)
                        )
                        Log.e(
                            "xyh",
                            "所在街道：" + LocationUtils.getStreet(this@MainActivity, location.latitude, location.longitude)
                        )
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }
            })
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsAllGranted(requestCode: Int, perms: MutableList<String>?, isAllGranted: Boolean) {
        if (isAllGranted) {
            doClick(null)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        if (PermissionUtils.somePermissionPermanentlyDenied(this, perms!!)) {
            PermissionUtils.showDialogGoToAppSettting(this)
        } else {
            PermissionUtils.showPermissionReason(requestCode, this, permissions, "需要定位权限")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        LocationUtils.unregister()
    }

}
