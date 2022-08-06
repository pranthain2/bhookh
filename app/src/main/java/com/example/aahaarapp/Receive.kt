package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity

class Receive : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private var mMap: GoogleMap? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mLocationRequest: LocationRequest? = null
    private val REQUEST_CODE = 11
    var mapFragment: SupportMapFragment? = null
    var mFullName: EditText? = null
    var mDescription: EditText? = null
    var mPhone: EditText? = null
    var mSubmitBtn: Button? = null
    var fAuth: FirebaseAuth? = null
    var fStore: FirebaseFirestore? = null
    var userID: String? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)
        mFullName = findViewById(R.id.receivername)
        mDescription = findViewById(R.id.description)
        mSubmitBtn = findViewById(R.id.submit)
        mPhone = findViewById(R.id.phone)
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
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
        buildGoogleApiClient()
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
        val latLng = LatLng(location.getLatitude(), location.getLongitude())

        //MarkerOptions markerOptions1 = new MarkerOptions().position(latLng).title("You are here");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.addMarker(markerOptions1).showInfoWindow();
        val markerOptions: MarkerOptions = MarkerOptions().position(latLng).title("You are here")
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15))
        mMap.addMarker(markerOptions).showInfoWindow()
        mSubmitBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val fullname: String = mFullName.getText().toString().trim()
                val description: String = mDescription.getText().toString().trim()
                val phone: String = mPhone.getText().toString().trim()
                val type = "Receiver"
                if (TextUtils.isEmpty(fullname)) {
                    mFullName.setError("Name is Required.")
                    return
                }
                if (TextUtils.isEmpty(description)) {
                    mFullName.setError("Description is Required.")
                    return
                }
                if (phone.length() < 10) {
                    mFullName.setError("Phone Number Invalid.")
                    return
                }
                userID = fAuth.getCurrentUser().getUid()
                //DocumentReference documentReference = fStore.collection("receiver").document(userID);
                val collectionReference: CollectionReference = fStore.collection("user data")
                val geoPoint = GeoPoint(location.getLatitude(), location.getLongitude())
                val user: Map<String, Object> = HashMap()
                user.put("timestamp", FieldValue.serverTimestamp())
                user.put("name", fullname)
                user.put("phone", phone)
                user.put("description", description)
                user.put("location", geoPoint)
                user.put("userid", userID)
                user.put("type", type)
                collectionReference.add(user)
                    .addOnSuccessListener(object : OnSuccessListener<DocumentReference?>() {
                        @Override
                        fun onSuccess(documentReference: DocumentReference?) {
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, "Success!")
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            val intent = Intent(this@Receive, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    })
                    .addOnFailureListener(object : OnFailureListener() {
                        @Override
                        fun onFailure(@NonNull e: Exception?) {
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT)
                                .show()
                            Log.w(TAG, "Error!", e)
                        }
                    })
            }
        })
    }

    @Override
    fun onConnected(@Nullable bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        //mLocationRequest.setInterval(1000);
        //mLocationRequest.setFastestInterval(1000);
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