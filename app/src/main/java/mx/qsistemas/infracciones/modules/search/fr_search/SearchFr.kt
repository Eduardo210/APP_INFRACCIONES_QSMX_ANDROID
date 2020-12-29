package mx.qsistemas.infracciones.modules.search.fr_search

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.CustomCardviewBinding
import mx.qsistemas.infracciones.databinding.FragmentSearchBinding
import mx.qsistemas.infracciones.db_web.entities.InfractionItem
import mx.qsistemas.infracciones.db_web.entities.InfringementData
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.create.OPTION_UPDATE_INFRACTION
import mx.qsistemas.infracciones.modules.search.SearchActivity
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.modules.search.SearchIterator
import mx.qsistemas.infracciones.modules.search.adapters.*
import mx.qsistemas.infracciones.net.catalogs.Articles
import mx.qsistemas.infracciones.net.catalogs.Fractions
import mx.qsistemas.infracciones.net.result_web.detail_result.DetailResult
import mx.qsistemas.infracciones.net.result_web.search_result.DataItem
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.*
import java.text.SimpleDateFormat
import java.util.*

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
       /* , IPaymentsTransfer.PrintListener
        , IPaymentsTransfer.TransactionListener, DetailPaymentCallback*/ {

    private lateinit var binding: FragmentSearchBinding
    private val iterator = lazy { SearchIterator(this) }
    private var idDocIdent: String = ""

    private val PRINT: Int = 100
    private val PAYMENT: Int = 200
    private val format = SimpleDateFormat("yyyy-MM-dd")
    private val CURRENT_DATE = format.parse(format.format(Date()))
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
                iterator.value.doSearchByFilterOffLine(binding.edtFilterAny.text.toString())

            }
        }
        binding.imgCleanSearch.setOnClickListener {
            clearData()
        }
        binding.imvDialogInfo.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val binding: CustomCardviewBinding = DataBindingUtil.inflate(activity.layoutInflater, R.layout.custom_cardview, null, false)
            builder.setCancelable(false)
            builder.setCustomTitle(binding.root)
            val mAlertDialog = builder.show()
            binding.btnAccepted.setOnClickListener {
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
        var article = ""
        var fraction = ""



        SingletonTicket.dateTicket = infraction.infringement?.date ?: ""
        SingletonTicket.folioTicket = infraction.infringement?.folio ?: ""

        SingletonTicket.completeNameOffender = "${infraction.driver?.name} ${infraction.driver?.paternal} ${infraction.driver?.maternal}"
        if (!infraction.driver?.rfc.isNullOrEmpty()) {
            SingletonTicket.rfcOffender = infraction.driver?.rfc ?: ""
        }
        if(infraction.payOrder !=null){
            //SingletonTicket.completeNamePayer = "${infraction.electronicBill?.name} ${infraction.electronicBill?.paternal} ${infraction.electronicBill?.maternal}"
            SingletonTicket.paymentAuthCode = infraction.payOrder!!.authorize_no.toString()
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
        if (!infraction.vehicleVehicles?.brand?.isEmpty()!!) {
            SingletonTicket.brandVehicle = infraction.vehicleVehicles!!.brand
        }
        if (!infraction.vehicleVehicles?.sub_brand.isNullOrEmpty()) {
            SingletonTicket.subBrandVehicle = infraction.vehicleVehicles?.sub_brand ?: ""
        }
        if (!infraction.vehicleVehicles?.class_type.isNullOrEmpty()) {
            SingletonTicket.typeVehicle = infraction.vehicleVehicles?.class_type ?: ""
        }
        if (!infraction.vehicleVehicles?.colour.isNullOrEmpty()) {
            SingletonTicket.colorVehicle = infraction.vehicleVehicles?.colour ?: ""
        }

        if (!infraction.vehicleVehicles?.identifier_document.isNullOrEmpty()) {
            SingletonTicket.identifierVehicle = infraction.vehicleVehicles?.identifier_document
                    ?: ""
        }

        SingletonTicket.noIdentifierVehicle = infraction.vehicleVehicles?.num_document ?: ""

        if (!infraction.vehicleVehicles?.document_type.isNullOrEmpty()) {
            SingletonTicket.expeditionAuthVehicle = infraction.vehicleVehicles?.document_type ?: ""
        }

        if (!infraction.vehicleVehicles?.issued_in.isNullOrEmpty()) {
            SingletonTicket.stateExpVehicle = infraction.vehicleVehicles?.issued_in ?: ""
        }

        if (!infraction.driverAddressDriver?.colony.isNullOrEmpty()) {
            SingletonTicket.colonyOffender = infraction.driverAddressDriver?.colony ?:""
        }
        if (!infraction.driverAddressDriver?.state.isNullOrEmpty()) {
            SingletonTicket.stateOffender = infraction.driverAddressDriver?.state ?: ""
        }
        if (!infraction.driverLicense?.license_number.isNullOrEmpty()) {
            SingletonTicket.noLicenseOffender = infraction.driverLicense?.license_number ?: ""
        }
        if (!infraction.driverLicense?.license_type.isNullOrEmpty()) {
            SingletonTicket.typeLicenseOffender = infraction.driverLicense?.license_type ?: ""
        }
        if (!infraction.driverLicense?.state_license.isNullOrEmpty()) {
            SingletonTicket.stateLicenseOffender = infraction.driverLicense?.state_license ?: ""
        }
        SingletonTicket.nameAgent = "${infraction.personTownhall?.name} ${infraction.personTownhall?.paternal} ${infraction.personTownhall?.maternal}"
        SingletonTicket.idAgent = infraction.personTownhall?.idPersona.toString()
        SingletonTicket.modelVehicle = infraction.vehicleVehicles?.year ?: ""

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

        SingletonTicket.colonyInfraction = infraction.infringementAddress?.colony ?: ""
        SingletonTicket.retainedDocumentInfraction = infraction.infringement?.insured_document ?: ""

        if (infraction.infringement?.is_impound!!) {
            SingletonTicket.isRemitedInfraction = true
            SingletonTicket.remitedDispositionInfraction = infraction.infringement?.third_impound
                    ?: ""
        }

        infraction.captureLines?.forEach {
            SingletonTicket.captureLines.add(SingletonTicket.CaptureLine(it.key, if (it.discount == "0%") "Sin descuento" else "Con ${it.discount} de descuento", it.date, "%.2f".format(it.amount)))
        }

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

        SingletonTicket.dateTicket = infraction.date.toString()
        SingletonTicket.folioTicket = infraction.folio.toString()

        SingletonTicket.completeNameOffender = "${infraction.driver?.name} ${infraction.driver?.paternal} ${infraction.driver?.maternal}"
        SingletonTicket.rfcOffender = infraction.driver?.rfc.toString()

        if (infraction.isAbsent) {
            if(infraction.driver?.address !=null){
                if (infraction.driver.address.street?.isNotBlank()!!) {
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

        //SingletonTicket.expeditionAuthVehicle = infraction.vehicle. ?: "" //TODO: Verificar que venga en el servicio

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
                    SingletonTicket.CaptureLine(it?.key!!, if (it.discount_label == "0%") "Sin descuento" else "Con ${it.discount_label} de descuento", it.date!!, it.amount!!)
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

    /*fun doPaymentProcess(infraction: DetailResult) {
        var compareDate: Int
        val newCaptureLines: MutableList<NewCaptureLines> = mutableListOf()
        var captureSelected: NewCaptureLines = NewCaptureLines()
        var total_umas = 0

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
        run loop@{
            newCaptureLines.forEach { cDate ->
                compareDate = CURRENT_DATE.compareTo(cDate.date)
                //  0 comes when two date are same,
                //  1 comes when date1 is higher then date2
                // -1 comes when date1 is lower then date2
                if (compareDate <= 0) { //Si hoy es menor o igual a la fecha límite
                    captureSelected = cDate
                    return@loop
                }
            }
        }

        infraction.fractions?.forEach {
            SingletonInfraction.motivationList.add(SingletonInfraction.DtoMotivation(Articles(), Fractions(uma = it?.uma!!.toInt()), ""))
        }


        Log.d("DISCOUNT-amount", captureSelected.amount)
        Log.d("DISCOUNT-subtotal", infraction.subtotal?.toString())
        if (captureSelected.amount.isNullOrEmpty()) {
            captureSelected = newCaptureLines[newCaptureLines.lastIndex]
            //Hacer operación para calcular los recargos
            Application.firestore?.collection(FS_COL_CITIES)?.document(Application.prefs?.loadData(R.string.sp_id_township, "")!!)?.get()?.addOnSuccessListener { townshipSnapshot ->
                if (townshipSnapshot == null) {
                    Log.e(this.javaClass.simpleName, Application.getContext().getString(R.string.e_firestore_not_available))
                    onError(Application.getContext().getString(R.string.e_firestore_not_available))
                    activity.hideLoader()
                } else {
                    val township = townshipSnapshot.toObject(Townships::class.java) ?: Townships()
                    val diff = Date().time - captureSelected.date?.time!!
                    val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                    discountInfraction = (infraction.subtotal!! - captureSelected.amount?.toDouble()!!).toFloat()
                    folioInfraction = infraction.folio.toString()
                    subtotalInfraction = infraction.subtotal?.toFloat()!!
                    surchargesInfraction = days * township.surcharges_rate
                    totalInfraction = subtotalInfraction + surchargesInfraction

                    Log.d("PAYMENT", discountInfraction.toString())
                    Log.d("PAYMENT", subtotalInfraction.toString())
                    Log.d("PAYMENT", surchargesInfraction.toString())
                    Log.d("PAYMENT", totalInfraction.toString())


                    val completeName = infraction.townHall!!.split(" ")

                    SingletonInfraction.nameOffender = completeName[0]
                    SingletonInfraction.lastFatherName = completeName[1]
                    SingletonInfraction.lastMotherName = completeName[2]
                    SingletonInfraction.subTotalInfraction = subtotalInfraction.toString()
                    SingletonInfraction.surchargesInfraction = "%.2f".format(days * township.surcharges_rate)
                    SingletonInfraction.totalInfraction = "%.2f".format(SingletonInfraction.subTotalInfraction.toFloat() + SingletonInfraction.surchargesInfraction.toFloat()).replace(",", ".")
                    val dialog = DetailPaymentDialog()
                    dialog.listener = this
                    dialog.showDeclineOption = false
                    dialog.isCancelable = false
                    dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)

                    //PaymentsTransfer.runTransaction(activity, "%.2f".format(totalInfraction), if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
                }
            }
        } else {
            discountInfraction = (infraction.subtotal!! - captureSelected.amount?.toDouble()!!).toFloat()
            folioInfraction = infraction.folio.toString()
            subtotalInfraction = infraction.subtotal.toFloat()
            surchargesInfraction = 0F
            totalInfraction = subtotalInfraction
            //PaymentsTransfer.runTransaction(activity, totalInfraction.toString(), if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)

            Log.d("PAYMENT", discountInfraction.toString())
            Log.d("PAYMENT", subtotalInfraction.toString())
            Log.d("PAYMENT", surchargesInfraction.toString())
            Log.d("PAYMENT", totalInfraction.toString())
            val completeName = infraction.townHall!!.split(" ")
            completeName.forEach {
                Log.v("OFFENDER", it)
            }
            SingletonInfraction.nameOffender = completeName[0]
            SingletonInfraction.lastFatherName = completeName[1]
            SingletonInfraction.lastMotherName = completeName[2]
            SingletonInfraction.subTotalInfraction = subtotalInfraction.toString()
            SingletonInfraction.discountInfraction = if (discountInfraction != 0F) "%.2f".format(discountInfraction) else "0.0"
            SingletonInfraction.totalInfraction = "%.2f".format(captureSelected.amount!!.toFloat())
            val dialog = DetailPaymentDialog()
            dialog.listener = this
            dialog.showDeclineOption = false
            dialog.isCancelable = false
            dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
        }

    }*/


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

    /*override fun onError(var1: Int, var2: String) {
        Log.e("PRINT", var2)
        SnackbarHelper.showErrorSnackBar(activity, var2, Snackbar.LENGTH_LONG)
    }

    override fun onFinish() {
        Log.d("PRINT", "SUCCESS")
    }*/

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onResultInfractionById(infraction: DetailResult, origin: Int) {
        activity.hideLoader()
        when (origin) {
            PRINT -> {
                activity.showLoader(getString(R.string.l_preparing_printer))
                printInfractionOnline(infraction)//PaymentsTransfer.print(activity, printInfraction(infraction), null, this)
            }
            PAYMENT ->{
                SingletonInfraction.tokenInfraction = TOKEN_INFRACTION
                infraction.fractions?.forEach {
                    SingletonInfraction.motivationList.add(SingletonInfraction.DtoMotivation(Articles(), Fractions(uma = it?.uma!!.toInt()), ""))
                }
                SingletonInfraction.folioInfraction = infraction.folio ?: ""
                SingletonInfraction.subTotalInfraction = "%.2f".format(infraction.subtotal
                        ?: 0.0)
                SingletonInfraction.captureLines = infraction.captureLines ?: mutableListOf()
                val intent = Intent(activity, CreateInfractionActivity::class.java)
                intent.putExtra(EXTRA_OPTION_INFRACTION, OPTION_UPDATE_INFRACTION)
                startActivityForResult(intent, OK_PAYMENT)
            }
          /*      if (!infraction.isAbsent!!) {
                    //doPaymentProcess(infraction)
                    SnackbarHelper.showErrorSnackBar(activity,"Opción no disponible", Snackbar.LENGTH_SHORT)
                    //idPerson = infraction.id_person
                } else {*/
                    //mandar a pantalla de actualizacion del pago

               // }
        }
    }

    override fun onResultSearch(listInfractions: MutableList<DataItem>) {
        activity.hideLoader()
        itemInfraOnline = listInfractions

        val totalResults = listInfractions.size
        if (totalResults > 0) {
            binding.constraintResults.visibility = View.VISIBLE
            binding.rclResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rclResults.adapter = SearchAdapter(listInfractions, this)
            binding.txtTotalSearch.text = totalResults.toString()
            binding.txtFilterSearch.text = binding.edtFilterAny.text.toString()
        } else {
            activity.showLoader("Buscando infracciones locales...")
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


    private fun clearData() {
        binding.edtFilterAny.setText("")
        binding.constraintResults.visibility = View.GONE
        binding.rclResults.adapter = null

    }

    /*override fun onResultSavePayment(msg: String) {
        activity.hideLoader()
        SnackbarHelper.showSuccessSnackBar(activity, "El pago se guardó satisfactoriamente.", Snackbar.LENGTH_SHORT)


    }

    override fun onTxApproved(txInfo: TransactionInfo) {
        isPaid = true
        var dateFormat = SimpleDateFormat("yyyy-MM-dd")
        Log.d("DISCOUNT-before", "$discountInfraction")
        val paymentRequest = PaymentRequest(
                discountInfraction,
                folioInfraction,
                "",
                dateFormat.format(CURRENT_DATE),
                "CARD",
                0F,
                subtotalInfraction,
                surchargesInfraction,
                TOKEN_INFRACTION,
                SingletonInfraction.totalInfraction.toFloat(),
                txInfo.authorization
        )
        iterator.value.savePaymentToService(paymentRequest, TOKEN_INFRACTION)
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
                PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
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
    }*/

    /*override fun onCtlsDoubleTap() {
        // TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }*/

   /* override fun onTxVoucherFailed(message: String) {
        SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
    }
*/
    override fun onResultSearchOffLine(listInfractions: MutableList<InfractionItem>) {
        activity.hideLoader()
        itemInfraOffLine = listInfractions
        val totalResults = listInfractions.size

        if (totalResults > 0) {
            binding.constraintResults.visibility = View.VISIBLE
            binding.rclResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rclResults.adapter = HistoricalAdapter(listInfractions, this)
            binding.txtTotalSearch.text = totalResults.toString()
            binding.txtFilterSearch.text = "Infracciones locales"
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "No se encontraron infracciones.", Snackbar.LENGTH_SHORT)
            binding.constraintResults.visibility = View.GONE
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

    /*override fun onDeclinePayment() {
        SnackbarHelper.showErrorSnackBar(activity, "Pago cancelado", Snackbar.LENGTH_SHORT)
    }

    override fun onAcceptPayment() {
        PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
    }*/

    companion object {

        @JvmStatic
        fun newInstance() =
                SearchFr().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
