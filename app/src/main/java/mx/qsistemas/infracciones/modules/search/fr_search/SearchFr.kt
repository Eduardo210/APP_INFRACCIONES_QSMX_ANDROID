package mx.qsistemas.infracciones.modules.search.fr_search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentSearchBinding
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.create.OPTION_UPDATE_INFRACTION
import mx.qsistemas.infracciones.modules.search.SearchActivity
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.modules.search.SearchIterator
import mx.qsistemas.infracciones.modules.search.adapters.ID_INFRACTION
import mx.qsistemas.infracciones.modules.search.adapters.SearchAdapter
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.EXTRA_OPTION_INFRACTION
import mx.qsistemas.infracciones.utils.Ticket.Companion.getPrintBarCode
import mx.qsistemas.infracciones.utils.Ticket.Companion.getPrintObject
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROBE_AUTH_ALWAYS
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROD
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
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
val OK_PAYMENT = 100

class SearchFr : Fragment()
        , SearchContracts.Presenter
        , AdapterView.OnItemSelectedListener
        , SearchContracts.OnInfractionClick
        , IPaymentsTransfer.PrintListener
        , IPaymentsTransfer.TransactionListener {


    private var printJson = JSONObject()
    private lateinit var binding: FragmentSearchBinding
    private val iterator = lazy { SearchIterator(this) }
    private var idDocIdent: Int = 0

    private var dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    private val PRINT: Int = 100
    private val PAYMENT: Int = 200
    private val CURRENT_DATE = Date()
    private var isPaid: Boolean = false

    private var amountToPay = "0"
    private var discountPayment = "0"
    private var totalPayment = "0"
    private var totalAmount = "0"

    private var idPerson: Long = 0


    private lateinit var activity: SearchActivity

    private var INFRACTOR_IS_ABSENT: Int = 0

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

    override fun onPrintClick(view: View, position: Int) {
        activity.showLoader("Espere ...")
        iterator.value.doSearchByIdInfraction(ID_INFRACTION, PRINT)
        //Log.d("PRINT", "IMPRIMIENDO ...")
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

        INFRACTOR_IS_ABSENT = infraction.is_absent


        //Impresión de encabezados
        printTest.put(getPrintObject(header1 + header2 + header3 + header4 + header5 + header6, normal_size, "center", "1"))
        printTest.put(getPrintObject("\n\nDETECCIÓN   Y   LEVANTAMIENTO", "2", "center", "0"))
        //printTest.put(getPrintObject("\n", "2", "left", "0"))
        printTest.put(getPrintObject("ELECTRÓNICO     DE     INFRACCIO-", "2", "center", "0"))
        printTest.put(getPrintObject("NES  A CONDUCTORES   DE   VEHÍ-", "2", "center", "0"))
        printTest.put(getPrintObject("CULOS     QUE     CONTRAVENGAN", "2", "center", "0"))
        printTest.put(getPrintObject("LAS   DISPOSICIONES    EN   MATE-", "2", "center", "0"))
        printTest.put(getPrintObject("RIA   DE  TRÁNSITO,  EQUILIBRIO  E-", "2", "center", "0"))
        printTest.put(getPrintObject("COLÓGICO       PROTECCIÓN       AL", "2", "center", "0"))
        printTest.put(getPrintObject("AMBIENTE    Y     PARA    LA    PRE-", "2", "left", "0"))
        printTest.put(getPrintObject("VENCIÓN    Y    CONTROL    DE    LA", "2", "left", "0"))
        printTest.put(getPrintObject("CONTAMINACIÓN,   ASI   COMO  PA-", "2", "left", "0"))
        printTest.put(getPrintObject("GO   DE   SANCIONES    Y    APLICA-", "2", "left", "0"))
        printTest.put(getPrintObject("CIÓN    DE   MEDIDAS   DE   SEGURI-", "2", "left", "0"))
        printTest.put(getPrintObject("DAD.", "2", "left", "0"))
        printTest.put(getPrintObject("\n\n", "2", "left", "0"))
        printTest.put(getPrintObject("EL C. AGENTE  QUE  SUSCRIBE  LA", "2", "left", "0"))
        printTest.put(getPrintObject("PRESENTE      BOLETA       DE      IN-", "2", "left", "0"))
        printTest.put(getPrintObject("FRACCIÓN,  ESTA  FACULTADO   EN", "2", "left", "0"))
        printTest.put(getPrintObject("TÉRMINOS   DE   LOS  QUE  SE   ES-", "2", "left", "0"))
        printTest.put(getPrintObject("TABLECE     EN     LOS   ARTÍCULOS", "2", "left", "0"))
        printTest.put(getPrintObject("21   Y  115,  FRACCIÓN  III,   INCISO,", "2", "left", "0"))
        printTest.put(getPrintObject("H)   DE   LA  CONSTITUCION    POLÍ-", "2", "left", "0"))
        printTest.put(getPrintObject("TICA   DE  LOS  ESTADOS   UNIDOS", "2", "left", "0"))
        printTest.put(getPrintObject("MEXICANOS     DE     ACUERDO     A", "2", "left", "0"))
        printTest.put(getPrintObject("LO   ESTABLECIDO   EN   LOS   ARTÍ-", "2", "left", "0"))
        printTest.put(getPrintObject("CULOS   8.3,  8.10,  8.18,  8.19   BIS", "2", "left", "0"))
        printTest.put(getPrintObject(",8.19   TERCERO   Y   8.19 CUARTO", "2", "left", "0"))
        printTest.put(getPrintObject("DEL     CÓDIGO     ADMINISTRATIVO", "2", "left", "0"))
        printTest.put(getPrintObject("DEL    ESTADO    DE    MÉXICO.  ASÍ", "2", "left", "0"))
        printTest.put(getPrintObject("COMO HACER  CONSTAR  LOS  HE-", "2", "left", "0"))
        printTest.put(getPrintObject("CHOS     QUE     MOTIVAN    LA    IN", "2", "left", "0"))
        printTest.put(getPrintObject("FRACCIÓN    EN    TÉRMINOS    DEL", "2", "left", "0"))
        printTest.put(getPrintObject("ARTÍCULO   16  DENUESTRA   CAR-", "2", "left", "0"))
        printTest.put(getPrintObject("TA MAGNA.\n\n\n", "2", "left", "0"))
        printTest.put(getPrintObject(infraction.date + "\n" + "FOLIO: " + infraction.folio + "\n\n\n", normal_size, "right", "0"))
        try {


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

            } else if (infraction.is_absent == 1) {
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
            vehicle.append("\nNÚMERO: " + infraction.num_doc_ident)
            vehicle.append("\nAUTORIDAD QUE EXPIDE: " + infraction.authority_issue) //TODO:SERVICIO
            vehicle.append("\nEXPEDIDO: " + infraction.doc_ident_issued)
            vehicle.append("\nARTÍCULOS DEL REGLAMENTO DE TRÁNSITO DEL ESTADO DE MÉXICO: ")

            printTest.put(getPrintObject(vehicle.toString(), normal_size, "left", "0"))
            //Artículos y fracciones
            printTest.put(getPrintObject("\n\nARTÍCULO/FRACCIÓN\t\t\tU.M.A.\t\t\tPUNTOS\n*******************", "1", "center", "1"))

            infraction.infraction_fraction.forEach { art ->
                printTest.put(getPrintObject(art.art_fracc + "\t\t\t" + art.minimum_wages + "\t\t\t" + art.penalty_points, normal_size, "center", "0"))
                printTest.put(getPrintObject("\nCONDUCTA QUE MOTIVA LA INFRACCIÓN: ${art.motivation}", "1", "left", "0"))
            }
            address_infra.append("\n\nCALLE: " + infraction.address_street)
            address_infra.append("\nENTRE: " + infraction.address_between_street)
            address_infra.append("\nY: " + infraction.address_and_street)
            address_infra.append("\nCOLONIA: " + infraction.address_colony)

            printTest.put(getPrintObject(address_infra.toString(), normal_size, "left", "0"))

            //Documento que se retiene

            retained_doc.append("\n\nDOCUMENTO QUE SE RETIENE: ")
            if (!infraction.retained_document.equals("")) {
                retained_doc.append("\n" + infraction.retained_document)
            } else {
                retained_doc.append("\nNINGUNO\n")
            }
            if (infraction.referred == 1) {
                retained_doc.append("\nREMISIÓN DEL VEHÍCULO: SI")
                retained_doc.append("\n" + infraction.disposition) //TODO: reemplazar
            }
            printTest.put(getPrintObject(retained_doc.toString(), normal_size, "left", "0"))

            //Responsable del vehpiculo
            printTest.put(getPrintObject("RESPONSABLE DEL VEHÍCULO", normal_size, "center", "1"))
            if (infraction.is_absent == 0 || infraction.is_absent == 1) {
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

            /* printTest.put(getPrintBarCode(infraction.capture_line_i))
             printTest.put(getPrintObject(infraction.capture_line_i, normal_size, "center", "0"))
             printTest.put(getPrintObject("\nCON 70% DE DESCUENTO\nVIGENCIA: $current \nIMPORTE: ${infraction.total}\n", normal_size, "center", "0"))
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

        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("PRINT", ex.toString())
            return ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OK_PAYMENT) {
            clearData()
        }


    }


    fun doPaymentProcess(infraction: InfractionSearch) {
        var compare_date: Int
        var haveToPay: Boolean = true
        val expDate50: Date = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_ii) }
        val expDateFull: Date = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_iii) }

        compare_date = CURRENT_DATE.compareTo(expDate50)//expDate50.compareTo(CURRENT_DATE)
        totalAmount = infraction.amount_capture_line_iii.toString()
        if (compare_date <= 0) { //Si hoy es menor o igual a la fecha limite
            //Tiene el descuento del 50%
            amountToPay = "%.2f".format(infraction.amount_capture_line_ii)
            discountPayment = (infraction.amount_capture_line_iii.toDouble() - infraction.amount_capture_line_ii.toDouble()).toString()
        } else {
            //No tiene descuento
            discountPayment = "0"
            compare_date = CURRENT_DATE.compareTo(expDateFull)//expDateFull.compareTo(CURRENT_DATE)
            if (compare_date <= 0) {
                amountToPay = "#.2f".format(infraction.amount_capture_line_iii)
            } else {
                haveToPay = false
            }
        }
        totalPayment = amountToPay
        /* if (discountPayment.toDouble()>0){
             totalPayment = "%.2f".format(amountToPay.toDouble() - discountPayment.toDouble())
         }*/


        if (haveToPay) {
            PaymentsTransfer.runTransaction(activity, totalPayment, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "La infracción cuenta con recargos. Pagar en ventanilla", Snackbar.LENGTH_LONG)
        }


    }

    override fun onPaymentClick(view: View, position: Int) {
        activity.showLoader("Espere ...")
        iterator.value.doSearchByIdInfraction(ID_INFRACTION, PAYMENT)
        Log.d("PAYMENT", "PAGANDO ...")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onError(var1: Int, var2: String) {
        Log.e("PRINT", var2)
        SnackbarHelper.showErrorSnackBar(activity, var2, Snackbar.LENGTH_LONG)
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

    override fun onResultInfractionById(infraction: InfractionSearch, origin: Int) {
        activity.hideLoader()

        when (origin) {
            PRINT -> {
                activity.showLoader(getString(R.string.l_preparing_printer))
                PaymentsTransfer.print(activity, printInfraction(infraction), null, this)
            }
            PAYMENT ->
                if (infraction.is_absent == 0) {
                    doPaymentProcess(infraction)
                    idPerson = infraction.id_person
                } else {
                    //mandar a pantalla de actualizacion
                    SingletonInfraction.idNewInfraction = ID_INFRACTION.toLong()
                    //SingletonInfraction.subTotalInfraction = infraction.subtotal.toString()
                    //SingletonInfraction.discountInfraction = infraction.payment_discount.toString()
                    //SingletonInfraction.totalInfraction = infraction.payment_total.toString()
                    SingletonInfraction.captureLineii = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_ii) }// infraction.capture_line_ii
                    SingletonInfraction.captureLineiii = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_iii) }// infraction.capture_line_ii
                    SingletonInfraction.amountCaptureLineii = infraction.amount_capture_line_ii
                    SingletonInfraction.amountCaptureLineiii = infraction.amount_capture_line_iii

                    val intent = Intent(activity, CreateInfractionActivity::class.java)
                    intent.putExtra(EXTRA_OPTION_INFRACTION, OPTION_UPDATE_INFRACTION)
                    startActivityForResult(intent, OK_PAYMENT)
                }
        }
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

    override fun onResultSavePayment(msg: String, flag: Boolean) {
        activity.hideLoader()
        if (flag) {
            SnackbarHelper.showSuccessSnackBar(activity, "El pago se guardó satisfactoriamente.", Snackbar.LENGTH_SHORT)
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "Error al guardar datos de pago en servidor.", Snackbar.LENGTH_SHORT)
        }


    }

    override fun onTxApproved(txInfo: TransactionInfo) {
        isPaid = true
        iterator.value.savePaymentToService(ID_INFRACTION, txInfo, totalAmount, discountPayment, totalPayment, idPerson)
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_infraction_pay), Snackbar.LENGTH_SHORT)
    }

    override fun onTxVoucherPrinted() {
        if (isPaid) {
            activity.finish()
        } else {
            var builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_payment_failed), getString(R.string.w_reintent_transaction), activity
            )
            builder.setPositiveButton("Aceptar") { _, _ ->
                PaymentsTransfer.runTransaction(activity, totalPayment, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
            }
            builder.setNegativeButton("Cancelar") { _, _ ->
                // Imprimir boleta
            }
            builder.show()
        }
    }

    override fun onTxFailed(message: String) {
        isPaid = false
    }

    override fun onTxVoucherFailer(message: String) {
        SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
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
