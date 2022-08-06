package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity

class MyPin : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private var mMap: GoogleMap? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mLocationRequest: LocationRequest? = null
    var mapFragment: SupportMapFragment? = null
    private val REQUEST_CODE = 11
    var fStore: FirebaseFirestore? = null
    var fAuth: FirebaseAuth? = null
    private var cloudstorage: FirebaseFirestore? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pin)
        fAuth = FirebaseAuth.getInstance()
        mapFragment =
            getSupportFragmentManager().findFragmentById(R.id.google_map) as SupportMapFragment
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment.getMapAsync(this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
    }

    @Override
    fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        buildGoogleApiClient()
        mMap.setMyLocationEnabled(true)
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient.connect()
    }

    @Override
    fun onLocationChanged(@NonNull location: Location) {
        mLastLocation = location
        showLocation()
        val latLng = LatLng(location.getLatitude(), location.getLongitude())
        val markerOptions1: MarkerOptions = MarkerOptions().position(latLng).title("You are here")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.addMarker(markerOptions1).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15))
        mMap.addMarker(markerOptions1).showInfoWindow()
    }

    fun showLocation() {
        cloudstorage = FirebaseFirestore.getInstance()
        cloudstorage.collection("user data")
            .get()
            .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?>() {
                @Override
                fun onComplete(@NonNull task: Task<QuerySnapshot?>) {
                    if (task.isSuccessful()) {
                        for (document in task.getResult()) {
                            Log.d(TAG, document.getId().toString() + " => " + document.getData()) //
                            if (document.contains("location") && document.contains("name") && document.contains(
                                    "description"
                                ) && document.contains("userid")
                            ) {
                                val location: GeoPoint = document.get("location") as GeoPoint
                                val title = document.get("name") as String
                                val type = document.get("type") as String
                                val description = document.get("description") as String
                                val phone = document.get("phone") as String
                                val Userid = document.get("userid") as String
                                val userID: String = fAuth.getCurrentUser().getUid()
                                if (type.equals("Donor") and Userid.equals(userID)) {
                                    Log.d(TAG, "$userID Success $title")
                                    val latLng =
                                        LatLng(location.getLatitude(), location.getLongitude())
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.addMarker(
                                        MarkerOptions().position(latLng)
                                            .title("$type-$title|$phone").snippet(description).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_GREEN
                                            )
                                        )
                                    )
                                } else if (type.equals("Receiver") and Userid.equals(userID)) {
                                    Log.d(
                                        TAG,
                                        String.valueOf(location).toString() + " Success " + title
                                    )
                                    val latLng =
                                        LatLng(location.getLatitude(), location.getLongitude())
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.addMarker(
                                        MarkerOptions().position(latLng)
                                            .title("$type-$title|$phone").snippet(description).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_BLUE
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "Error fetching data: ", task.getException())
                    }
                }
            })
    }

    @Override
    fun onConnected(@Nullable bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }

    @Override
    fun onConnectionSuspended(i: Int) {
    }

    @Override
    fun onConnectionFailed(@NonNull connectionResult: ConnectionResult?) {
    }

    @Override
    fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>?,
        @NonNull grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment.getMapAsync(this)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TAG = "TAG"
    }
}