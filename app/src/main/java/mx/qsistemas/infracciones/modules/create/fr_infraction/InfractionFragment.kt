package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentInfractionBinding
import mx.qsistemas.infracciones.db.entities.RetainedDocument
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.Utils.Companion.bitmapDescriptorFromVector

private const val ARG_IS_CREATION = "is_creation"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InfractionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InfractionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InfractionFragment : Fragment(), InfractionContracts.Presenter, AdapterView.OnItemSelectedListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, OnMapReadyCallback {
    private var isCreation: Boolean = true
    private val iterator = lazy { InfractionIterator(this) }
    private var map: GoogleMap? = null
    private var hasObtainedMaxAccuracy = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var activity: CreateInfractionActivity
    private lateinit var binding: FragmentInfractionBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as CreateInfractionActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isCreation = it.getBoolean(ARG_IS_CREATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_infraction, container, false)
        /*val map = childFragmentManager.findFragmentById(R.id.map_infraction_location) as SupportMapFragment
        map.getMapAsync(this)*/
        initAdapters()
        fillFields()
        startLocationListener()
        return binding.root
    }

    override fun initAdapters() {
        binding.rcvArticles.layoutManager = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        /* Init listener of components*/
        binding.spnArticle.onItemSelectedListener = this
        binding.spnFraction.onItemSelectedListener = this
        binding.spnRetainedDoc.onItemSelectedListener = this
        binding.spnDisposition.onItemSelectedListener = this
        binding.rdbReferralYes.setOnCheckedChangeListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.edtColony.doOnTextChanged { text, start, count, after -> SingletonInfraction.colonnyInfraction = text?.trim().toString().toUpperCase() }
        binding.edtStreet.doOnTextChanged { text, start, count, after -> SingletonInfraction.streetInfraction = text?.trim().toString().toUpperCase() }
        binding.edtBetweenStreet1.doOnTextChanged { text, start, count, after -> SingletonInfraction.betweenStreet1 = text?.trim().toString().toUpperCase() }
        binding.edtBetweenStreet2.doOnTextChanged { text, start, count, after -> SingletonInfraction.betweenStreet2 = text?.trim().toString().toUpperCase() }
        /* Init adapters */
        binding.spnArticle.adapter = iterator.value.getArticlesAdapter()
        binding.spnRetainedDoc.adapter = iterator.value.getRetainedDocAdapter()
        binding.spnDisposition.adapter = iterator.value.getDispositionAdapter()
        binding.rcvArticles.adapter = MotivationAdapter()
    }

    override fun fillFields() {
        binding.edtColony.setText(SingletonInfraction.colonnyInfraction)
        binding.edtStreet.setText(SingletonInfraction.streetInfraction)
        binding.edtBetweenStreet1.setText(SingletonInfraction.betweenStreet1)
        binding.edtBetweenStreet2.setText(SingletonInfraction.betweenStreet2)
        if (SingletonInfraction.isRemited) {
            binding.rdbReferralYes.isChecked = true
        } else {
            binding.rdbReferralNo.isChecked = true
        }
        binding.spnRetainedDoc.setSelection(iterator.value.getPositionRetainedDoc(SingletonInfraction.retainedDocument))
        binding.spnDisposition.setSelection(iterator.value.getPositionDisposition(SingletonInfraction.dispositionRemited))
    }

    @SuppressLint("MissingPermission")
    override fun startLocationListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback(), GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(p0: Marker?) {
                if (p0 != null) {
                    var newLocation = Location("newLocation")
                    newLocation.latitude = p0.position.latitude
                    newLocation.longitude = p0.position.longitude
                    val distanceBetween = lastLocation.distanceTo(newLocation)
                    if (distanceBetween <= lastLocation.accuracy) {
                        lastLocation.latitude = p0.position.latitude
                        lastLocation.longitude = p0.position.longitude
                        iterator.value.getAddressFromCoordinates(lastLocation.latitude, lastLocation.longitude)
                    } else {
                        onError(getString(R.string.e_marker_too_distance))
                        val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)
                        val circlePos = CircleOptions().center(latlng).radius(lastLocation.accuracy.toDouble())
                                .fillColor(ContextCompat.getColor(Application.getContext(), R.color.colorPrimaryTransparent))
                                .strokeColor(ContextCompat.getColor(Application.getContext(), R.color.colorPrimaryDark))
                        circlePos.strokeWidth(5F)
                        map?.clear()
                        map?.addMarker(
                                MarkerOptions().position(latlng).title("Mi Ubicación")
                                        .icon(bitmapDescriptorFromVector(activity, R.drawable.ic_place))
                        )?.isDraggable = true
                        map?.setOnMarkerDragListener(this)
                        map?.addCircle(circlePos)
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18F))
                        iterator.value.getAddressFromCoordinates(lastLocation.latitude, lastLocation.longitude)
                    }
                }
            }

            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }

            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                lastLocation = locationResult.lastLocation
                SingletonInfraction.latitudeInfraction = lastLocation.latitude
                SingletonInfraction.longitudeInfraction = lastLocation.longitude
                iterator.value.getAddressFromCoordinates(lastLocation.latitude, lastLocation.longitude)
                /*if (map != null) {
                    val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)
                    if (lastLocation.accuracy in MIN_ACCURACY..MAX_ACCURACY && !hasObtainedMaxAccuracy) {
                        *//* Change value of hasObtainedMaxAccuracy to validate that max accuracy is already obtained *//*
                        hasObtainedMaxAccuracy = true
                        fusedLocationClient.removeLocationUpdates(this)
                        map?.clear()
                        val circlePos = CircleOptions().center(latlng).radius(lastLocation.accuracy.toDouble())
                                .fillColor(ContextCompat.getColor(Application.getContext(), R.color.colorLightBlueTransparent))
                                .strokeColor(ContextCompat.getColor(Application.getContext(), R.color.colorPrimary))
                        circlePos.strokeWidth(5F)
                        map?.addMarker(MarkerOptions().position(latlng).title("Mi Ubicación")
                                .icon(bitmapDescriptorFromVector(activity, R.drawable.ic_place))
                        )?.isDraggable = true
                        map?.setOnMarkerDragListener(this)
                        map?.addCircle(circlePos)
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18F))
                        iterator.value.getAddressFromCoordinates(lastLocation.latitude, lastLocation.longitude)
                    }
                }*/
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.spnArticle.id -> {
                binding.spnFraction.adapter = iterator.value.getFractionAdapter(p2)
            }
            binding.spnRetainedDoc.id -> {
                if (p2 == 0) {
                    SingletonInfraction.retainedDocument = RetainedDocument(0, "NINGUNO")
                } else {
                    SingletonInfraction.retainedDocument = iterator.value.retainedDocList[p2]
                }
            }
            binding.spnDisposition.id -> {
                SingletonInfraction.dispositionRemited = iterator.value.dispositionList[p2]
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (!hasObtainedMaxAccuracy) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onAddressLocated(colony: String, street: String, betweenStreet: String, andStreet: String) {
        if (binding.edtColony.text.isEmpty()) binding.edtColony.setText(colony)
        if (binding.edtStreet.text.isEmpty()) binding.edtStreet.setText(street)
        if (binding.edtBetweenStreet1.text.isEmpty()) binding.edtBetweenStreet1.setText(betweenStreet)
        if (binding.edtBetweenStreet2.text.isEmpty()) binding.edtBetweenStreet2.setText(andStreet)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAdd.id -> {
                when {
                    iterator.value.articlesList[binding.spnArticle.selectedItemPosition].id.toInt() == 0 -> onError(getString(R.string.e_invalid_article))
                    fractionExist(iterator.value.fractionList[binding.spnFraction.selectedItemPosition].fraccion
                            , iterator.value.articlesList[binding.spnArticle.selectedItemPosition].article) -> SnackbarHelper.showErrorSnackBar(activity, "La fracción no se puede repetir.", Snackbar.LENGTH_LONG)
                    else -> {

                        iterator.value.saveNewArticle(binding.spnArticle.selectedItemPosition,
                                binding.spnFraction.selectedItemPosition)
                        binding.rcvArticles.adapter?.notifyDataSetChanged()
                        binding.spnFraction.setSelection(0)
                        binding.spnArticle.setSelection(0)
                    }
                }
            }
            binding.btnNext.id -> {
                if (validFields()) {
                    activity.stepUp()
                    activity.router.value.presentOffenderFragment(isCreation, Direction.NONE)
                }
            }
        }
    }

    fun fractionExist(fraction: String, article: String): Boolean {
        SingletonInfraction.motivationList.forEach { item ->
            if (item.fraction.fraccion == fraction && item.article.article == article) {
                return true
            }
        }
        return false
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        SingletonInfraction.isRemited = p1
        if (p1) {
            binding.txtDispositionTitle.visibility = VISIBLE
            binding.spnDisposition.visibility = VISIBLE
        } else {
            binding.txtDispositionTitle.visibility = GONE
            binding.spnDisposition.setSelection(0)
            binding.spnDisposition.visibility = GONE
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            map = p0
            map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
            map?.uiSettings?.isZoomControlsEnabled = false
            map?.uiSettings?.isMyLocationButtonEnabled = false
            map?.uiSettings?.isMapToolbarEnabled = false
            map?.setMinZoomPreference(17F)
            map?.setMaxZoomPreference(20F)
            /* If Singleton already has the location of incidence, then show in google map */
            if (SingletonInfraction.latitudeInfraction != 0.0 && SingletonInfraction.longitudeInfraction != 0.0) {
                val latlng = LatLng(SingletonInfraction.latitudeInfraction, SingletonInfraction.longitudeInfraction)
                map?.addMarker(MarkerOptions().position(latlng).icon(bitmapDescriptorFromVector(activity, R.drawable.ic_place)))
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19F))
            }
        }
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
    }


    override fun validFields(): Boolean {
        var isValid = true
        when {
            SingletonInfraction.colonnyInfraction.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_colonny))
            }
            SingletonInfraction.streetInfraction.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_street))
            }
            SingletonInfraction.motivationList.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_articles_empty))
            }
            SingletonInfraction.isRemited && SingletonInfraction.dispositionRemited.id == 0 -> {
                isValid = false
                onError(getString(R.string.e_disposition_remitted))
            }
            SingletonInfraction.motivationList.isNotEmpty() -> {
                SingletonInfraction.motivationList.forEach {
                    if (it.motivation.trim().isEmpty()) {
                        onError("Artículo ${it.article.article}: " + getString(R.string.e_motivation_empty))
                        return false
                    }
                }
            }
            SingletonInfraction.retainedDocument.id == 0 -> {
                isValid = false
                onError(getString(R.string.e_retained_doc))
            }
        }
        return isValid
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isCreation Parameter 1.
         * @return A new instance of fragment InfractionFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                InfractionFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
