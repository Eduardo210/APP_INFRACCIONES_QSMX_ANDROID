package mx.qsistemas.infracciones.modules.search.fragments

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*

import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentSearchBinding
import mx.qsistemas.infracciones.databinding.FragmentVehicleBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.create.fr_vehicle.VehicleIterator
import mx.qsistemas.infracciones.modules.search.SearchActivity
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.modules.search.SearchIterator
import mx.qsistemas.infracciones.modules.search.adapters.ID_INFRACTION
import mx.qsistemas.infracciones.modules.search.adapters.SearchAdapter
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import okhttp3.internal.Util
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchFr.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchFr.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchFr : Fragment()
        , SearchContracts.Presenter
        , AdapterView.OnItemSelectedListener
        , SearchContracts.OnInfractionClick
        , IPaymentsTransfer.PrintListener {
    private var printJson = JSONObject()
    private lateinit var binding: FragmentSearchBinding
    private val iterator = lazy { SearchIterator(this) }
    private var idDocIdent: Int = 0
    private lateinit var activity: SearchActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SearchActivity

    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    // private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnShowInfra.setOnClickListener {
            if (isValidFilter()) {
                activity.showLoader("Buscando datos ...")
                if (!binding.edtFilterFolio.text.toString().equals("")) {
                    iterator.value.doSearchByFilter(0, binding.edtFilterFolio.text.toString())
                } else {
                    iterator.value.doSearchByFilter(idDocIdent, binding.etFilterAny.text.toString())
                }

            } else {

            }
        }
        binding.imgCleanSearch.setOnClickListener {
            clearData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        initAdapters()
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private fun initAdapters() {
        binding.spSearchFilter.onItemSelectedListener = this
        binding.spSearchFilter.adapter = iterator.value.getDocIdentAdapter()

    }

    private fun getPrintObject(text: String, size: String, position: String, bold: String): JSONObject {
        val json = JSONObject()
        try {
            json.put("content-type", "txt")
            json.put("content", text)
            json.put("size", size)
            json.put("position", position)
            json.put("offset", "0")
            json.put("bold", bold)
            json.put("italic", "0")
            json.put("height", "-1")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    private fun getPrintBarCode(text: String): JSONObject {
        val json = JSONObject()
        try {
            json.put("content-type", "one-dimension")
            json.put("content", text)
            json.put("size", "3")
            json.put("position", "center")
            json.put("offset", "0")
            json.put("bold", "0")
            json.put("italic", "0")
            json.put("height", "2")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    override fun onPrintClick(view: View, position: Int) {
        activity.showLoader("Espere ...")
        iterator.value.doSearchByIdInfraction(ID_INFRACTION)
        Log.d("PRINT", "IMPRIMIENDO ...")
    }

    fun printInfraction(infraction: InfractionSearch): String {
        val title_size = "3"
        val normal_size = "2"
        val bold = "1"


        val printTest = JSONArray()
        val infractor_data = StringBuilder()
        val card_data = StringBuilder()
        val vehicle = StringBuilder()
        val art_frac = StringBuilder()
        val address_infra = StringBuilder()
        val retained_doc = StringBuilder()
        val responsible = StringBuilder()
        val data_footer = StringBuilder()

        val format_date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val current = format_date.format(Date())

        val header1 = "QSISTEMAS_I\n"
        val header2 = "QSISTEMAS_II\n"
        val header3 = "QSISTEMAS_III\n"
        val header4 = "QSISTEMAS_IV\n"
        val header5 = "QSISTEMAS_V\n"
        val header6 = "QSISTEMAS_VI\n"

        val footer1 = "QSISTEMAS_VII"
        val footer2 = "QSISTEMAS_VIII"
        val footer3 = "QSISTEMAS_IX"





        infraction.infraction_fraction?.forEach { art ->
            //printTest.put(getPrintObject(art.id_fraction.toString() + "/" + art.minimum_wages + "/" + art.penalty_points + "\nCONDUCTA QUE MOTIVA LA INFRACCIÓN: " + art.motivation, "2", "center"))
            Log.d("FRACCIONES", "$art")
        }

        //Impresión de encabezados
        printTest.put(getPrintObject(header1 + header2 + header3 + header4 + header5 + header6, normal_size, "center", "1"))
        printTest.put(getPrintObject("\n\nDETECCION Y LEVANTAMIENTO ELECTRONICO DE INFRACCIONES A CONDUCTORES DE VEHICULOS QUE CONTRAVENGAN LAS DISPOSICIONES EN MATERIA DE TRANSITO, EQUILIBRIO ECOLOGICO, PROTECCIÓN AL AMBIENTE Y PARA LA PREVENCION Y CONTROL DE LA CONTAMINACION, ASI COMO PAGO DE SANCIONES Y APLICACION DE MEDIDAS DE SEGURIDAD.\n\nEL C. AGENTE QUE SUSCRIBE LA PRESENTE BOLETA DE INFRACCION, ESTA FACULTADO EN TERMINOS DE LOS QUE SE ESTABLECE EN LOS ARTICULOS 21 Y 115, FRACCION III, INCISO H), DE LA CONSTITUCION  POLITICA DE LOS ESTADOS UNIDOS MEXICANOS DE ACUERDO A LO ESTABLECIDO EN LOS ARTICULOS 8.3, 8.10, 8.18, 8.19 BIS, 8.19 TERCERO Y 8.19 CUARTO, DEL CODIGO ADMINISTRATIVO DEL ESTADO DE MEXICO. ASI COMO HACER CONSTAR LOS HECHOS QUE MOTIVAN LA INFRACCION EN TERMINOS DEL ARTICULO 16 DE NUESTRA CARTA MAGNA.\n\n\n", normal_size, "left", "0"))
        printTest.put(getPrintObject(infraction.date + "\n" + "FOLIO: " + infraction.folio + "\n\n\n", normal_size, "right", "0"))

        //Datos del infractor
        if (infraction.is_absent == 0) {

            infractor_data.append(infraction.name.toUpperCase() + " " + infraction.last_name.toUpperCase() + " " + infraction.mother_last_name.toUpperCase())
            if (!infraction.rfc.equals("")) {
                infractor_data.append("\n" + infraction.rfc)
            }
            if (!infraction.infractor_external_number.equals("")) {
                infractor_data.append("\n" + infraction.infractor_external_number)
            }
            if (!infraction.infractor_internal_number.equals("")) {
                infractor_data.append("\n" + infraction.infractor_internal_number)
            }
            if (!infraction.infractor_colony.equals("")) {
                infractor_data.append("\n" + infraction.infractor_colony)
            }
            printTest.put(getPrintObject(infractor_data.toString(), normal_size, "center", "0"))

            if (!infraction.circulation_card.equals("")) {
                card_data.append("\n" + "LICENCIA/PERMISO: " + infraction.circulation_card)
            }
            if (infraction.card_type_type.equals("")) {
                card_data.append("\n" + "TIPO LICENCIA: " + infraction.card_type_type)
            }
            if (infraction.issued_in.equals("")) {
                card_data.append("\n" + "EXPEDIDA: " + infraction.issued_in)
            }
            printTest.put(getPrintObject(card_data.toString(), normal_size, "left", "0"))

        } else if (infraction.is_absent == 2) {
            printTest.put(getPrintObject(infraction.name.toUpperCase() + " " + infraction.last_name.toUpperCase() + " " + infraction.mother_last_name.toUpperCase() + "\n\n", normal_size, "center", "0"))
        }
        //Características del vehículo
        vehicle.append("\nCARACTERÍSTICAS DEL VEHÍCULO: ")
        vehicle.append("\nMARCA: " + infraction.brand) //TODO:SERVICIO
        if (!infraction.sub_brand.equals("")) {
            vehicle.append("\nSUBMARCA: " + infraction.sub_brand)
        }
        vehicle.append("\nTIPO: " + infraction.vehicle_type)
        vehicle.append("\nCOLOR: " + infraction.vehicle_color)
        vehicle.append("\nMODELO: " + infraction.vehicle_model)
        vehicle.append("\nIDENTIFICADOR: " + infraction.ident_document) //TODO:SERVICIIO
        vehicle.append("\nNUMERO: " + infraction.num_doc_ident)
        vehicle.append("\nAUTORIDAD QUE EXPIDE: " + infraction.authority_issue) //TODO:SERVICIO
        vehicle.append("\nEXPEDIDO: " + infraction.doc_ident_issued)
        vehicle.append("\nARTICULOS DEL REGLAMENTO DE TRÁNSITO DEL ESTADO DE MÉXICO: ")

        printTest.put(getPrintObject(vehicle.toString(), normal_size, "left", "0"))
        //Artículos y fracciones
        printTest.put(getPrintObject("\n\nARTICULO/FRACCION\t\t\tU.M.A.\t\t\tPUNTOS\n*******************", "1", "center", "1"))

        infraction.infraction_fraction.forEach { art ->
            //TODO:SERVICIO
            printTest.put(getPrintObject(art.art_fracc + "\t\t\t" + art.minimum_wages + "\t\t\t" + art.penalty_points, normal_size, "center", "0"))
            printTest.put(getPrintObject("\nCONDUCTA QUE MOTIVA LA INFRACCIÓN: ${art.motivation}", "1", "center", "0"))
        }
        address_infra.append("CALLE: " + infraction.address_street)
        address_infra.append("ENTRE: " + infraction.address_between_street)
        address_infra.append("Y: " + infraction.address_and_street)
        address_infra.append("COLONIA: " + infraction.address_colony)

        printTest.put(getPrintObject(address_infra.toString(), normal_size, "left", "0"))

        //Documento que se retiene

        retained_doc.append("\n\nDOCUMENTO QUE SE RETIENE: ")
        if (!infraction.retained_document.equals("")) {
            retained_doc.append("\n" + infraction.retained_document)
        } else {
            retained_doc.append("\nNINGUNO\n")
        }
        if (infraction.referred == 1) {
            retained_doc.append("\nREMISION DEL VEHICULO: SI")
            retained_doc.append("\n" + infraction.disposition) //TODO: reemplazar
        }
        printTest.put(getPrintObject(retained_doc.toString(), normal_size, "left", "0"))

        //Responsable del vehpiculo
        printTest.put(getPrintObject("RESPONSABLE DEL VEHÍCULO", normal_size, "center", "1"))
        if (infraction.is_absent == 0 || infraction.is_absent == 2) {
            responsible.append("\n\n" + infraction.name + " " + infraction.last_name + " " + infraction.mother_last_name + "\n")
        } else {
            responsible.append("\n\nQ R R \n")
        }
        responsible.append("\nRECIBO DE CONFORMIDAD\n\n\n\n")
        responsible.append("\nFIRMA\n")
        responsible.append("\nAGENTE: \n")
        responsible.append(infraction.official) //TODO: reemplazar
        responsible.append("\nEMPLEADO: ${infraction.employee}\n\n") //TODO: reemplazar

        printTest.put(getPrintObject(responsible.toString(), normal_size, "center", "0"))
        printTest.put(getPrintObject("\nFIRMA\n\n", normal_size, "center", "0"))
        printTest.put(getPrintObject("\n\n\n", normal_size, "center", "0"))
        printTest.put(getPrintObject("\n\n\n", normal_size, "center", "0"))

        //Descuento con el 70%

        /*printTest.put(getPrintBarCode(infraction.capture_line_i))
        printTest.put(getPrintObject(infraction.capture_line_i, normal_size, "center","0"))
        printTest.put(getPrintObject("\nCON 70% DE DESCUENTO\nVIGENCIA: $current \nIMPORTE: ${infraction.total}\n", normal_size, "center","0"))
*/
        //Descuento con el 50%
        printTest.put(getPrintBarCode(infraction.capture_line_ii))
        printTest.put(getPrintObject(infraction.capture_line_ii, normal_size, "center", "0"))
        printTest.put(getPrintObject("\nCON 50% DE DESCUENTO\nVIGENCIA: ${infraction.date_capture_line_ii} \nIMPORTE: ${infraction.amount_capture_line_ii}\n\n", normal_size, "center", "0"))

        //Sin descuento
        printTest.put(getPrintBarCode(infraction.capture_line_iii))
        printTest.put(getPrintObject(infraction.capture_line_iii, normal_size, "center", "0"))
        printTest.put(getPrintObject("\nSIN DESCUENTO\nVIGENCIA: ${infraction.date_capture_line_iii} \nIMPORTE: ${infraction.amount_capture_line_iii}\n", normal_size, "center", "0"))

        data_footer.append("\n\n\n$footer1")
        data_footer.append("\n$footer2")
        data_footer.append("\n$footer3")
        data_footer.append("\n\n-AVISO DE PRIVACIDAD-\n\n")
        data_footer.append("\nFUENTE DE CAPTURA: SIIP\n\n\n")

        printTest.put(getPrintObject(data_footer.toString(), normal_size, "center", "0"))

        printJson.put("spos", printTest)
        return printJson.toString()
    }

    override fun onPaymentClick(view: View, position: Int) {
        Log.d("PAYMENT", "PAGANDO ...")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onError(var1: Int, var2: String) {
        Log.e("PRINT", var2)
    }

    override fun onFinish() {
        Log.d("PRINT", "SUCCESS")
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        idDocIdent = returnCorrectNumber(p2)
        if (p2 > 0) {
            binding.etFilterAny.visibility = View.VISIBLE
            if (!binding.edtFilterFolio.text.equals("")) {
                binding.edtFilterFolio.setText("")
            }

        } else {
            binding.etFilterAny.visibility = View.GONE
        }

    }

    fun returnCorrectNumber(position: Int): Int {
        return when (position) {
            0 -> 0
            1 -> 4
            2 -> 2
            3 -> 1
            4 -> 5
            5 -> 3
            else -> 0
        }


    }

    fun isValidFilter(): Boolean {
        return if (binding.edtFilterFolio.text != null) {
            true
        } else idDocIdent > 0 && binding.etFilterAny.text != null
    }

    override fun onResultInfractionById(infraction: InfractionSearch) {
        activity.hideLoader()
        PaymentsTransfer.print(activity, printInfraction(infraction), null, this)
    }

    override fun onResultSearch(listInfractions: MutableList<InfractionList.Results>) {
        activity.hideLoader()
        val totalResults = listInfractions.size
        if (totalResults > 0) {
            constraint_results.visibility = View.VISIBLE
            binding.rclResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rclResults.adapter = SearchAdapter(listInfractions, this)
        } else {
            constraint_results.visibility = View.GONE
        }


        binding.txtTotalSearch.text = totalResults.toString()

        val filter =
                if (binding.edtFilterFolio.text.toString().equals("")) {
                    binding.etFilterAny.text.toString()
                } else {
                    binding.edtFilterFolio.text.toString()
                }

        binding.txtFilterSearch.text = filter

    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
        activity.hideLoader()
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun clearData() {
        binding.etFilterAny.setText("")
        binding.edtFilterFolio.setText("")
        binding.etFilterAny.visibility = View.GONE
        binding.spSearchFilter.setSelection(0)
        binding.constraintResults.visibility = View.GONE
        binding.rclResults.adapter = null

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchFr().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
