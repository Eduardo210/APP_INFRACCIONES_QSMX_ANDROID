package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentVehicleBinding
import mx.qsistemas.infracciones.helpers.BitmapHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.RC_INTENT_CAMERA_EV1
import mx.qsistemas.infracciones.utils.RC_INTENT_CAMERA_EV2
import mx.qsistemas.infracciones.utils.RC_PERMISSION_CAMERA
import mx.qsistemas.infracciones.utils.Utils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_IS_CREATION = "is_creation"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [VehicleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [VehicleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VehicleFragment : Fragment(), VehicleContracts.Presenter, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var isCreation: Boolean = true
    private var pathPhoto = ""
    private val iterator = lazy { VehicleIterator(this) }
    private lateinit var activity: CreateInfractionActivity
    private lateinit var binding: FragmentVehicleBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle, container, false)
        initAdapters()
        fillFields()
        return binding.root
    }

    override fun initAdapters() {
        /* Set item listeners */
        binding.spnBrandVehicle.onItemSelectedListener = this
        binding.spnTypeDoc.onItemSelectedListener = this
        binding.spnType.onItemSelectedListener = this
        binding.spnIssuedIn.onItemSelectedListener = this
        binding.spnIdentifierDoc.onItemSelectedListener = this
        binding.autxtSubBrandVehicle.doOnTextChanged { text, start, count, after -> SingletonInfraction.subBrandVehicle = text?.trim().toString().toUpperCase() }
        binding.autxtColor.doOnTextChanged { text, start, count, after -> SingletonInfraction.colorVehicle = text?.trim().toString().toUpperCase() }
        binding.edtNoDoc.doOnTextChanged { text, start, count, after -> SingletonInfraction.noDocument = text?.trim().toString().toUpperCase() }
        binding.edtNoCard.doOnTextChanged { text, start, count, after -> SingletonInfraction.noCirculationCard = text?.trim().toString().toUpperCase() }
        binding.edtYear.doOnTextChanged { text, start, count, after ->
            when {
                text?.toString()!!.contentEquals("-") -> binding.edtYear.filters = arrayOf(InputFilter.LengthFilter(1))
                text.isEmpty() -> binding.edtYear.filters = arrayOf(InputFilter.LengthFilter(4))
                else -> binding.edtYear.filters = arrayOf(InputFilter.LengthFilter(4), InputFilter { charSequence, i1, i2, spanned, i3, i4 -> if (charSequence == "-") "" else charSequence })
            }
            SingletonInfraction.yearVehicle = text.trim().toString()
        }
        binding.imgEvidence1.setOnClickListener(this)
        binding.imgEvidence2.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        /* Set spinners adapters */
        binding.spnBrandVehicle.adapter = iterator.value.getBrandAdapter()
        binding.spnType.adapter = iterator.value.getTypeAdapter()
        binding.autxtColor.setAdapter(iterator.value.getColorAdapter())
        binding.spnIdentifierDoc.adapter = iterator.value.getIdentifierDocAdapter()
        iterator.value.getIssuedInAdapter() // Download catalog from Firebase
        binding.spnTypeDoc.adapter = iterator.value.getTypeDocument()
    }

    override fun fillFields() {
        if (SingletonInfraction.evidence1.isNotEmpty()) {
            val decodedBytes = Base64.decode(SingletonInfraction.evidence1.substring(SingletonInfraction.evidence1.indexOf(
                    ",") + 1), Base64.DEFAULT)
            val baseBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            binding.imgEvidence1.setImageBitmap(baseBitmap)
        }
        if (SingletonInfraction.evidence2.isNotEmpty()) {
            val decodedBytes = Base64.decode(SingletonInfraction.evidence2.substring(SingletonInfraction.evidence2.indexOf(
                    ",") + 1), Base64.DEFAULT)
            val baseBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            binding.imgEvidence2.setImageBitmap(baseBitmap)
        }
        binding.spnIdentifierDoc.setSelection(iterator.value.getPositionIdentifiedDoc(SingletonInfraction.identifierDocument))
        binding.spnTypeDoc.setSelection(iterator.value.getPositionAuthority(SingletonInfraction.typeDocument))
        binding.spnBrandVehicle.setSelection(iterator.value.getPositionBrand(SingletonInfraction.brandVehicle))
        binding.spnType.setSelection(iterator.value.getPositionType(SingletonInfraction.typeVehicle))
        binding.autxtSubBrandVehicle.setText(SingletonInfraction.subBrandVehicle)
        binding.autxtColor.setText(SingletonInfraction.colorVehicle)
        binding.edtNoDoc.setText(SingletonInfraction.noDocument)
        binding.edtNoCard.setText(SingletonInfraction.noCirculationCard)
        binding.edtYear.setText(SingletonInfraction.yearVehicle)
    }

    override fun onIssuedInReady(adapter: ArrayAdapter<String>) {
        binding.spnIssuedIn.adapter = adapter
        binding.spnIssuedIn.setSelection(iterator.value.getPositionState(SingletonInfraction.stateIssuedIn))
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.imgEvidence1.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION_CAMERA)
                } else {
                    takePhoto(RC_INTENT_CAMERA_EV1)
                }
            }
            binding.imgEvidence2.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION_CAMERA)
                } else {
                    takePhoto(RC_INTENT_CAMERA_EV2)
                }
            }
            binding.btnSave.id -> {
                if (validFields()) {
                    activity.stepUp()
                    activity.router.value.presentInfractionFragment(Direction.NONE)
                }
            }
        }
    }

    override fun takePhoto(requestCode: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    pathPhoto = photoFile.absolutePath
                    val photoURI: Uri = Utils.getOutputMediaFileUri(it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    val resolvedIntentActivities = activity.packageManager.queryIntentActivities(
                            takePictureIntent,
                            PackageManager.MATCH_DEFAULT_ONLY
                    )
                    for (resolvedIntentInfo in resolvedIntentActivities) {
                        val packageName = resolvedIntentInfo.activityInfo.packageName
                        activity.grantUriPermission(
                                packageName,
                                photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    activity.startActivityForResult(takePictureIntent, requestCode)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.spnIdentifierDoc.id -> {
                SingletonInfraction.identifierDocument = iterator.value.identifierDocList[p2]
            }
            binding.spnIssuedIn.id -> {
                SingletonInfraction.stateIssuedIn = iterator.value.statesList[p2]
            }
            binding.spnTypeDoc.id -> {
                SingletonInfraction.typeDocument = iterator.value.authorityIssuesList[p2]
            }
            binding.spnBrandVehicle.id -> {
                SingletonInfraction.brandVehicle = iterator.value.brandList[p2]
                val idBrand = iterator.value.brandList[p2].id
                binding.autxtSubBrandVehicle.setAdapter(iterator.value.getSubBrandAdapter(idBrand))
            }
            binding.spnType.id -> {
                SingletonInfraction.typeVehicle = iterator.value.typeVehicleList[p2]
            }
        }
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
    }

    override fun validFields(): Boolean {
        var isValid = true
        when {
            SingletonInfraction.identifierDocument.id == 0 -> {
                isValid = false
                onError(getString(R.string.e_identifier_doc))
            }
            binding.edtNoDoc.text.trim().isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_no_document))
            }
            SingletonInfraction.typeDocument.id == 0 -> {
                isValid = false
                onError(getString(R.string.e_type_doc))
            }
            SingletonInfraction.brandVehicle.id == 0 -> {
                isValid = false
                onError(getString(R.string.e_brand_vehicle))
            }
            binding.autxtSubBrandVehicle.text.trim().isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_sub_brand_vehicle))
            }
            binding.autxtColor.text.trim().isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_color_vehicle))
            }
            SingletonInfraction.typeVehicle.id == 0L -> {
                isValid = false
                onError(getString(R.string.e_type_vehicle))
            }
            /* binding.edtNoCard.text.trim().isEmpty() -> {
                 isValid = false
                 onError(getString(R.string.e_circulation_card))
             }*/
            binding.edtYear.text.trim().isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_year))
            }
            !binding.edtYear.text.toString().contentEquals("-") && (binding.edtYear.text.trim().length < 4
                    || binding.edtYear.text.toString().toInt() > Calendar.getInstance().get(Calendar.YEAR) + 1) -> {
                isValid = false
                onError(getString(R.string.e_year_invalid))
            }
            SingletonInfraction.evidence1.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_evidence_1))
            }
            SingletonInfraction.evidence2.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_evidence_2))
            }
        }
        return isValid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val opts = BitmapFactory.Options()
        opts.inSampleSize = 2
        galleryAddPic()
        val exif = ExifInterface(pathPhoto)
        try {
            val original = BitmapFactory.decodeFile(pathPhoto, opts)
            val rotated = BitmapHelper.rotateBitmap(
                    original,
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            )
            val width = rotated!!.width
            val height = rotated.height
            val encoded = if (width > 1280 || height > 720) {
                /* Reducir el tamaño de las imagenes un 40% */
                val newHeight = Math.round(height * .60).toInt()
                val newWidth = Math.round(width * .60).toInt()
                val scaled = Bitmap.createScaledBitmap(rotated, newWidth, newHeight, false)
                BitmapHelper.bitmapToBase64(scaled)
            } else {
                BitmapHelper.bitmapToBase64(rotated)
            }
            val decodedBytes = Base64.decode(encoded.substring(encoded.indexOf(
                    ",") + 1), Base64.DEFAULT)
            val baseBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            if (requestCode == RC_INTENT_CAMERA_EV1) {
                SingletonInfraction.evidence1 = encoded
                binding.imgEvidence1.setImageBitmap(baseBitmap)
            } else if (requestCode == RC_INTENT_CAMERA_EV2) {
                SingletonInfraction.evidence2 = encoded
                binding.imgEvidence2.setImageBitmap(baseBitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(getString(R.string.e_photo_not_loaded))
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an media file valor
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            pathPhoto = absolutePath
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(pathPhoto)
            mediaScanIntent.data = Uri.fromFile(f)
            activity.sendBroadcast(mediaScanIntent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isCreation Parameter 1.
         * @return A new instance of fragment VehicleFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                VehicleFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
