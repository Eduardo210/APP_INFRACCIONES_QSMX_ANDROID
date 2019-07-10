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
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.create.OPTION_UPDATE_INFRACTION
import mx.qsistemas.infracciones.modules.search.SearchActivity
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.modules.search.SearchIterator
import mx.qsistemas.infracciones.modules.search.adapters.*
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.EXTRA_OPTION_INFRACTION
import mx.qsistemas.infracciones.utils.Ticket
import mx.qsistemas.infracciones.utils.Ticket.Companion.getPrintBarCode
import mx.qsistemas.infracciones.utils.Ticket.Companion.getPrintObject
import mx.qsistemas.infracciones.utils.Validator
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROBE_AUTH_ALWAYS
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROD
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.meta.When

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

    private lateinit var binding: FragmentSearchBinding
    private val iterator = lazy { SearchIterator(this) }
    private var idDocIdent: Int = 0

    private val PRINT: Int = 100
    private val PAYMENT: Int = 200
    private val CURRENT_DATE = Date()
    private var isPaid: Boolean = false

    private var amountToPay = "0"
    private var discountPayment = "0"
    private var totalPayment = "0"
    private var totalAmount = "0"

    private var idPerson: Long = 0

    private var itemInfraOnline: MutableList<InfractionList.Results> = ArrayList()
    private var itemInfraOffLine: MutableList<InfractionItem> = ArrayList()


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
                if (Validator.isNetworkEnable(activity)) {
                    activity.showLoader("Buscando infracciones")
                    if (!binding.edtFilterFolio.text.toString().equals("")) {
                        iterator.value.doSearchByFilter(0, binding.edtFilterFolio.text.toString())
                    } else {
                        iterator.value.doSearchByFilter(idDocIdent, binding.etFilterAny.text.toString())
                    }
                } else {
                    activity.hideLoader()
                    activity.showLoader("Buscando infracciones")
                    if (!binding.edtFilterFolio.text.toString().equals("")) {
                        iterator.value.doSearchByFilterOffLine(idDocIdent, binding.edtFilterFolio.text.toString())
                    } else {
                        iterator.value.doSearchByFilterOffLine(idDocIdent, binding.etFilterAny.text.toString())
                    }

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

    override fun onPrintClick(view: View, position: Int, origin: Int) {

        activity.showLoader("Espere ...")
        val idInfrac: Long
        when (origin) {
            PRINT_LOCAL -> {
                idInfrac = itemInfraOffLine[position].id_infraction
                Log.d("ID_INFRACCION_LIST", "$idInfrac")
                iterator.value.doSearchByIdInfractionOffLine(idInfrac.toString(), PRINT)
            }
            PRINT_ONLINE -> {
                idInfrac = itemInfraOnline[position].id_infraction
                Log.d("ID_INFRACCION_LIST", "$idInfrac")
                iterator.value.doSearchByIdInfraction(idInfrac.toString(), PRINT)
            }
        }
    }

    fun printLocalInfraction(infraction: InfractionLocal, infra_fracc: MutableList<SingletonTicket.ArticleFraction>) {
        SingletonTicket.cleanData()

        SingletonTicket.dateTicket = infraction.REGISTRO_FECHA
        SingletonTicket.folioTicket = infraction.FOLIO

        SingletonTicket.completeNameOffender = "${infraction.NOMBRE} ${infraction.A_PATERNO} ${infraction.A_MATERNO}"
        if(!infraction.RFC.isNullOrEmpty()){
            SingletonTicket.rfcOffender = infraction.RFC.toString()
        }
        if (!infraction.CALLE_PERSON.isNullOrEmpty()){
            SingletonTicket.streetOffender = infraction.CALLE_PERSON.toString()
        }
        if(!infraction.EXTERIOR.isNullOrEmpty()){
            SingletonTicket.noExtOffender = infraction.EXTERIOR.toString()
        }

        if(!infraction.INTERIOR.isNullOrEmpty()){
            SingletonTicket.noIntOffender = infraction.INTERIOR.toString()
        }
        if(!infraction.COL_PERSON.isNullOrEmpty()){
            SingletonTicket.colonyOffender = infraction.COL_PERSON.toString()
        }
        if(!infraction.ESTADO_INFRACTOR.isNullOrEmpty()){
            SingletonTicket.stateOffender = infraction.ESTADO_INFRACTOR.toString()
        }
        if(!infraction.TARJETA_CIRCULACION.isNullOrEmpty()){
            SingletonTicket.noLicenseOffender = infraction.TARJETA_CIRCULACION.toString()
        }
        if(!infraction.TIPO.isNullOrEmpty()){
            SingletonTicket.typeLicenseOffender = infraction.TIPO.toString()
        }
        if(!infraction.EXPEDIDA_EN_LICENCIA.isNullOrEmpty()){
            SingletonTicket.stateLicenseOffender = infraction.EXPEDIDA_EN_LICENCIA.toString()
        }

        SingletonTicket.brandVehicle = infraction.MARCA_VEHICULO.toString()
        SingletonTicket.subBrandVehicle = infraction.SUBMARCA.toString()
        SingletonTicket.typeVehicle = infraction.TIPO.toString()
        SingletonTicket.colorVehicle = infraction.COLOR.toString()
        SingletonTicket.modelVehicle = infraction.MODELO.toString()
        SingletonTicket.identifierVehicle = infraction.DOCUMENTO.toString()
        SingletonTicket.noIdentifierVehicle = infraction.NUM_DOCUMENTO_IDENTIFICADOR
        SingletonTicket.expeditionAuthVehicle = infraction.AUTORIDAD.toString()
        SingletonTicket.stateExpVehicle = infraction.DOCUMENTO_EXPEDIDO_EN.toString()

        infra_fracc.forEach { fracc ->
            val article = fracc.motivation.let {
                SingletonTicket.ArticleFraction(
                        fracc.article,
                        fracc.fraction,
                        fracc.umas,
                        fracc.points, it)
            }

            article.let { SingletonTicket.fractionsList.add(it) }
        }


        SingletonTicket.streetInfraction = infraction.CALLE_INFRA.toString()

        if(!infraction.ENTRE_CALLE_INFRA.isNullOrEmpty()){
            SingletonTicket.betweenStreetInfraction = infraction.ENTRE_CALLE_INFRA.toString()
        }

        if(!infraction.Y_CALLE_INFRA.isNullOrBlank()){
            SingletonTicket.andStreetInfraction = infraction.Y_CALLE_INFRA.toString()
        }

        SingletonTicket.colonyInfraction = infraction.COL_INFRA.toString()
        SingletonTicket.townshipInfraction = infraction.TOWN_INFRA.toString()
        SingletonTicket.retainedDocumentInfraction = infraction.DOCUMENTO_RETENIDO.toString()

        if (infraction.ID_DISPOSICION != 0) {
            SingletonTicket.isRemitedInfraction = true
            SingletonTicket.remitedDispositionInfraction = infraction.DISPOSICION.toString()
        }

        SingletonTicket.captureLineList.add(
                SingletonTicket.CaptureLine(
                        infraction.LINEA_CAPTURA_II,
                        "CON 50% DE DESCUENTO",
                        infraction.FEC_LINEA_CAPTURA_II,
                        infraction.IMPORTE_LINEA_CAPTURA_II.toString()))
        SingletonTicket.captureLineList.add(
                SingletonTicket.CaptureLine(
                        infraction.LINEA_CAPTURA_III,
                        "SIN DESCUENTO",
                        infraction.FEC_LINEA_CAPTURA_III,
                        infraction.IMPORTE_LINEA_CAPTURA_III.toString()
                )
        )
        Ticket.printTicket(activity, object : Ticket.TicketListener {
            override fun onTicketPrint() {
                activity.hideLoader()

            }

            override fun onTicketError() {
                onError("Ha ocurrido un error em la impresión")
            }

        })


    }

    fun printInfractionTest(infraction: InfractionSearch) {
        INFRACTOR_IS_ABSENT = infraction.is_absent
        SingletonTicket.cleanData()

        SingletonTicket.dateTicket = infraction.date
        SingletonTicket.folioTicket = infraction.folio

        SingletonTicket.completeNameOffender = "${infraction.name} ${infraction.last_name} ${infraction.mother_last_name}"
        SingletonTicket.rfcOffender = infraction.rfc
        SingletonTicket.streetOffender = infraction.infractor_street
        SingletonTicket.noExtOffender = infraction.infractor_external_number
        SingletonTicket.noIntOffender = infraction.infractor_internal_number
        SingletonTicket.colonyOffender = infraction.infractor_colony
        SingletonTicket.stateOffender = infraction.infractor_state
        SingletonTicket.noLicenseOffender = infraction.license_number
        SingletonTicket.typeLicenseOffender = infraction.card_type_type
        SingletonTicket.stateLicenseOffender = infraction.issued_in
        SingletonTicket.brandVehicle = infraction.brand
        SingletonTicket.subBrandVehicle = infraction.sub_brand
        SingletonTicket.typeVehicle = infraction.vehicle_type
        SingletonTicket.colorVehicle = infraction.vehicle_color
        SingletonTicket.modelVehicle = infraction.vehicle_model
        SingletonTicket.identifierVehicle = infraction.ident_document
        SingletonTicket.noIdentifierVehicle = infraction.num_doc_ident
        SingletonTicket.expeditionAuthVehicle = infraction.doc_ident_issued
        SingletonTicket.stateExpVehicle = infraction.doc_ident_issued //TODO: corregir

        infraction.infraction_fraction.forEach { fracc ->
            var article = fracc.motivation?.let {
                SingletonTicket.ArticleFraction(
                        fracc.art,
                        fracc.fracc,
                        fracc.minimum_wages.toString(),
                        fracc.penalty_points.toString(), it)
            }

            article?.let { SingletonTicket.fractionsList.add(it) }
        }


        SingletonTicket.streetInfraction = infraction.address_street
        SingletonTicket.betweenStreetInfraction = infraction.address_between_street
        SingletonTicket.andStreetInfraction = infraction.address_and_street
        SingletonTicket.colonyInfraction = infraction.address_colony
        SingletonTicket.townshipInfraction = infraction.infraction_township
        SingletonTicket.retainedDocumentInfraction = infraction.retained_document

        if (infraction.id_disposition != 0) {
            SingletonTicket.isRemitedInfraction = true
            SingletonTicket.remitedDispositionInfraction = infraction.disposition
        }

        SingletonTicket.captureLineList.add(
                SingletonTicket.CaptureLine(
                        infraction.capture_line_ii,
                        "CON 50% DE DESCUENTO",
                        infraction.date_capture_line_ii,
                        infraction.amount_capture_line_ii.toString()))
        SingletonTicket.captureLineList.add(
                SingletonTicket.CaptureLine(
                        infraction.capture_line_iii,
                        "SIN DESCUENTO",
                        infraction.date_capture_line_iii,
                        infraction.amount_capture_line_iii.toString()
                )
        )
        Ticket.printTicket(activity, object : Ticket.TicketListener {
            override fun onTicketPrint() {
                activity.hideLoader()

            }

            override fun onTicketError() {
                onError("Ha ocurrido un error em la impresión")
            }

        })


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

    override fun onPaymentClick(view: View, position: Int, origin: Int) {
        activity.showLoader("Espere ...")
        val idInfrac:Long
        when (origin) {
            PAYMENT_LOCAL -> {
                idInfrac = itemInfraOffLine[position].id_infraction
                localPayment()
            }
            PAYMENT_ONLINE -> {
                idInfrac = itemInfraOnline[position].id_infraction
                iterator.value.doSearchByIdInfraction(idInfrac.toString(), PAYMENT)
            }
        }
    }

    private fun localPayment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                printInfractionTest(infraction)//PaymentsTransfer.print(activity, printInfraction(infraction), null, this)
            }
            PAYMENT ->
                if (infraction.is_absent == 0) {
                    doPaymentProcess(infraction)
                    idPerson = infraction.id_person
                } else {
                    //mandar a pantalla de actualizacion
                    SingletonInfraction.idNewInfraction = ID_INFRACTION.toLong()
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
        itemInfraOnline = listInfractions

        val totalResults = listInfractions.size
        if (totalResults > 0) {
            constraint_results.visibility = View.VISIBLE
            binding.rclResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rclResults.adapter = SearchAdapter(listInfractions, this)
            binding.txtTotalSearch.text = totalResults.toString()

            val filter =
                    if (binding.edtFilterFolio.text.toString().equals("")) {
                        binding.etFilterAny.text.toString()
                    } else {
                        binding.edtFilterFolio.text.toString()
                    }

            binding.txtFilterSearch.text = filter
        } else {
            activity.showLoader("Buscando infracciones")
            if (!binding.edtFilterFolio.text.toString().equals("")) {
                iterator.value.doSearchByFilterOffLine(idDocIdent, binding.edtFilterFolio.text.toString())
            } else {
                iterator.value.doSearchByFilterOffLine(idDocIdent, binding.etFilterAny.text.toString())
            }
        }


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

    override fun onResultSearchOffLine(listInfractions: MutableList<InfractionItem>) {
        activity.hideLoader()
        itemInfraOffLine = listInfractions
        val totalResults = listInfractions.size
        if (totalResults > 0) {
            constraint_results.visibility = View.VISIBLE
            binding.rclResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rclResults.adapter = HistoricalAdapter(listInfractions, this)

            binding.txtTotalSearch.text = totalResults.toString()
            val filter =
                    if (binding.edtFilterFolio.text.toString().equals("")) {
                        binding.etFilterAny.text.toString()
                    } else {
                        binding.edtFilterFolio.text.toString()
                    }

            binding.txtFilterSearch.text = "Infracciones locales"
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "No se encontraron infracciones.", Snackbar.LENGTH_SHORT)
            constraint_results.visibility = View.GONE
        }


    }

    override fun onResultInfractionByIdOffline(infraction: InfractionLocal, infra_fracc: MutableList<SingletonTicket.ArticleFraction>, origin: Int) {
        when (origin) {
            PAYMENT -> doPaymentLocalProcess() //Hacer el proceso de pago
            PRINT -> {
                activity.showLoader(getString(R.string.l_preparing_printer))
                printLocalInfraction(infraction, infra_fracc)
            }
        }

    }

    private fun doPaymentLocalProcess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTicketPrinted() {

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
