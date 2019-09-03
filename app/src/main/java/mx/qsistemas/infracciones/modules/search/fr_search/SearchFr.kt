package mx.qsistemas.infracciones.modules.search.fr_search

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_cardview.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentSearchBinding
import mx.qsistemas.infracciones.db_web.entities.InfractionItem
import mx.qsistemas.infracciones.db_web.entities.InfringementData
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.create.OPTION_UPDATE_INFRACTION
import mx.qsistemas.infracciones.modules.search.SearchActivity
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.modules.search.SearchIterator
import mx.qsistemas.infracciones.modules.search.adapters.*
import mx.qsistemas.infracciones.net.result_web.detail_result.DetailResult
import mx.qsistemas.infracciones.net.result_web.detail_result.NewCaptureLines
import mx.qsistemas.infracciones.net.result_web.search_result.DataItem
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.*
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROBE_AUTH_ALWAYS
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROD
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
var TOKEN_INFRACTION: String = ""

class SearchFr : Fragment()
        , SearchContracts.Presenter
        , AdapterView.OnItemSelectedListener
        , SearchContracts.OnInfractionClick
        , IPaymentsTransfer.PrintListener
        , IPaymentsTransfer.TransactionListener {

    private lateinit var binding: FragmentSearchBinding
    private val iterator = lazy { SearchIterator(this) }
    private var idDocIdent: String = ""

    private val PRINT: Int = 100
    private val PAYMENT: Int = 200
    private val CURRENT_DATE = Date()
    private var isPaid: Boolean = false

    private var amountToPay: Float = 0F
    private var discountInfraction: Float = 0F
    private var subtotalInfraction: Float = 0F
    private var surchargesInfraction: Float = 0F
    private var totalInfraction = 0F
    private var totalAmount = 0F
    private var folioInfraction = ""

    private var idPerson: Long = 0

    private lateinit var itemInfraOnline: MutableList<DataItem>
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

            if (Validator.isNetworkEnable(activity) && binding.edtFilterAny.text.toString().isNotEmpty()) {
                activity.showLoader("Buscando infracciones ...")
                iterator.value.doSearchByFilter(binding.edtFilterAny.text.toString())
            } else {
                activity.showLoader("Buscando infracciones ...")
                lifecycleScope.launch {
                    iterator.value.doSearchByFilterOffLine(binding.edtFilterAny.text.toString())
                }
            }
        }
        binding.imgCleanSearch.setOnClickListener {
            clearData()
        }
        binding.imvDialogInfo.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val view = activity.layoutInflater.inflate(R.layout.custom_cardview, null)
            builder.setCancelable(false)
            builder.setCustomTitle(view)
            val mAlertDialog = builder.show()
            view.btn_accepted.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        initAdapters()
        return binding.root

    }

    private fun initAdapters() {
        iterator.value.getIdentifierDocAdapter()

    }

    override fun onPrintClick(view: View, position: Int, origin: Int) {

        activity.showLoader("Espere ...")
        when (origin) {
            PRINT_LOCAL -> {
                TOKEN_INFRACTION = itemInfraOffLine[position].id_infraction.toString()
                Log.d("ID_INFRACCION_LIST", TOKEN_INFRACTION)
                lifecycleScope.launch {
                    iterator.value.doSearchByIdInfractionOffLine(TOKEN_INFRACTION, PRINT)
                }
            }
            PRINT_ONLINE -> {
                TOKEN_INFRACTION = itemInfraOnline[position].token.toString()
                Log.d("ID_INFRACCION_LIST", TOKEN_INFRACTION)
                iterator.value.doSearchByIdInfraction(TOKEN_INFRACTION, PRINT)
            }
        }
    }

    private suspend fun printLocalInfraction(infraction: InfringementData) {
        SingletonTicket.cleanData()
        var brand = ""
        var model = ""
        var type = ""
        var colour = ""
        var doc_ident = ""
        var authority = ""
        var issued_in = ""
        var article = ""
        var fraction = ""



        SingletonTicket.dateTicket = infraction.infringement?.date ?: ""
        SingletonTicket.folioTicket = infraction.infringement?.folio ?: ""

        SingletonTicket.completeNameOffender = "${infraction.driver?.name} ${infraction.driver?.paternal} ${infraction.driver?.maternal}"
        if (!infraction.driver?.rfc.isNullOrEmpty()) {
            SingletonTicket.rfcOffender = infraction.driver?.rfc ?: ""
        }
        if (!infraction.driverAddressDriver?.street.isNullOrEmpty()) {
            SingletonTicket.streetOffender = infraction.driverAddressDriver?.street.toString()
        }
        if (!infraction.driverAddressDriver?.exterior_num.isNullOrEmpty()) {
            SingletonTicket.noExtOffender = infraction.driverAddressDriver?.exterior_num ?: ""
        }

        if (!infraction.driverAddressDriver?.internal_num.isNullOrEmpty()) {
            SingletonTicket.noIntOffender = infraction.driverAddressDriver?.internal_num ?: ""
        }
        /*Obtener catálogos desde firebase*/
        val job = GlobalScope.launch(Dispatchers.Main) {
            brand = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.brand_reference.toString(), FS_COL_BRANDS, "value")
            model = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.sub_brand_id.toString(), FS_COL_MODELS, "value")
            type = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.class_type_id.toString(), FS_COL_CLASS_TYPE, "value")
            colour = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.colour_id.toString(), FS_COL_COLORS, "value")
            doc_ident = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.identifier_document_id.toString(), FS_COL_IDENTIF_DOC, "value")
            authority = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.document_type.toString(), FS_COL_TYPE_DOC, "value")
            issued_in = CatalogsFirebaseManager.getValue(infraction.vehicleVehicles?.issued_in_id.toString(), FS_COL_STATES, "value")

        }
        job.join()


        /*if (!infraction.COL_PERSON.isNullOrEmpty()) {
            SingletonTicket.colonyOffender = infraction.COL_PERSON.toString()
        }
        if (!infraction.ESTADO_INFRACTOR.isNullOrEmpty()) {
            SingletonTicket.stateOffender = infraction.ESTADO_INFRACTOR.toString()
        }
        if (!infraction.TARJETA_CIRCULACION.isNullOrEmpty()) {
            SingletonTicket.noLicenseOffender = infraction.TARJETA_CIRCULACION.toString()
        }
        if (!infraction.TIPO.isNullOrEmpty()) {
            SingletonTicket.typeLicenseOffender = infraction.TIPO.toString()
        }
        if (!infraction.EXPEDIDA_EN_LICENCIA.isNullOrEmpty()) {
            SingletonTicket.stateLicenseOffender = infraction.EXPEDIDA_EN_LICENCIA.toString()
        }
        SingletonTicket.nameAgent = infraction.OFICIAL.toString()*/
        if (brand.isNotEmpty()) {
            SingletonTicket.brandVehicle = brand
        }
        if (model.isNotEmpty()) {
            SingletonTicket.subBrandVehicle = model
        }

        if (type.isNotEmpty()) {
            SingletonTicket.typeVehicle = type
        }

        if (colour.isNotEmpty()) {
            SingletonTicket.colorVehicle = colour
        }

        SingletonTicket.modelVehicle = infraction.vehicleVehicles?.year ?: ""

        if (doc_ident.isNotEmpty()) {
            SingletonTicket.identifierVehicle = doc_ident
        }

        SingletonTicket.noIdentifierVehicle = infraction.vehicleVehicles?.num_document ?: ""

        if (authority.isNotEmpty()) {
            SingletonTicket.expeditionAuthVehicle = authority
        }

        if (issued_in.isNotEmpty()) {
            SingletonTicket.stateExpVehicle = issued_in
        }

        infraction.fractions?.forEach { fracc ->

            val jobFractions = GlobalScope.launch(Dispatchers.Main) {
                article = CatalogsFirebaseManager.getValue(fracc.articles_reference, FS_COL_ARTICLES, "number")
                fraction = CatalogsFirebaseManager.getValue(fracc.fraction_id, FS_COL_FRACTIONS, "number")
            }
            jobFractions.join()

            SingletonTicket.ArticleFraction(
                    article,
                    fraction,
                    fracc.uma.toString(),
                    fracc.reason)

            SingletonTicket.fractionsList.add(SingletonTicket.ArticleFraction(article, fraction, fracc.uma.toString(), fracc.reason))
        }
        SingletonTicket.streetInfraction = infraction.infringementAddress?.street ?: ""

        if (!infraction.infringementAddress?.street_a.isNullOrEmpty()) {
            SingletonTicket.betweenStreetInfraction = infraction.infringementAddress?.street_a ?: ""
        }

        if (!infraction.infringementAddress?.street_b.isNullOrEmpty()) {
            SingletonTicket.andStreetInfraction = infraction.infringementAddress?.street_b ?: ""
        }
/*
        SingletonTicket.colonyInfraction = infraction.COL_INFRA.toString()
        SingletonTicket.retainedDocumentInfraction = infraction.DOCUMENTO_RETENIDO.toString()

        if (infraction.ID_DISPOSICION != 0) {
            SingletonTicket.isRemitedInfraction = true
            SingletonTicket.remitedDispositionInfraction = infraction.DISPOSICION.toString()
        }
*/
        SingletonTicket.captureLines.add(
                SingletonTicket.CaptureLine(
                        infraction.captureLines?.get(0)?.key ?: "",
                        "CON 50% DE DESCUENTO",
                        infraction.captureLines?.get(0)?.date ?: "",
                        infraction.captureLines?.get(0)?.amount.toString()))
        SingletonTicket.captureLines.add(
                SingletonTicket.CaptureLine(
                        infraction.captureLines?.get(1)?.key ?: "",
                        "SIN DESCUENTO",
                        infraction.captureLines?.get(1)?.date ?: "",
                        infraction.captureLines?.get(1)?.amount.toString()
                )
        )
        Ticket.printTicket(activity, object : Ticket.TicketListener {
            override fun onTicketPrint() {
                activity.hideLoader()

            }

            override fun onTicketError() {
                onError("Ha ocurrido un error en la impresión")
            }
        })
    }

    private fun printInfractionOnline(infraction: DetailResult) {
        INFRACTOR_IS_ABSENT = if (infraction.isAbsent!!) 1 else 0
        SingletonTicket.cleanData()

        SingletonTicket.dateTicket = infraction.folio.toString()//TODO:Falta
        SingletonTicket.folioTicket = infraction.date.toString() //TODO: Falta el folio

        SingletonTicket.completeNameOffender = "${infraction.driver?.name} ${infraction.driver?.paternal} ${infraction.driver?.maternal}"
        SingletonTicket.rfcOffender = infraction.driver?.rfc.toString()

        if (infraction.isAbsent) { //TODO: Corregir con negacion
            if (infraction.driver?.address?.street?.isNotBlank()!!) {
                SingletonTicket.streetOffender = infraction.driver.address.street
            }

            if (infraction.driver.address.exteriorNum?.isNotBlank()!!) {
                SingletonTicket.noExtOffender = infraction.driver.address.exteriorNum
            }

            if (infraction.driver.address.internalNum?.isNotBlank()!!) {
                SingletonTicket.noIntOffender = infraction.driver.address.internalNum
            }

            if (infraction.driver.address.colony?.isNotBlank()!!) {
                SingletonTicket.colonyOffender = infraction.driver.address.colony
            }

            if (infraction.driver.address.state?.isNotBlank()!!) {
                SingletonTicket.stateOffender = infraction.driver.address.state
            }
        }


        if (infraction.townHall != null) {
            SingletonTicket.nameAgent = infraction.townHall
        }
        if (infraction.driverLicense?.licenseNumber != null) {
            SingletonTicket.noLicenseOffender = infraction.driverLicense.licenseNumber
        }
        if (infraction.driverLicense?.licenseType != null) {
            SingletonTicket.typeLicenseOffender = infraction.driverLicense.licenseType
        }
        if (infraction.driverLicense?.state != null) {
            SingletonTicket.stateLicenseOffender = infraction.driverLicense.state
        }

        if (infraction.vehicle?.brand != null) {
            SingletonTicket.brandVehicle = infraction.vehicle.brand
        }
        if (infraction.vehicle?.model != null) {
            SingletonTicket.subBrandVehicle = infraction.vehicle.model
        }
        if (infraction.vehicle?.classType != null) {
            SingletonTicket.typeVehicle = infraction.vehicle.classType
        }
        if (infraction.vehicle?.color != null) {
            SingletonTicket.colorVehicle = infraction.vehicle.color
        }

        if (infraction.vehicle?.year != null) {
            SingletonTicket.modelVehicle = infraction.vehicle.year
        }

        if (infraction.vehicle?.identifierDocument != null) {
            SingletonTicket.identifierVehicle = infraction.vehicle.identifierDocument
        }

        if (infraction.vehicle?.numDocument != null) {
            SingletonTicket.noIdentifierVehicle = infraction.vehicle.numDocument
        }

        SingletonTicket.expeditionAuthVehicle = "" //TODO: El servicio no lo envía.

        if (infraction.vehicle?.issuedIn != null) {
            SingletonTicket.stateExpVehicle = infraction.vehicle.issuedIn.toString()
        }


        infraction.fractions?.forEach { fracc ->
            SingletonTicket.fractionsList.add(SingletonTicket.ArticleFraction(
                    fracc?.article.toString(),
                    fracc?.numFraction.toString(),
                    fracc?.uma.toString(),
                    fracc?.reason.toString()))
        }

        SingletonTicket.streetInfraction = infraction.addressInfringement?.street.toString()
        SingletonTicket.betweenStreetInfraction = infraction.addressInfringement?.streetA.toString()
        SingletonTicket.andStreetInfraction = infraction.addressInfringement?.streetB.toString()
        SingletonTicket.colonyInfraction = infraction.addressInfringement?.colony.toString()
        SingletonTicket.retainedDocumentInfraction = infraction.insuredDocument.toString()

        if (infraction.is_impound!!) {
            SingletonTicket.isRemitedInfraction = true
            SingletonTicket.remitedDispositionInfraction = infraction.third_impound!!
        }

        infraction.captureLines?.forEach {
            SingletonTicket.captureLines.add(
                    SingletonTicket.CaptureLine(it?.key!!, if (it.discount_label == "0%") "Sin descuento" else "Con ${it.discount_label} de descuento", it.date!!, "%.2f".format(it.amount))
            )
        }

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

    //fun selector(cLines: NewCaptureLines): Date? = cLines.date

    fun doPaymentProcess(infraction: DetailResult) {
        var compareDate: Int
        val newCaptureLines: MutableList<NewCaptureLines> = mutableListOf()
        var captureSelected: NewCaptureLines = NewCaptureLines()


        infraction.captureLines?.forEach {

            val originalFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate = originalFormat.parse(it?.date)
            newCaptureLines.add(NewCaptureLines(it?.amount, it?.key, it?.order, newDate, it?.discount_label))
        }
        newCaptureLines.forEach {
            Log.v("CAPTURE_LINES", "No order: $it.date")
        }
        newCaptureLines.sortBy { captureLinesItem -> captureLinesItem.date }

        newCaptureLines.forEach {
            Log.v("CAPTURE_LINES", "Order: $it.date")
        }
        newCaptureLines.forEach { cDate ->
            compareDate = CURRENT_DATE.compareTo(cDate.date)
            //  0 comes when two date are same,
            //  1 comes when date1 is higher then date2
            // -1 comes when date1 is lower then date2
            if (compareDate <= 0) { //Si hoy es menor o igual a la fecha límite
                captureSelected = cDate
                return@forEach
            }
        }
        if (captureSelected.amount.isNullOrEmpty()) {
            //Hacer operación para calcular los recargos
        }

        discountInfraction = ((captureSelected.amount?.toDouble()?.minus(infraction.subtotal!!))!!.toFloat())
        folioInfraction = infraction.folio.toString()
        subtotalInfraction = infraction.subtotal?.toFloat()!!
        surchargesInfraction = 0F //En lo que los calculo
        totalInfraction = (subtotalInfraction - discountInfraction) + surchargesInfraction


        /* var compare_date: Int
         var haveToPay: Boolean = true
         val expDate50: Date = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_ii) }
         val expDateFull: Date = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_iii) }

         compare_date = CURRENT_DATE.compareTo(expDate50)//expDate50.compareTo(CURRENT_DATE)
         totalAmount = infraction.amount_capture_line_iii.toString()
         if (compare_date <= 0) { //Si hoy es menor o igual a la fecha limite
             //Tiene el descuento del 50%
             amountToPay = "%.2f".format(infraction.amount_capture_line_ii).replace(",", ".")
             discountPayment = (infraction.amount_capture_line_iii.toDouble() - infraction.amount_capture_line_ii.toDouble()).toString()
         } else {
             //No tiene descuento
             discountPayment = "0"
             compare_date = CURRENT_DATE.compareTo(expDateFull)//expDateFull.compareTo(CURRENT_DATE)
             if (compare_date <= 0) {
                 amountToPay = "%.2f".format(infraction.amount_capture_line_iii).replace(",", ".")
             } else {
                 haveToPay = false
             }
         }
         totalPayment = amountToPay
         *//* if (discountPayment.toDouble()>0){
             totalPayment = "%.2f".format(amountToPay.toDouble() - discountPayment.toDouble())
         }*//*


        if (haveToPay) {
            PaymentsTransfer.runTransaction(activity, totalPayment, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "La infracción cuenta con recargos. Pagar en ventanilla", Snackbar.LENGTH_LONG)
        }*/
    }

    override fun onPaymentClick(view: View, position: Int, origin: Int) {
        activity.showLoader("Espere ...")

        when (origin) {
            PAYMENT_LOCAL -> {
                TOKEN_INFRACTION = itemInfraOffLine[position].id_infraction.toString()
                localPayment()
            }
            PAYMENT_ONLINE -> {
                TOKEN_INFRACTION = itemInfraOnline[position].token.toString()
                iterator.value.doSearchByIdInfraction(TOKEN_INFRACTION, PAYMENT)
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
    }

    override fun onResultInfractionById(infraction: DetailResult, origin: Int) {
        activity.hideLoader()
        when (origin) {
            PRINT -> {
                activity.showLoader(getString(R.string.l_preparing_printer))
                printInfractionOnline(infraction)//PaymentsTransfer.print(activity, printInfraction(infraction), null, this)
            }
            PAYMENT ->
                if (!infraction.isAbsent!!) {
                    doPaymentProcess(infraction)
                    //idPerson = infraction.id_person
                } else {
                    //mandar a pantalla de actualizacion
                    SingletonInfraction.tokenInfraction = TOKEN_INFRACTION
                    /*SingletonInfraction.captureLineii = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction. date_capture_line_ii) }// infraction.capture_line_ii
                    SingletonInfraction.captureLineiii = SimpleDateFormat("dd/MM/yyyy").run { this.parse(infraction.date_capture_line_iii) }// infraction.capture_line_ii
                    SingletonInfraction.amountCaptureLineii = infraction.amount_capture_line_ii
                    SingletonInfraction.amountCaptureLineiii = infraction.amount_capture_line_iii*/

                    val intent = Intent(activity, CreateInfractionActivity::class.java)
                    intent.putExtra(EXTRA_OPTION_INFRACTION, OPTION_UPDATE_INFRACTION)
                    startActivityForResult(intent, OK_PAYMENT)
                }
        }
    }

    override fun onResultSearch(listInfractions: MutableList<DataItem>) {
        activity.hideLoader()
        itemInfraOnline = listInfractions

        val totalResults = listInfractions.size
        if (totalResults > 0) {
            constraint_results.visibility = View.VISIBLE
            binding.rclResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rclResults.adapter = SearchAdapter(listInfractions, this)
            binding.txtTotalSearch.text = totalResults.toString()

            binding.txtFilterSearch.text = binding.edtFilterAny.text.toString()
        } else {
            activity.showLoader("Buscando infracciones ...")
            lifecycleScope.launch {
                iterator.value.doSearchByFilterOffLine(binding.edtFilterAny.text.toString())
            }

        }


    }

    override fun onError(msg: String) {
        activity.hideLoader()
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
        if (binding.rclResults.adapter != null) {
            binding.constraintResults.visibility = View.GONE
            binding.rclResults.adapter = null
        }
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun clearData() {
        binding.edtFilterAny.setText("")
        binding.constraintResults.visibility = View.GONE
        binding.rclResults.adapter = null

    }

    override fun onResultSavePayment(msg: String) {
        activity.hideLoader()
        SnackbarHelper.showSuccessSnackBar(activity, "El pago se guardó satisfactoriamente.", Snackbar.LENGTH_SHORT)


    }

    override fun onTxApproved(txInfo: TransactionInfo) {
        isPaid = true
       /* val paymentRequest: PaymentRequest = PaymentRequest(
                discountPayment,
                "folio_infraction",
                "",
                CURRENT_DATE.toString(),
                "CARD",
                0F,
                "subtotal",
                "recargos",
                TOKEN_INFRACTION,
                totalAmount,
                txInfo.authorization
        )
        iterator.value.savePaymentToService(paymentRequest)*/
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
                PaymentsTransfer.runTransaction(activity, totalInfraction.toString(), if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
            }
            builder.setNegativeButton("Cancelar") { _, _ ->
                // Imprimir boleta
            }
            builder.show()
        }
    }

    override fun onTxFailed(retry: Boolean, message: String) {
        isPaid = false
        SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
    }

    override fun onCtlsDoubleTap() {
        // TODO("not implemented") To change body of created functions use File | Settings | File Templates.
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
            binding.txtFilterSearch.text = "Infracciones locales"
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "No se encontraron infracciones.", Snackbar.LENGTH_SHORT)
            constraint_results.visibility = View.GONE
        }


    }

    override suspend fun onResultInfractionByIdOffline(infraction: InfringementData, origin: Int) {
        when (origin) {
            PAYMENT -> doPaymentLocalProcess() //Hacer el proceso de pago
            PRINT -> {
                activity.showLoader(getString(R.string.l_preparing_printer))
                printLocalInfraction(infraction)
            }
        }
    }

    private fun doPaymentLocalProcess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onIdentifierDocReady(adapter: ArrayAdapter<String>) {
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
